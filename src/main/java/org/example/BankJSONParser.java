package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankJSONParser implements BankStatementParser {

    List<BankTransaction> bankTransactions = new ArrayList<>();
    // Задача усложнена: есть пустая строка, транзакции в разных форматах, поля написаны разными словами

    public List<BankTransaction> parseLinesFrom(List<String> lines) {

        String regexDate = "\\d{2}.\\d{2}.\\d{4}";
        String regexAmount = "(.?\\d+)(,)";
        String regexDescription = "\"\\w+\":\\s?\"(([A-Za-zА-Яа-я]+)(\\s?[A-Za-zА-Яа-я]+)*)\"";

        int i = 0;
        LocalDate date = null;
        double amount = 0;
        String description = null;

        for (String line : lines) {

            //if(line.matches("(\\s*)(\\{.\\w+.:)\\s*.\\d{2}-\\d{2}-\\d{4}.{2}\\s*.\\w+."));
            Pattern patternDate = Pattern.compile(regexDate);
            Pattern patternAmount = Pattern.compile(regexAmount);
            Pattern patternDescription = Pattern.compile(regexDescription);

            Matcher matcherDate = patternDate.matcher(line);
            Matcher matcherAmount = patternAmount.matcher(line);
            Matcher matcherDescription = patternDescription.matcher(line);

            if (matcherDate.find()) {
                String lineDate = matcherDate.group();
                date = LocalDate.parse(lineDate, DATE_PATTERN);
                i++;
            }
            if (matcherAmount.find()) {
                amount = Double.parseDouble(matcherAmount.group(1));
                i++;
            }
            if (matcherDescription.find()) {
                description = matcherDescription.group(1);
                i++;
            }
            if(i == 3) {
                BankTransaction bankTransaction = BankTransaction.validatedConstructor(date, amount, description);
                bankTransactions.add(bankTransaction);
                i = 0;
                date = null;
                amount = 0;
                description = null;
            }
        }
        return bankTransactions;
    }

    public List<BankTransaction> collectValidatedTransactions(List<String> lines) {
        final List<BankTransaction> bankTransactionsValid = new ArrayList<>();
        int operationNumber = 0;
        for (BankTransaction bankTransaction : parseLinesFrom(lines)) {
            if (bankTransaction.validated) {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                bankTransactionsValid.add(bankTransaction);
            } else {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                Validator.bankTransactionsInvalid.add(bankTransaction);
            }
        }
        return bankTransactionsValid;
    }

}
