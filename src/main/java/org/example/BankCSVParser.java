package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import static org.example.BankTransaction.validatedConstructor;

public class BankCSVParser implements BankStatementParser {

    static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BankTransaction formatFrom (final String line) {
        final String[] columns = line.split(",");
        LocalDate date = null;
        double amount = 0;
        String description = null;

        // Проверяем, что есть как минимум два элемента в строке
            if (columns.length >= 2) {
                // Преобразуем дату, если она доступна
                try {
                    if (columns[0] != null && !columns[0].isEmpty()) {
                        date = LocalDate.parse(columns[0], DATE_PATTERN);
                    }
                } catch (DateTimeParseException e) {
                    date = LocalDate.parse("01-01-2300", DATE_PATTERN);
                }
                // Преобразуем сумму, если она доступна
                try {
                    if (columns[1] != null && !columns[1].isEmpty()) {
                        amount = Double.parseDouble(columns[1]);
                    }
                } catch (NullPointerException e) {
                    amount = -1;
                }
                // Присваиваем описание, если оно доступно
                try {
                    if (columns.length >= 3 && columns[2] != null && !columns[2].isEmpty()) {
                        description = columns[2];
                    }
                } catch (NullPointerException | InputMismatchException e) {
                    System.out.println("Проблема при форматировании. Null или неверный ввод.");
                }
            }
        return validatedConstructor (date, amount, description);
    }

    public List<BankTransaction> parseLinesFrom(final List<String> lines) {
        final List<BankTransaction> bankTransactions = new ArrayList<>();
        for(final String line : lines) {
            bankTransactions.add(formatFrom((line)));
        }
        return bankTransactions;
    }

    public List<BankTransaction> collectValidatedTransactions (final List<String> lines) {
        final List<BankTransaction> bankTransactionsValid = new ArrayList<>();
        int operationNumber = 0;
        for (BankTransaction bankTransaction : parseLinesFrom(lines)) {
            if(bankTransaction.validated) {
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
