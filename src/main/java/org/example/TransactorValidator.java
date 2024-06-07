package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactorValidator {

    private final LocalDate date;
    private final double amount;
    private final String description;
    private final String additionalDescription;
    public static List<Transaction> transactionsInvalid = new ArrayList<>();

    public TransactorValidator(LocalDate date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.additionalDescription = "Пустое значение";
    }

    public TransactorValidator(LocalDate date, double amount, String description, String additionalDescription) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.additionalDescription = additionalDescription;
    }

    public static void checkValidatorNotifications () {
        int i = 0;
        if (TransactorValidator.transactionsInvalid != null) {
            for (Transaction transaction : TransactorValidator.transactionsInvalid) {
                System.out.println("Некорректная транзакция №" + transaction.getOperationNumber() + ";");
                System.out.println("Содержимое транзакции: \nДата:" + transaction.getDate() + "; Стоимость: " + transaction.getAmount() + "; Категория: " + transaction.getDescription() + ".");
                System.out.println(transaction.transactorNotification.errorMessage());
            i++;
            }
        }
        if (i == 0) System.out.println("\nНекорректных транзакций нет.");
    }

    public TransactorNotification validate() {
        final TransactorNotification transactorNotification = new TransactorNotification();

        if (this.description.length() >= 30) {
            transactorNotification.addError("Слишком длинное описание.");
        }

        final LocalDate parsedDate;
        try {
            parsedDate = this.date;
            if(parsedDate.isAfter(LocalDate.now())) {
                transactorNotification.addError("Дата не должна быть будущей.");
            }
        } catch (DateTimeParseException e) {
            transactorNotification.addError("Недопустимый формат даты.");
        }

        final double amount;
        try {
            amount = this.amount;
            if (amount == 0) {
                transactorNotification.addError("Стоимость не должна быть равна нулю!");
            }
        } catch (NumberFormatException | NullPointerException e) {
            transactorNotification.addError("Недопустимый формат числа.");
        }
        return transactorNotification;
    }

}
