package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {

    final private LocalDate date;
    final private double amount;
    final private String description;
    public static List<BankTransaction> bankTransactionsInvalid = new ArrayList<>();

    public Validator(LocalDate date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public static void checkValidatorNotifications () {
        int i = 0;
        if (Validator.bankTransactionsInvalid != null) {
            for (BankTransaction bankTransaction : Validator.bankTransactionsInvalid) {
                System.out.println("Некорректная транзакция №" + bankTransaction.getOperationNumber() + ";");
                System.out.println("Содержимое транзакции: \nДата:" + bankTransaction.getDate() + "; Стоимость: " + bankTransaction.getAmount() + "; Категория: " + bankTransaction.getDescription() + ".");
                System.out.println(bankTransaction.notification.errorMessage());
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
