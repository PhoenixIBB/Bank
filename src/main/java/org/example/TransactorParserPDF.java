package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactorParserPDF implements TransactorParsers {

    List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> collectValidatedTransactions(List<String> lines) {

        List<Transaction> transactionsValid = new ArrayList<>();
        int operationNumber = 0;

        for (Transaction transaction : parseLinesFrom(lines)) {

            operationNumber++;
            transaction.setOperationNumber(operationNumber);

            if (transaction.validated) {
                transactionsValid.add(transaction);
            } else {
                TransactorValidator.transactionsInvalid.add(transaction);
            }
        }
        return transactionsValid;
    }

    public List<Transaction> parseLinesFrom(List<String> lines) {

        LocalDate date = null;
        double amount = 0;
        String description = null;
        String additionalDescription = null;
        String notAdditionalDescription = null;
        int i = 0;
        int j = 0;

        String regexDate = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s(\\d{2}:\\d{2})";
        String regexAmount = "^(\\+?(\\d+\\s?\\u00A0?)*\\d+,\\d{2})";
        String regexDescription = "^([А-Я][а-я]+(\\s[А-Яа-я]*)*)";
        String regexAdditionalDescription1 = "^((.+)(\\s\\w?\\d*)?(\\.\\sОперация по карте))";
        String regexAdditionalDescription2 = "^((.+)(\\s\\w?\\d*)?)";
        String regexUserName = "^([А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+$)";

        Pattern patternDate = Pattern.compile(regexDate);
        Pattern patternAmount = Pattern.compile(regexAmount);
        Pattern patterDescription = Pattern.compile(regexDescription);
        Pattern patternAdditionalDescription1 = Pattern.compile(regexAdditionalDescription1);
        Pattern patternAdditionalDescription2 = Pattern.compile(regexAdditionalDescription2);
        Pattern patternUserName = Pattern.compile(regexUserName);

        try {
            for (String line : lines) {

                Matcher matcherDate = patternDate.matcher(line);
                Matcher matcherAmount = patternAmount.matcher(line);
                Matcher matcherDescription = patterDescription.matcher(line);
                Matcher matcherAdditionalDescription1 = patternAdditionalDescription1.matcher(line);
                Matcher matcherAdditionalDescription2 = patternAdditionalDescription2.matcher(line);
                Matcher matcherUserName = patternUserName.matcher(line);

                if (matcherUserName.find()) {
                    TransactorReportGenerator.userName = matcherUserName.group(0);
                }

                if (j >= 1) {

                    if (matcherDate.find()) {
                        date = LocalDate.parse(matcherDate.group(1), DATE_PATTERN);
                        i++;
                    }
                    if (date != null) {
                        if (description == null) {
                            if (matcherDescription.find()) {
                                notAdditionalDescription = line;
                                description = matcherDescription.group(1).trim();
                                i++;
                            }
                        }
                        if (additionalDescription == null) {
                            if (matcherAdditionalDescription1.find()) {
                                String additionalDescriptionTempo = matcherAdditionalDescription1.group(2).replace("*", " ");
                                String[] additionalDescriptionArrayTempo = additionalDescriptionTempo.split("\\s\\w?\\d+\\.?$");
                                additionalDescription = TransactorInputConverter.additionalDescriptionConverter(additionalDescriptionArrayTempo[0]);
                                i++;
                            }
                        }
                        if (additionalDescription == null && description != null && amount == 0 && !line.equals(description + "\r") && !line.equals(notAdditionalDescription)) {
                            if (matcherAdditionalDescription2.find()) {
                                String additionalDescriptionTempo = matcherAdditionalDescription2.group(2);
                                additionalDescription = TransactorInputConverter.additionalDescriptionConverter(additionalDescriptionTempo);
                                i++;
                            }
                        }
                        if (matcherAmount.find()) {
                            String amountLine = matcherAmount.group(0);
                            if (!amountLine.matches("\\+(\\d+\\s?\\u00A0?)*\\d+,\\d{2}")) {
                                amountLine = "-" + amountLine;
                            }
                            amount = Double.parseDouble(amountLine.replaceAll("\\s", "").replaceAll("\\u00A0", "").replace(",", "."));
                            i++;
                        }
                        if (i == 4) {
                            Transaction transaction = Transaction.validatedConstructor(date, amount, description, additionalDescription);
                            if (transaction.getAmount() < 0 && !transaction.getDescription().matches("Банковские операции")) {
                                Transaction.expenseTransactions.add(transaction);
                            } else {
                                Transaction.incomeTransactions.add(transaction);
                            }
                            transactions.add(transaction);
                            i = 0;
                            date = null;
                            amount = 0;
                            description = null;
                            additionalDescription = null;
                        }
                        if (i > 4) {
                            System.out.println("Ошибка PDF-парсера. Сбился фокус с целевых данных.");
                        }
                    }
                }
                if (line.matches("([А-Яа-я]+\\s)?Сумма в валюте операции²\r|([А-Яа-я]+\\s)?Сумма в валюте счёта\r")) j++;
            }
        } catch (Exception e) {
            System.out.println("Ошибка PDF-парсера. Ошибка при сборке транзакций: " + e.getClass());
        }
        transactions.sort((bankTransactionFirst, bankTransactionSecond) -> {
            if (bankTransactionFirst.getDate().isAfter(bankTransactionSecond.getDate())) return 1;
            else if (bankTransactionFirst.getDate().isBefore(bankTransactionSecond.getDate())) return -1;
            else return 0;
        });
        return transactions;
    }

}

