package org.example;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class BankInfoDisplay {

    //Поля
    private final List<BankTransaction> bankTransactions;

    //Конструктор
    public BankInfoDisplay(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    //Метод для вывода сводки на экран
    public void allInformation(BankStatementProcessor bankStatementProcessor) {
        double roundedTotalAmount = (double) Math.round(bankStatementProcessor.calculateTotalAmount() * 100) / 100;
        System.out.println("\nЗатрат за всё время: " + roundedTotalAmount);
    }

    public void showTransactionsByNumbersRange() {
        try {
            System.out.println("Введите диапазон номеров транзакций в формате X-Y. (Например 10-99)");
            Scanner scan = new Scanner(System.in);
            String inputRange = scan.nextLine();
            String[] inputRangeValues = inputRange.split("-| - ");
            int leftEdge = Integer.parseInt(inputRangeValues[0]);
            int rightEdge = Integer.parseInt(inputRangeValues[1]);

            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getOperationNumber() >= leftEdge && bankTransaction.getOperationNumber() <= rightEdge) {
                    System.out.println("(" + bankTransaction.getAdditionalDescription() + ").");
                    System.out.println("\nТранзакция №" + bankTransaction.getOperationNumber() + ".\nДата транзакции: " + bankTransaction.getDate().format(BankStatementParser.SINGLE_DATE_PATTERN) + ". Стоимость: " + bankTransaction.getAmount() + ". Категория: " + bankTransaction.getDescription());
                }
            }
        } catch (InputMismatchException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    public void showTransactionsByDatesRange() {
        try {
            System.out.println("Введите диапазон дат транзакций в формате dd.MM.yyyy-dd.MM.yyyy. (Например 10.01.2023-12.02.2023)");
            Scanner scan = new Scanner(System.in);
            String inputRange = scan.nextLine();
            String[] inputRangeValues = inputRange.split("-| - ");
            LocalDate leftEdge = LocalDate.parse(inputRangeValues[0], BankStatementParser.DATE_PATTERN);
            LocalDate rightEdge = LocalDate.parse(inputRangeValues[1], BankStatementParser.DATE_PATTERN);

            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getDate().isEqual(leftEdge) || bankTransaction.getDate().isAfter(leftEdge) && bankTransaction.getDate().isEqual(rightEdge) || bankTransaction.getDate().isBefore(rightEdge)) {
                    System.out.println("\nТранзакция №" + bankTransaction.getOperationNumber() + ".\nДата транзакции: " + bankTransaction.getDate().format(BankStatementParser.SINGLE_DATE_PATTERN) + ". Стоимость: " + bankTransaction.getAmount() + ". Категория: " + bankTransaction.getDescription());
                }
            }
        } catch (InputMismatchException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    public void showTransactionsByAmountsRange() {
        try {
            System.out.println("Введите диапазон номеров транзакций в рублях в формате X-Y. (Например 1000-9990)");
            Scanner scan = new Scanner(System.in);
            String inputRange = scan.nextLine();
            String[] inputRangeValues = inputRange.split("-| - ");
            int leftEdge = Integer.parseInt(inputRangeValues[0]);
            int rightEdge = Integer.parseInt(inputRangeValues[1]);

            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getAmount() >= leftEdge && bankTransaction.getAmount() <= rightEdge) {
                    System.out.println("\nТранзакция №" + bankTransaction.getOperationNumber() + ".\nДата транзакции: " + bankTransaction.getDate().format(BankStatementParser.SINGLE_DATE_PATTERN) + ". Стоимость: " + bankTransaction.getAmount() + ". Категория: " + bankTransaction.getDescription());
                }
            }
        } catch (InputMismatchException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    public void showAllDescriptions() {
        Set<String> descriptions = new TreeSet<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            descriptions.add(bankTransaction.getDescription());
        }
        int i = 0;
        for (String description : descriptions) {
            i++;
            System.out.println(i + ". " + description);
        }
    }

    public void showAllAdditionalDescription() {
        Set<String> additionalDescriptions = new TreeSet<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            additionalDescriptions.add(bankTransaction.getAdditionalDescription());
        }
        for (String additionalDescription : additionalDescriptions) {
            System.out.println(additionalDescription);
        }
    }
}