package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BankTransaction implements Comparable<BankTransaction> {
    private LocalDate date;
    private double amount;
    private String description;
    private double totalPrice;
    boolean validated = false;
    private int operationNumber = 0;
    static int detected = 0;
    static int added = 0;
    static int badTransactions = 0;
    Notification notification;

    //Конструктор
    public BankTransaction(LocalDate date, double amount, String description, boolean validated, Notification notification) {
            this.date = date;
            this.amount = amount;
            this.description = description;
            this.validated = validated;
            this.notification = notification;
    }

    // Реализовать запись ошибок в notification
    // V - она уже реализована в валидаторе - нужно вывести содержимое errors из notification в конце программы по вызову
    // Проверять, чтобы в коллекцию добавлялись только валидные транзакции с true
    // Сделать, чтобы в начале писало сколько транзакций добавлено, а сколько плохих
    // Сделать, чтобы в конце можно было вывести все плохие транзакции
    // Сделать корректные выводы ошибок, если неверно введен месяц и т.п.
    // Сделать вывод "не найдено" там, где объект не найден

    //Конструктор с валидацией входных данных
    public static BankTransaction validatedConstructor (LocalDate date, double amount, String description) {
        Validator validator = new Validator(date, amount, description);
        Notification notification;
        BankTransaction.detected += 1;
        if(!validator.validate().hasErrors()) {
            added += 1;
            notification = validator.validate();
            return new BankTransaction(date, amount, description, true, notification);
        } else {
            notification = validator.validate();
            badTransactions += 1;
        }
        return new BankTransaction(date, amount, description, false, notification);
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
