package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankPDFParser implements BankStatementParser {

    List<BankTransaction> bankTransactions = new ArrayList<>();

    // ЗАДАЧИ:
    // ПРОВЕРИТЬ ВСЕ ВНЕСЕННЫЕ ТРАНЗАКЦИИ НА КОРРЕКТНОСТЬ
    // ДОБАВИТЬ МЕТОДЫ ДЛЯ РАБОТЫ С ТРАНЗАКЦИЯМИ, Т.Е. ОСНОВНОЙ ФУНКЦИОНАЛ И ПО УСПЕВАНИЮ ЗАПИСЬ РЕЗУЛЬТАТОВ ФАЙЛ ИЛИ ИНОЙ ЭКСПОРТ

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

        Pattern patternDate = Pattern.compile(regexDate);
        Pattern patternAmount = Pattern.compile(regexAmount);
        Pattern patterDescription = Pattern.compile(regexDescription);
        Pattern patternAdditionalDescription = Pattern.compile(regexAdditionalDescription);

        try {
            for (String line : lines) {

                Matcher matcherDate = patternDate.matcher(line);
                Matcher matcherAmount = patternAmount.matcher(line);
                Matcher matcherDescription = patterDescription.matcher(line);
                Matcher matcherAdditionalDescription = patternAdditionalDescription.matcher(line);

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
                                String additionalDescriptionTempo = matcherAdditionalDescription.group(2);
                                String[] additionalDescriptionArrayTempo = additionalDescriptionTempo.split("\\s\\w?\\d+$");
                                additionalDescription = additionalDescriptionArrayTempo[0];
                                i++;
                            }
                        }
                        if (matcherAmount.find()) {
                            String amountLine = matcherAmount.group(0);
                            amount = Double.parseDouble(amountLine.replaceAll("\\s", "").replaceAll("\\u00A0", "").replace(",", "."));
                            i++;
                        }
                        if (i == 4) {
                            BankTransaction bankTransaction = BankTransaction.validatedConstructor(date, amount, description, additionalDescription);
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
//        TreeMap<Integer, BankTransaction> bankTransactionTreeMap = new TreeMap<Integer, BankTransaction>((bankTransactionFirst, bankTransactionSecond) -> {
//            if (bankTransactionFirst.getDate().isBefore(bankTransactionSecond.getDate())) return 1;
//            else if (bankTransactionFirst.getDate().isAfter(bankTransactionSecond.getDate())) return -1;
//            else return 0;
//        });
        bankTransactions.sort((bankTransactionFirst, bankTransactionSecond) -> {
            if (bankTransactionFirst.getDate().isAfter(bankTransactionSecond.getDate())) return 1;
            else if (bankTransactionFirst.getDate().isBefore(bankTransactionSecond.getDate())) return -1;
            else return 0;
        });
//        List<BankTransaction> bankTransactionsSorted = new ArrayList<BankTransaction>(bankTransactionTreeMap.values());
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

