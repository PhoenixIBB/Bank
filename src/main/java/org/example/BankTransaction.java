package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankTransaction implements Comparable<BankTransaction> {
    private LocalDate date;
    private double amount;
    private String description;
    private String additionalDescription;
    private double totalPrice;
    boolean validated = false;
    private int operationNumber = 0;
    static int detected = 0;
    static int added = 0;
    static int badTransactions = 0;
    Notification notification;
    static List<BankTransaction> expenseTransactions = new ArrayList<>();
    static List<BankTransaction> incomeTransactions = new ArrayList<>();

    //Конструктор
    public BankTransaction(LocalDate date, double amount, String description, boolean validated, Notification notification) {
            this.date = date;
            this.amount = amount;
            this.description = description;
            this.validated = validated;
            this.notification = notification;
    }

    public BankTransaction(LocalDate date, double amount, String description, String additionalDescription, boolean validated, Notification notification) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.additionalDescription = additionalDescription;
        this.validated = validated;
        this.notification = notification;
    }

    //Конструктор с валидацией входных данных
    public static BankTransaction validatedConstructor (LocalDate date, double amount, String description) {

        Validator validator = new Validator(date, amount, description);
        Notification notification = validator.validate();
        BankTransaction.detected += 1;

        if(!notification.hasErrors()) {
            added += 1;
            return new BankTransaction(date, amount, description, true, notification);
        } else {
            badTransactions += 1;
            return new BankTransaction(date, amount, description, false, notification);
        }
    }

    public static BankTransaction validatedConstructor (LocalDate date, double amount, String description, String additionalDescription) {

        Validator validator = new Validator(date, amount, description, additionalDescription);
        Notification notification = validator.validate();
        BankTransaction.detected += 1;

        if(!notification.hasErrors()) {
            added += 1;
            if (description.matches("Прочие операции\\r?\\n?|Прочие расходы\\r?\\n?|Неизвестная категория(-)|Неизвестная категория")) {
                description = additionalDescription;
            }
            return new BankTransaction(date, amount, description, additionalDescription, true, notification);
        } else {
            badTransactions += 1;
            return new BankTransaction(date, amount, description, additionalDescription, false, notification);
        }
    }

    //Сеттеры

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }
    public void setAdditionalDescription(String additionalDescription) { this.additionalDescription = additionalDescription; }

    //Геттеры
    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public double getTotalPrice() {
        return totalPrice;
    }
    public Notification getNotification() {
        return notification;
    }
    public int getOperationNumber() {
        return operationNumber;
    }
    public String getAdditionalDescription() { return additionalDescription; }

    //Контракт
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description);
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "Дата=" + date +
                ", Значение=" + amount +
                ", Категория='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(BankTransaction o) {
        if(this.amount > o.getAmount()) {return 1;}
        else if(this.amount < o.getAmount()) {return -1;}
        else {return 0;}
    }

}
