package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankPDFParser implements BankStatementParser {

    List<BankTransaction> bankTransactions = new ArrayList<>();

    public List<BankTransaction> parseLinesFrom(List<String> lines) {

        LocalDate date = null;
        double amount = 0;
        String description = null;
        int i = 0;
        int j = 0;

        String regexDate = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s(\\d{2}:\\d{2})";
        String regexAmount = "^(\\+?(\\d+\\s?\\u00A0?)*\\d+,\\d{2})";
        String regexDescription = "^([А-Я][а-я]+(\\s[А-Яа-я]*)*)";

        Pattern patternDate = Pattern.compile(regexDate);
        Pattern patternAmount = Pattern.compile(regexAmount);
        Pattern patterDescription = Pattern.compile(regexDescription);

        try {
            for (String line : lines) {

                Matcher matcherDate = patternDate.matcher(line);
                Matcher matcherAmount = patternAmount.matcher(line);
                Matcher matcherDescription = patterDescription.matcher(line);

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
                        if (matcherAmount.find()) {
                            String amountLine = matcherAmount.group(0);
                            String amountLineInter1 = amountLine.replaceAll("\\s", "");
                            String amountLineInter2 = amountLineInter1.replaceAll("\\u00A0", "");
                            String amountLineFormatted = amountLineInter2.replace(",", ".");
                            amount = Double.parseDouble(amountLineFormatted);
                            i++;
                        }
                        if (i == 3) {
                            BankTransaction bankTransaction = BankTransaction.validatedConstructor(date, amount, description.toString());
                            bankTransactions.add(bankTransaction);
                            i = 0;
                            date = null;
                            amount = 0;
                            description = null;
                        }
                        if (i > 3) {
                            System.out.println("Ошибка PDF-парсера. Сбился фокус с целевых данных.");
                        }
                    }
                }
                if (line.matches("Сумма в валюте операции²\r")) j++;
            }
        } catch (Exception e) {
            System.out.println("Ошибка PDF-парсера. Ошибка при сборке транзакций: " + e.getClass());
        }
        TreeMap<BankTransaction, Integer> bankTransactionTreeMap = new TreeMap<BankTransaction, Integer>((bankTransactionFirst, bankTransactionSecond) -> {
            if (bankTransactionFirst.getDate().isAfter(bankTransactionFirst.getDate())) return 1;
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

