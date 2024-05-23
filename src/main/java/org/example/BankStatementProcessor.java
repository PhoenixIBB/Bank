package org.example;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.*;

public class BankStatementProcessor {

    private final List<BankTransaction> bankTransactions;

    // Конструктор
    public BankStatementProcessor(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }


    // Суммирует все транзакции за всё время
    public double calculateTotalAmount () {
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
    public double calculateTotalAmount(final Month month, final String category) {
        return calculateTotalAmount(month, category, 0);
    }
    // Суммирует все транзакции в заданной категории за указанный год
    public double calculateTotalAmount(final int year, final String category) {
        return calculateTotalAmount(null, category, year);
    }
    // Обслуживающий перегруженный метод
    private double calculateTotalAmount(Month month, final String category, final int year) {
        double total = 0;
        try {
            for (final BankTransaction bankTransaction : bankTransactions) {
                if ((month == null || bankTransaction.getDate().getMonth() == month) &&
                        (category == null || bankTransaction.getDescription().equals(category)) &&
                        (year == 0 || bankTransaction.getDate().getYear() == year)) {
                    total += bankTransaction.getAmount();
                }
            }
        }catch (NoSuchElementException e) {
            System.out.println("\nИскомый элемент не найден. Выполнить расчет невозможно.");
        }
        return total;
    }

    // Вывести самую дорогую категорию
    public BankTransaction mostExpensiveCategory() {
        double max = 0;
        BankTransaction mostExpensiveTransaction = null;
        Scanner scan;
        try {
        scan = new Scanner (System.in);
        System.out.println("За какой временной период Вы хотели бы получить информацию? (Месяц или Год)");
        String chooseTimeValue = scan.nextLine();
        switch (chooseTimeValue) {
            case "Месяц", "месяц", "мес", "За месяц", "за месяц":
                System.out.println("За какой месяц Вы хотите получить информацию?");
                String mesyats = scan.nextLine();
                Month month = InputConverter.monthChooser(mesyats);
                for (BankTransaction bankTransaction : bankTransactions) {
                    if (bankTransaction.getDate().getMonth() == month) {
                        String category = bankTransaction.getDescription();
                        double price = calculateTotalAmount(month, category);
                        if (price <= max) {
                            max = price;
                            bankTransaction.setTotalPrice(price);
                            mostExpensiveTransaction = bankTransaction;
                        }
                    }
                }
                break;
            case "Год", "год", "За год", "за год":
                System.out.println("За какой год Вы хотели бы получить информацию?");
                int god = scan.nextInt();
                for (BankTransaction bankTransaction : bankTransactions) {
                    int year = bankTransaction.getDate().getYear();
                    if (year == god) {
                        String category = bankTransaction.getDescription();
                        double price = calculateTotalAmount(year, category);
                        if (price <= max) {
                            max = price;
                            bankTransaction.setTotalPrice(price);
                            mostExpensiveTransaction = bankTransaction;
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
    public BankTransaction cheapestCategory() {
        double min = -10000000;
        BankTransaction cheapestTransaction = null;
        Scanner scan;
        try {
            scan = new Scanner(System.in);
            System.out.println("За какой временной период Вы хотели бы получить информацию? (Месяц или Год)");
            String chooseTimeValue = scan.nextLine();
            switch (chooseTimeValue) {
                case "Месяц", "месяц", "мес", "За месяц", "за месяц":
                    System.out.println("За какой месяц Вы хотите получить информацию?");
                    String mesyats = scan.nextLine();
                    Month month = InputConverter.monthChooser(mesyats);
                    for (BankTransaction bankTransaction : bankTransactions) {
                        if (bankTransaction.getDate().getMonth() == month) {
                            String category = bankTransaction.getDescription();
                            double price = calculateTotalAmount(month, category);
                            if (price >= min && price < 0) {
                                min = price;
                                bankTransaction.setTotalPrice(price);
                                cheapestTransaction = bankTransaction;
                            }
                        }
                    }
                    break;
                case "Год", "год", "За год", "за год":
                    System.out.println("За какой год Вы хотели бы получить информацию?");
                    int god = scan.nextInt();
                    for (BankTransaction bankTransaction : bankTransactions) {
                        int year = bankTransaction.getDate().getYear();
                        if (year == god) {
                            String category = bankTransaction.getDescription();
                            double price = calculateTotalAmount(year, category);
                            if (price >= min && price < 0) {
                                min = price;
                                bankTransaction.setTotalPrice(price);
                                cheapestTransaction = bankTransaction;
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
    public void mostExpensiveOrMostCheap () {
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
        String monthName;
        String monthNameOutput;
        char firstChar;
        System.out.println("\nВас интересует наибольшая статья расходов или наименьшая? (Введите 1 или 2.)");
        try {
            Scanner scan = new Scanner(System.in);
            String choose = scan.nextLine();
            switch (choose) {
                case "1":
                    BankTransaction mostExpensive = bankStatementProcessor.mostExpensiveCategory();
                    monthName = InputConverter.monthConverter(mostExpensive.getDate().getMonth());
                    firstChar = monthName.charAt(0);
                    firstChar = Character.toLowerCase(firstChar);
                    monthNameOutput = firstChar + monthName.substring(1);
                    System.out.println("Наиболее затратная категория за " + monthNameOutput + " это: " + mostExpensive.getDescription() + ". \nЕё сумма составила: " + mostExpensive.getTotalPrice() + ".");
                    break;
                case "2":
                    BankTransaction cheapest = bankStatementProcessor.cheapestCategory();
                    monthName = InputConverter.monthConverter(cheapest.getDate().getMonth());
                    firstChar = monthName.charAt(0);
                    firstChar = Character.toLowerCase(firstChar);
                    monthNameOutput = firstChar + monthName.substring(1);
                    System.out.println("Наименее затратная категория за " + monthNameOutput + " это: " + cheapest.getDescription() + ". \nЕё сумма составила: " + cheapest.getTotalPrice() + ".");
                    break;
                default:
                    throw new IllegalArgumentException("Ошибка при выборе типа статьи расходов");
            }
        } catch (IllegalArgumentException | InputMismatchException | NullPointerException e) {
            System.out.println("\nОшибка выбора типа статьи расходов.");
        }
    }

    // Найти и вывести все транзакции, дороже указанной суммы за определенный период
    public void findTransactions (final BankTransactionFilter bankTransactionFilter) {
        try {
            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransactionFilter.test(bankTransaction)) {
                    System.out.println("\nИскомые транзакции:\n" + "Транзакция №" + bankTransaction.getOperationNumber() + "\nДата: " + bankTransaction.getDate() + ". Стоимость: " + bankTransaction.getAmount() + ". Описание: " + bankTransaction.getDescription());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nК сожалению, искомый элемент не найден.");
        }
    }

    // Получить транзакцию по номеру
    public void getTransactionByNumber (List<BankTransaction> bankTransactions) {
        try {
            Scanner scan = new Scanner(System.in);
            int i = 0;
            System.out.println("\nВведите номер искомой транзакции.");
            int number = scan.nextInt();
            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getOperationNumber() == number) {
                    System.out.println("Транзакция №" + number + ". Информация:\nДата транзакции: " + bankTransaction.getDate() + ". Стоимость: " + bankTransaction.getAmount() + ". Категория: " + bankTransaction.getDescription());
                    i++;
                }
            }
            if (i == 0) System.out.println("Транзакция с указанным номером не найдена.");
        } catch (NullPointerException | InputMismatchException e) {
            System.out.println("Неверный ввод номера транзакции.");
        }
    }

    // Получить транзакцию по дате
    public void getTransactionByDate () {
        try {Scanner scan = new Scanner(System.in);
            System.out.println("\nВведите дату искомой транзакции в формате 'dd-MM-yyyy'.");
            String dateLine = scan.nextLine();
            LocalDate date = LocalDate.parse(dateLine, BankXMLParser.DATE_PATTERN);
            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getDate() == date) {
                    System.out.println("Транзакция №" + bankTransaction.getOperationNumber() + ". Информация:\nДата транзакции: " + bankTransaction.getDate() + ". Стоимость: " + bankTransaction.getAmount() + ". Категория: " + bankTransaction.getDescription());
                }
            }
        } catch (DateTimeParseException | NullPointerException e) {
            System.out.println("Неверный формат даты. Пожалуйста, введите дату в формате 'dd-MM-yyyy'");
        }
    }


}

