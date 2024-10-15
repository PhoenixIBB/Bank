package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactorParserJSON implements TransactorParsers {

    List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> parseLinesFrom(List<String> lines) {

        String regexDate = "\\d{2}.\\d{2}.\\d{4}";
        String regexAmount = "(\"[A-Za-zА-Яа-я]+\":)(\\s*\"?)(-?\\d+)(,)";
        String regexDescription = "(\"[A-Za-zА-Яа-я]+\":\\s*\")(([A-Za-zА-Яа-я]+)(\\s*[A-Za-zА-Яа-я]+)*)\"";

        int i = 0;
        LocalDate date = null;
        double amount = 0;
        String description = null;

        Pattern patternDate = Pattern.compile(regexDate);
        Pattern patternAmount = Pattern.compile(regexAmount);
        Pattern patternDescription = Pattern.compile(regexDescription);

        for (String line : lines) {

            Matcher matcherDate = patternDate.matcher(line);
            Matcher matcherAmount = patternAmount.matcher(line);
            Matcher matcherDescription = patternDescription.matcher(line);

            if (matcherDate.find()) {
                String lineDate = matcherDate.group();
                date = LocalDate.parse(lineDate, DATE_PATTERN);
                i++;
            }
            if (matcherAmount.find()) {
                amount = Double.parseDouble(matcherAmount.group(3));
                i++;
            }
            if (matcherDescription.find()) {
                description = matcherDescription.group(2);
                i++;
            }
            if (i == 3) {
                Transaction transaction = Transaction.validatedConstructor(date, amount, description);
                transactions.add(transaction);
                i = 0;
                date = null;
                amount = 0;
                description = null;
            }
        }
        return transactions;
    }

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

}
