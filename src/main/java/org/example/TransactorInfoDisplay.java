package org.example;

import java.time.LocalDate;
import java.util.*;

public class TransactorInfoDisplay {

    //Поля
    private final List<Transaction> transactions;

    //Конструктор
    public TransactorInfoDisplay(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void showExpensesByDescription() {

        System.out.println("Введите интересующую категорию.");
        Scanner scan = new Scanner(System.in);
        String descriptionFind = scan.nextLine();
        int i = 0;

        double totalByDescription = 0;

        System.out.println("Все транзакции в категории " + descriptionFind);
        for (Transaction transaction : Transaction.expenseTransactions) {
            if (transaction.getDescription().equals(descriptionFind)) {
                i++;
                System.out.println(i + ". Дата: " + transaction.getDate() + ". Категория: " + transaction.getDescription() + ". Стоимость:" + transaction.getAmount());
            }
        }

        for (Transaction transaction : transactions) {
            if (transaction.getDescription().equals(descriptionFind)) {
                totalByDescription += transaction.getAmount();
            }
        }
        System.out.println("\nСуммарный расход в данной категории: " + totalByDescription);
    }

    public void showTransactionsByNumbersRange() {
        try {
            System.out.println("Введите диапазон номеров транзакций в формате X-Y. (Например 10-99)");
            Scanner scan = new Scanner(System.in);
            String inputRange = scan.nextLine();
            String[] inputRangeValues = inputRange.split("-| - ");
            int leftEdge = Integer.parseInt(inputRangeValues[0]);
            int rightEdge = Integer.parseInt(inputRangeValues[1]);

            for (Transaction transaction : transactions) {
                if (transaction.getOperationNumber() >= leftEdge && transaction.getOperationNumber() <= rightEdge) {
                    System.out.println("\nТранзакция №" + transaction.getOperationNumber() + ".\nДата транзакции: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Категория: " + transaction.getDescription());
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
            LocalDate leftEdge = LocalDate.parse(inputRangeValues[0], TransactorParsers.DATE_PATTERN);
            LocalDate rightEdge = LocalDate.parse(inputRangeValues[1], TransactorParsers.DATE_PATTERN);
            transactions.sort((s1,s2) -> {
                if(s1.getDate().isAfter(s2.getDate())) return 1;
                else if (s1.getDate().isBefore(s2.getDate())) return -1;
                else return 0;
            });

            for (Transaction transaction : transactions) {
                if (transaction.getDate().isEqual(leftEdge) || transaction.getDate().isAfter(leftEdge) && transaction.getDate().isEqual(rightEdge) || transaction.getDate().isBefore(rightEdge)) {
                    System.out.println("\nТранзакция №" + transaction.getOperationNumber() + ".\nДата транзакции: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Категория: " + transaction.getDescription());
                }
            }
        } catch (InputMismatchException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    public void showTransactionsByAmountsRange() {
        try {
            System.out.println("Введите диапазон сумм транзакций в рублях в формате X-Y. (Например 1000-9990)");
            Scanner scan = new Scanner(System.in);
            String inputRange = scan.nextLine();
            String[] inputRangeValues = inputRange.split("-| - ");
            int leftEdge = Integer.parseInt(inputRangeValues[0]);
            int rightEdge = Integer.parseInt(inputRangeValues[1]);

            for (Transaction transaction : transactions) {
                if (transaction.getAmount() >= leftEdge && transaction.getAmount() <= rightEdge) {
                    System.out.println("\nТранзакция №" + transaction.getOperationNumber() + ".\nДата транзакции: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Категория: " + transaction.getDescription());
                }
            }
        } catch (InputMismatchException | IllegalArgumentException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    public void showAllDescriptions() {
        Set<String> descriptions = new TreeSet<>();
        for (Transaction transaction : transactions) {
            descriptions.add(transaction.getDescription());
        }
        int i = 0;
        for (String description : descriptions) {
            i++;
            System.out.println(i + ". " + description);
        }
    }

//    public void showAllAdditionalDescription() {
//        Set<String> additionalDescriptions = new TreeSet<>();
//        for (Transaction transaction : transactions) {
//            additionalDescriptions.add(transaction.getAdditionalDescription());
//        }
//        for (String additionalDescription : additionalDescriptions) {
//            System.out.println(additionalDescription);
//        }
//    }
}