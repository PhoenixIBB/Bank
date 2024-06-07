package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    private final LocalDate date;
    private final double amount;
    private final String description;
    private final String additionalDescription;
    public static List<Transaction> transactionsInvalid = new ArrayList<>();

    public Validator(LocalDate date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.additionalDescription = "Пустое значение";
    }

    public Validator(LocalDate date, double amount, String description, String additionalDescription) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.additionalDescription = additionalDescription;
    }

    public static void checkValidatorNotifications () {
        int i = 0;
        if (Validator.transactionsInvalid != null) {
            for (Transaction transaction : Validator.transactionsInvalid) {
                System.out.println("Некорректная транзакция №" + transaction.getOperationNumber() + ";");
                System.out.println("Содержимое транзакции: \nДата:" + transaction.getDate() + "; Стоимость: " + transaction.getAmount() + "; Категория: " + transaction.getDescription() + ".");
                System.out.println(transaction.notification.errorMessage());
            i++;
            }
        }
        if (i == 0) System.out.println("\nНекорректных транзакций нет.");
    }

    public Notification validate() {
        final Notification notification = new Notification();

        if (this.description.length() >= 30) {
            notification.addError("Слишком длинное описание.");
        }

        final LocalDate parsedDate;
        try {
            parsedDate = this.date;
            if(parsedDate.isAfter(LocalDate.now())) {
                notification.addError("Дата не должна быть будущей.");
            }
        } catch (DateTimeParseException e) {
            notification.addError("Недопустимый формат даты.");
        }

        final double amount;
        try {
            amount = this.amount;
            if (amount == 0) {
                notification.addError("Стоимость не должна быть равна нулю!");
            }
        } catch (NumberFormatException | NullPointerException e) {
            notification.addError("Недопустимый формат числа.");
        }
        return notification;
    }

}
