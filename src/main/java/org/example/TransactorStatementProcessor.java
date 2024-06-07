package org.example;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TransactorStatementProcessor {

    private final List<Transaction> transactions;

    // Конструктор
    public TransactorStatementProcessor(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    // Суммирует все транзакции за всё время
    public double calculateTotalAmount() {
        return calculateTotalAmount(null, null, 0);
    }

    // Суммирует все транзакции за указанный месяц
    public double calculateTotalAmount(final Month month) {
        return calculateTotalAmount(month, null, 0);
    }

    // Суммирует транзакции в заданной категории
    public double calculateTotalAmount(final String category) {
        return calculateTotalAmount(null, category, 0);
    }

    // Суммирует транзакции в заданной категории за указанный месяц
    public double calculateTotalAmount(final Month month, final String category) { return calculateTotalAmount(month, category, 0);
    }

    // Суммирует все транзакции в заданной категории за указанный год
    public double calculateTotalAmount(final int year, final String category) { return calculateTotalAmount(null, category, year);
    }

    // Обслуживающий перегруженный метод
    private double calculateTotalAmount(Month month, final String category, final int year) {
        double total = 0;
        try {
            for (final Transaction transaction : transactions) {
                if ((month == null || transaction.getDate().getMonth() == month) &&
                        (category == null || transaction.getDescription().equals(category)) &&
                        (year == 0 || transaction.getDate().getYear() == year)) {
                    total += transaction.getAmount();
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nИскомый элемент не найден. Выполнить расчет невозможно.");
        }
        return total;
    }

    // Вывести самую дорогую категорию
    public Transaction mostExpensiveCategory() {
        double max = 0;
        Transaction mostExpensiveTransaction = null;
        Scanner scan;
        try {
            scan = new Scanner(System.in);
            System.out.println("\nЗа какой временной период Вы хотели бы получить информацию? (Например, май или 2023)");
            String chooseTimeValue = scan.nextLine();
            switch (chooseTimeValue) {
                case "Январь", "январь",
                        "Февраль", "февраль",
                        "Март", "март",
                        "Апрель", "апрель",
                        "Май", "май",
                        "Июнь", "июнь",
                        "Июль", "июль",
                        "Август", "август",
                        "Сентябрь", "сентябрь",
                        "Октябрь", "октябрь",
                        "Ноябрь", "ноябрь",
                        "Декабрь", "декабрь":
                    Month month = TransactorInputConverter.monthChooser(chooseTimeValue);
                    for (Transaction transaction : transactions) {
                        if (transaction.getDate().getMonth() == month) {
                            String category = transaction.getDescription();
                            double price = calculateTotalAmount(month, category);
                            if (price <= max) {
                                max = price;
                                transaction.setTotalPrice(price);
                                mostExpensiveTransaction = transaction;
                            }
                        }
                    }
                    break;
                case "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026":
                    for (Transaction transaction : transactions) {
                        int year = transaction.getDate().getYear();
                        int yearChoice = Integer.parseInt(chooseTimeValue);
                        if (year == yearChoice) {
                            String category = transaction.getDescription();
                            double price = calculateTotalAmount(year, category);
                            if (price <= max) {
                                max = price;
                                transaction.setTotalPrice(price);
                                mostExpensiveTransaction = transaction;
                            }
                        }
                    }
                    break;
            }
        } catch (NullPointerException | IllegalArgumentException | InputMismatchException e) {
            System.out.println("\nОшибка поиска наиболее затратной категории.");
        }
        return mostExpensiveTransaction;
    }

    // Вывести самую дешевую категорию
    public Transaction cheapestCategory() {
        double min = -10000000;
        Transaction cheapestTransaction = null;
        Scanner scan;
        try {
            scan = new Scanner(System.in);
            System.out.println("\nЗа какой временной период Вы хотели бы получить информацию? (Например, май или 2023)");
            String chooseTimeValue = scan.nextLine();
            switch (chooseTimeValue) {
                case "Январь", "январь",
                        "Февраль", "февраль",
                        "Март", "март",
                        "Апрель", "апрель",
                        "Май", "май",
                        "Июнь", "июнь",
                        "Июль", "июль",
                        "Август", "август",
                        "Сентябрь", "сентябрь",
                        "Октябрь", "октябрь",
                        "Ноябрь", "ноябрь",
                        "Декабрь", "декабрь":

                    Month month = TransactorInputConverter.monthChooser(chooseTimeValue);
                    for (Transaction transaction : transactions) {
                        if (transaction.getDate().getMonth() == month) {
                            String category = transaction.getDescription();
                            double price = calculateTotalAmount(month, category);
                            if (price >= min && price < 0) {
                                min = price;
                                transaction.setTotalPrice(price);
                                cheapestTransaction = transaction;
                            }
                        }
                    }
                    break;
                case "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026":

                    for (Transaction transaction : transactions) {
                        int year = transaction.getDate().getYear();
                        int yearChoice = Integer.parseInt(chooseTimeValue);
                        if (year == yearChoice) {
                            String category = transaction.getDescription();
                            double price = calculateTotalAmount(year, category);
                            if (price >= min && price < 0) {
                                min = price;
                                transaction.setTotalPrice(price);
                                cheapestTransaction = transaction;
                            }
                        }
                    }
                    break;
            }
        } catch (InputMismatchException | IllegalArgumentException | NullPointerException e) {
            System.out.println("\nОшибка поиска наименее затратной категории.");
        }
        return cheapestTransaction;
    }

    // Инициировать выбор и вывод выбранной категории
    public void mostExpensiveOrMostCheap() {
        final TransactorStatementProcessor transactorStatementProcessor = new TransactorStatementProcessor(transactions);
        String monthName;
        String monthNameOutput;
        char firstChar;
        System.out.println("\nВас интересует наибольшая статья расходов или наименьшая? (Введите 1 или 2.)\n");
        try {
            Scanner scan = new Scanner(System.in);
            String choose = scan.nextLine();
            switch (choose) {
                case "1":
                    Transaction mostExpensive = transactorStatementProcessor.mostExpensiveCategory();
                    monthName = TransactorInputConverter.monthConverter(mostExpensive.getDate().getMonth());
                    firstChar = monthName.charAt(0);
                    firstChar = Character.toLowerCase(firstChar);
                    monthNameOutput = firstChar + monthName.substring(1);
                    System.out.println("\nНаиболее затратная категория за " + monthNameOutput + " это: " + mostExpensive.getDescription() + ". \nЕё сумма составила: " + mostExpensive.getTotalPrice() + ".");
                    break;
                case "2":
                    Transaction cheapest = transactorStatementProcessor.cheapestCategory();
                    monthName = TransactorInputConverter.monthConverter(cheapest.getDate().getMonth());
                    firstChar = monthName.charAt(0);
                    firstChar = Character.toLowerCase(firstChar);
                    monthNameOutput = firstChar + monthName.substring(1);
                    System.out.println("\nНаименее затратная категория за " + monthNameOutput + " это: " + cheapest.getDescription() + ". \nЕё сумма составила: " + cheapest.getTotalPrice() + ".");
                    break;
                default:
                    throw new IllegalArgumentException("Ошибка при выборе типа статьи расходов");
            }
        } catch (IllegalArgumentException | InputMismatchException | NullPointerException e) {
            System.out.println("\nОшибка выбора типа статьи расходов.");
        }
    }

    // Найти и вывести все транзакции, дороже указанной суммы за определенный период
    public void findTransactions(final TransactorTransactionFilter transactorTransactionFilter) {
        int i = 0;
        try {
            for (Transaction transaction : transactions) {
                if (transactorTransactionFilter.test(transaction)) {
                    i++;
                    System.out.println("\nИскомые транзакции:\n" + "Транзакция №" + transaction.getOperationNumber() + "\nДата: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Описание: " + transaction.getDescription());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nК сожалению, искомый элемент не найден.");
        }
        if (i == 0) {
            System.out.println("\nТранзакции не найдены!");
        }
    }

    // Получить транзакцию по номеру
    public void getTransactionByNumber(List<Transaction> transactions) {
        try {
            Scanner scan = new Scanner(System.in);
            int i = 0;
            System.out.println("\nВведите номер искомой транзакции.");
            int number = scan.nextInt();
            for (Transaction transaction : transactions) {
                if (transaction.getOperationNumber() == number) {
                    System.out.println("\nТранзакция №" + number + ".\nДата транзакции: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Категория: " + transaction.getDescription());
                    i++;
                }
            }
            if (i == 0) System.out.println("\nТранзакция с указанным номером не найдена.");
        } catch (NullPointerException | InputMismatchException e) {
            System.out.println("\nНеверный ввод номера транзакции.");
        }
    }

    // Получить транзакцию по дате
    public void getTransactionByDate() {
        int i = 0;
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("\nВведите дату искомой транзакции в формате 'dd-MM-yyyy'.\n");
            String dateLine = scan.nextLine();
            LocalDate date = LocalDate.parse(dateLine, TransactorParsers.DATE_PATTERN);
            for (Transaction transaction : transactions) {
                if (transaction.getDate().equals(date)) {
                    i++;
                    System.out.println("\nТранзакция №" + transaction.getOperationNumber() + ".\nДата транзакции: " + transaction.getDate().format(TransactorParsers.SINGLE_DATE_PATTERN) + ". Стоимость: " + transaction.getAmount() + ". Категория: " + transaction.getDescription());
                }
            }
            if (i == 0) System.out.println("\nТранзакция не найдена.");
        } catch (DateTimeParseException | NullPointerException e) {
            System.out.println("\nНеверный формат даты. Пожалуйста, введите дату в формате 'dd-MM-yyyy'");
        }
    }


}

