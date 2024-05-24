package org.example;

import java.io.IOException;
import java.time.Month;
import java.util.InputMismatchException;
import java.util.List;

import java.util.Scanner;

public class BankMain {
    private ImportManager importManager;
    private BankStatementParser bankParser;


    // 1.Зациклить работу приложения V
    // 2.Добавить обработку исключений V
    // 3.Покрыть большую часть кода тестами
    // 4.Реализовать поддержку импорта JSON, XML
    // 5.Реализовать поддержку экспорта HTML, CSV, JSON, XML
    // 6.Разработать базовый графический интерфейс GUI для анализатора банковских операций
    // 7.Подключить к коду notifications и validator
    // 8.summary statictics ещё зачем-то есть
    // 9.Рассмотреть парсинг из сберовской выписки
    // 10.Добавить работу со всеми транзакциями, а не только с расходами
    // 11.!!!Поработать над дубликатами, у нас не могло накопиться 400 транзакций, значит, мы закидываем ложные


    public static void main (String... args) throws IOException {
        //BankStatementParser bankStatementParser = new BankXMLParser();
        BankStatementParser bankStatementParser = new SecondHTMLParser();
        //BankStatementParser bankStatementParser = new BankCSVParser();
        final BankMain bankAnalyzer = new BankMain(bankStatementParser);
        bankAnalyzer.analyze();

    }

    // РЕАЛИЗАЦИЯ

    //Конструктор
    public BankMain(BankStatementParser bankParser) {
        this.importManager = new ImportManager("src/main/resources/");
        this.bankParser = bankParser;
        }

    // Главный управляющий метод
    public void analyze () throws IOException {
        //Инициализация необходимых объектов
        List <BankTransaction> bankTransactions = importManager.collectInformation(bankParser);
        final BankInfoDisplay bankInfoDisplay = new BankInfoDisplay(bankTransactions);
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
        System.out.println("Сборка завершена. \n\nТранзакций считано: " + BankTransaction.detected + "\nТранзакций успешно добавлено: " + BankTransaction.added + "\nНекорректных транзакций не добавлено: " + BankTransaction.badTransactions);
        Scanner scan;
        boolean flag = false;
        do {
            scan = new Scanner(System.in);
            try {
                // Вывод сообщения для пользователя
                System.out.println("""
                        \nЧто вы хотели бы сделать?
                        1.Вывести всю информацию по моим транзакциям;
                        2.Вывести информацию о транзакциях в формате гистрограммы;
                        3.Узнать наиболее или наименее затратную статью расходов за какой-то промежуток времени;
                        4.Найти определенные транзакции;
                        5.Для отмены и выхода из приложения.
                        6.Получить транзакцию по номеру.
                        7.Получить транзакцию по дате.
                        8.Для просмотра некорректных транзакций.
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
                    case 5 -> flag = true;
                    case 6 -> bankStatementProcessor.getTransactionByNumber(bankTransactions);
                    case 7 -> bankStatementProcessor.getTransactionByDate();
                    case 8 -> Validator.checkValidatorNotifications();
                }
            } catch (InputMismatchException | IllegalArgumentException | NullPointerException e) {
                System.out.println("\nОшибка ввода, введите корректный запрос.\n");
            }

        } while (!flag);
        scan.close();
    }
}
