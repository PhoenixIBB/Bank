package org.example;

import java.io.IOException;
import java.time.Month;
import java.util.InputMismatchException;
import java.util.List;

import java.util.Scanner;

public class BankMain {
    private final ImportManager importManager;

    // 3.Покрыть большую часть кода тестами
    // 5.Реализовать поддержку экспорта HTML, CSV, JSON, XML
    // 6.Разработать базовый графический интерфейс GUI для анализатора банковских операций
    // 8.summary statictics доработать сущность хранения и вывода результатов работы с транзакциями
    // 9.Рассмотреть парсинг из сберовской выписки
    // 10.Добавить работу со всеми транзакциями, а не только с расходами

    // Вывод регулярных списаний. Например, подключена услуга, которая уже не нужна, можно будет её увидеть
    // Супермаркеты (Караван)
    // Супермаркеты (Пятерочка)
    // Интернет-магазины (Озон)
    // Здоровье и красота (Апрель)
    // Прочие операции (Мобильный банк, комиссия)
    // Отдых и развлечения (Яндекс.такси)
    // Рестораны и кафе (Столовая ТПУ)
    // Всё для дома (ИП)

    // Вывод, сколько уходит на ту или иную выбранную категорию в месяц и в год
    // Доработать гистограмму
    // Возможно, доработать метод вывода в отдельном классе (например), чтобы избежать дублирования


    public static void main(String... args) throws IOException {

        BankMain bankAnalyzer = new BankMain();
        bankAnalyzer.analyze();

    }

    // РЕАЛИЗАЦИЯ

    //Конструктор
    public BankMain() {
        this.importManager = new ImportManager("src/main/resources/");
    }

    // Главный управляющий метод
    public void analyze() throws IOException {

        Scanner scan;
        boolean restart;
        boolean exit = false;

        while (!exit) {
            // Сбор транзакций
            BankTransaction.detected = 0;
            BankTransaction.added = 0;
            BankTransaction.badTransactions = 0;
            List<BankTransaction> bankTransactions = null;
            bankTransactions = importManager.collectInformation();
            // Формирование объекта вывода транзакций
            BankInfoDisplay bankInfoDisplay = new BankInfoDisplay(bankTransactions);
            // Объект для обработки транзакций
            BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

            restart = false;

            System.out.println("Сборка завершена. " +
                    "\n\nТранзакций считано: " + BankTransaction.detected + "." +
                    "\nТранзакций успешно добавлено: " + BankTransaction.added + "." +
                    "\nНекорректных транзакций не добавлено: " + BankTransaction.badTransactions + ".");

            while (!restart) {
                scan = new Scanner(System.in);
                try {
                    System.out.println("""
                            \nЧто вы хотели бы сделать?
                            1.Вывести всю информацию по моим транзакциям;
                            2.Вывести информацию о транзакциях в формате гистрограммы;
                            3.Узнать наиболее или наименее затратную статью расходов за какой-то промежуток времени;
                            4.Найти определенные транзакции;
                            5.Получить транзакцию по номеру.
                            6.Получить транзакцию по дате.
                            7.Получить транзакции в интервале номеров.
                            8.Получить транзакции за определенный промежуток времени.
                            9.Получить транзакции в определенном диапазоне сумм.
                            10.Для вывода всех категорий транзакций.
                            11.Для просмотра некорректных транзакций.
                            12.Для отмены и выхода из приложения.
                            13.Для возврата к выбору файла.
                            (Введите цифру.)
                            """);

                    // Обработка выбора и запроса
                    int choice = scan.nextInt();
                    switch (choice) {
                        case 1 -> bankInfoDisplay.allInformation(bankStatementProcessor);
                        case 2 -> bankInfoDisplay.generateGystogram();
                        case 3 -> bankStatementProcessor.mostExpensiveOrMostCheap();
                        case 4 -> {
                            System.out.println("\nТранзакции выше какой суммы вы хотели бы увидеть?");
                            double number = InputConverter.inputNumber();
                            if (number != -1) {
                                System.out.println("\nВ каком месяце?");
                                Month month = InputConverter.inputMonth();
                                if (month != null) {
                                    bankStatementProcessor.findTransactions(bankTransaction -> bankTransaction.getDate().getMonth() == month
                                            && bankTransaction.getAmount() <= -number);
                                }
                            }
                        }
                        case 5 -> bankStatementProcessor.getTransactionByNumber(bankTransactions);
                        case 6 -> bankStatementProcessor.getTransactionByDate();
                        case 7 -> bankInfoDisplay.showTransactionsByNumbersRange();
                        case 8 -> bankInfoDisplay.showTransactionsByDatesRange();
                        case 9 -> bankInfoDisplay.showTransactionsByAmountsRange();
                        case 10 -> bankInfoDisplay.showAllCategories();
                        case 11 -> Validator.checkValidatorNotifications();
                        case 12 -> {
                            scan.close();
                            restart = true;
                            exit = true;
                        }
                        case 13 -> restart = true;
                    }
                } catch (InputMismatchException | IllegalArgumentException | NullPointerException e) {
                    System.out.println("\nОшибка ввода, введите корректный запрос.\n");
                }
            }
        }
    }
}
