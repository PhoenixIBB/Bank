package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.example.BankTransaction.expenseTransactions;
import static org.example.BankTransaction.incomeTransactions;

public class BankPDFParser implements BankStatementParser {

    List<BankTransaction> bankTransactions = new ArrayList<>();

    public List<BankTransaction> parseLinesFrom(List<String> lines) {

        LocalDate date = null;
        double amount = 0;
        String description = null;
        String additionalDescription = null;
        int i = 0;
        int j = 0;

        String regexDate = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s(\\d{2}:\\d{2})";
        String regexAmount = "^(\\+?(\\d+\\s?\\u00A0?)*\\d+,\\d{2})";
        String regexDescription = "^([А-Я][а-я]+(\\s[А-Яа-я]*)*)";
        String regexAdditionalDescription = "^((.+)(\\s\\w?\\d*)?(\\.\\sОперация по карте))";
        String regexUserName = "^([А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+$)";

        Pattern patternDate = Pattern.compile(regexDate);
        Pattern patternAmount = Pattern.compile(regexAmount);
        Pattern patterDescription = Pattern.compile(regexDescription);
        Pattern patternAdditionalDescription = Pattern.compile(regexAdditionalDescription);
        Pattern patternUserName = Pattern.compile(regexUserName);

        try {
            for (String line : lines) {

                Matcher matcherDate = patternDate.matcher(line);
                Matcher matcherAmount = patternAmount.matcher(line);
                Matcher matcherDescription = patterDescription.matcher(line);
                Matcher matcherAdditionalDescription = patternAdditionalDescription.matcher(line);
                Matcher matcherUserName = patternUserName.matcher(line);

                if (matcherUserName.find()) {
                    BankReportGenerator.userName = matcherUserName.group(0);
                }

                if (j >= 1) {

                    if (matcherDate.find()) {
                        date = LocalDate.parse(matcherDate.group(1), DATE_PATTERN);
                        i++;
                    }
                    if (date != null) {
                        if (description == null) {
                            if (matcherDescription.find()) {
                                description = matcherDescription.group(1);
                                i++;
                            }
                        }
                        if (additionalDescription == null) {
                            if (matcherAdditionalDescription.find()) {
                                String additionalDescriptionTempo = matcherAdditionalDescription.group(2).replace("*", " ");
                                String[] additionalDescriptionArrayTempo = additionalDescriptionTempo.split("\\s\\w?\\d+\\.?$");
                                additionalDescription = InputConverter.additionalDescriptionConverter(additionalDescriptionArrayTempo[0]);
                                i++;
                            }
                        }
                        if (matcherAmount.find()) {
                            String amountLine = matcherAmount.group(0);
                            if (amountLine.matches("\\+(\\d+\\s?\\u00A0?)*\\d+,\\d{2}")) {
                                amount = Double.parseDouble(amountLine.replaceAll("\\s", "").replaceAll("\\u00A0", "").replace(",", "."));
                            } else {
                                amountLine = "-" + amountLine;
                                amount = Double.parseDouble(amountLine.replaceAll("\\s", "").replaceAll("\\u00A0", "").replace(",", "."));
                            }
                            i++;
                        }
                        if (i == 4) {
                            BankTransaction bankTransaction = BankTransaction.validatedConstructor(date, amount, description, additionalDescription);
                            if (bankTransaction.getAmount() < 0 && !bankTransaction.getDescription().matches("Банковские операции")) {
                                BankTransaction.expenseTransactions.add(bankTransaction);
                            } else {
                                BankTransaction.incomeTransactions.add(bankTransaction);
                            }
                            bankTransactions.add(bankTransaction);
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
                if (line.matches("Сумма в валюте операции²\r")) j++;
            }
        } catch (Exception e) {
            System.out.println("Ошибка PDF-парсера. Ошибка при сборке транзакций: " + e.getClass());
        }
        bankTransactions.sort((bankTransactionFirst, bankTransactionSecond) -> {
            if (bankTransactionFirst.getDate().isAfter(bankTransactionSecond.getDate())) return 1;
            else if (bankTransactionFirst.getDate().isBefore(bankTransactionSecond.getDate())) return -1;
            else return 0;
        });
        return bankTransactions;
    }

    public List<BankTransaction> collectValidatedTransactions(List<String> lines) {

        List<BankTransaction> bankTransactionsValid = new ArrayList<>();
        int operationNumber = 0;

        for (BankTransaction bankTransaction : parseLinesFrom(lines)) {

            operationNumber++;
            bankTransaction.setOperationNumber(operationNumber);

            if (bankTransaction.validated) {
                bankTransactionsValid.add(bankTransaction);
            } else {
                Validator.bankTransactionsInvalid.add(bankTransaction);
            }
        }
        return bankTransactionsValid;
    }

}

