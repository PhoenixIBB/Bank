package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactorParserPDFSecond implements TransactorParsers {

    List<Transaction> transactions = new ArrayList<>();

    // Натравить чатжпт на банковские выписки мои и чужие
    // Парсер работает, но пока кривовато. Он плохо понимает
    @Override
    public List<Transaction> parseLinesFrom(List<String> lines) {

        String regexDate = "^((\\d{2}[-.]{1}\\d{2}[-.]{1}\\d{2}(\\d{2})?)(\\s\\d{2}[-.]{1}))";
        String regexAmount = "(\\+?((\\d*\\s)?\\d+)(,\\d{2}))";
        String regexDescription = "(RUS\\s[A-Za-zА-Яа-я]+)\\s([A-Za-z-А-Яа-я]*\\s?[A-Za-zА-Яа-я]*)";

        LocalDate date = null;
        double amount = 0;
        String description = null;
        int i = 0;
        int j = 0;

        for (String line : lines) {

            Pattern patternDate = Pattern.compile(regexDate);
            Pattern patternAmount = Pattern.compile(regexAmount);
            Pattern patternDescription = Pattern.compile(regexDescription);

            Matcher matcherDate = patternDate.matcher(line);
            Matcher matcherAmount = patternAmount.matcher(line);
            Matcher matcherDescription = patternDescription.matcher(line);

            if (matcherDate.find()) {
                date = LocalDate.parse(matcherDate.group(2), TransactorParsers.DATE_PATTERN);
                i++;
                j = 1;
            }
            if (j == 1) {
                if (matcherAmount.find()) {
                    String amountLineReplaced = matcherAmount.group(1).replace(",", ".").replace(" ", "");
                    amount = Double.parseDouble(amountLineReplaced);
                    i++;
                }
                if (matcherDescription.find()) {
                    description = matcherDescription.group(0);
                    i++;
                }
                if (i == 3) {
                    Transaction transaction = Transaction.validatedConstructor(date, amount, description);
                    transactions.add(transaction);
                    date = null;
                    amount = 0;
                    description = null;
                    i = 0;
                }
                if (i > 4) {
                    System.out.println("\nОшибка PDF-парсера. Слетел фокус с целевых данных.\n");
                    break;
                }
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> collectValidatedTransactions(List<String> lines) {
        List<Transaction> transactionsValid = new ArrayList<>();
        int operationNumber = 0;
        for (Transaction transaction : parseLinesFrom(lines)) {
            if (transaction.validated) {
                operationNumber++;
                transaction.setOperationNumber(operationNumber);
                transactionsValid.add(transaction);
            } else {
                operationNumber++;
                transaction.setOperationNumber(operationNumber);
                TransactorValidator.transactionsInvalid.add(transaction);
            }
        }
        return transactionsValid;
    }
}
