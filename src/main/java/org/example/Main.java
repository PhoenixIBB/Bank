package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import java.util.Scanner;

public class Main {
    private final TransactorImportManager transactorImportManager;

    // 3.Покрыть большую часть кода тестами
    // 5.Реализовать поддержку экспорта HTML, CSV, JSON, XML
    // 6.Разработать базовый графический интерфейс GUI для анализатора банковских операций


    public static void main(String... args) throws IOException {
        Main bankAnalyzer = new Main();
        bankAnalyzer.analyze();

    }

    // РЕАЛИЗАЦИЯ

    //Конструктор
    public Main() {
        this.transactorImportManager = new TransactorImportManager("src/main/resources/");
    }

    // Главный управляющий метод
    public void analyze() throws IOException {

        Scanner scan;
        boolean restart;
        boolean exit = false;

        while (!exit) {
            // Сбор транзакций
            Transaction.detected = 0;
            Transaction.added = 0;
            Transaction.badTransactions = 0;
            List<Transaction> transactions;
            transactions = transactorImportManager.collectInformation();
            // Формирование объекта вывода транзакций
            TransactorInfoDisplay transactorInfoDisplay = new TransactorInfoDisplay(transactions);
            // Объект для обработки транзакций
            TransactorStatementProcessor transactorStatementProcessor = new TransactorStatementProcessor(transactions);
            TransactorReportGenerator transactorReportGenerator = new TransactorReportGenerator(transactions);

            restart = false;

            System.out.println("Сборка завершена. " +
                    "\n\nТранзакций считано: " + Transaction.detected + "." +
                    "\nТранзакций успешно добавлено: " + Transaction.added + "." +
                    "\nНекорректных транзакций не добавлено: " + Transaction.badTransactions + ".");

            while (!restart) {
                scan = new Scanner(System.in);
                try {
                    System.out.println("""
                            \nЧто вы хотели бы сделать?
                            1.Сгенерировать отчет по моим транзакциям (только из выписки СбербанкОнлайн)
                            2.Узнать наиболее или наименее затратную статью расходов за какой-то промежуток времени;
                            3.Получить транзакцию по номеру.
                            4.Получить транзакцию по дате.
                            5.Получить транзакции в интервале номеров.
                            6.Получить транзакции за определенный промежуток времени.
                            7.Получить транзакции в определенном диапазоне сумм.
                            8.Сгенерировать диаграмму расходов (только из выписки СбербанкОнлайн)
                            9.Просмотреть все категории моих транзакций
                            10.Для просмотра некорректных транзакций.
                            11.Для отмены и выхода из приложения.
                            12.Для возврата к выбору файла.
                            (Введите цифру.)
                            """);

                    // Обработка выбора и запроса
                    int choice = scan.nextInt();
                    switch (choice) {
                        case 1 -> transactorReportGenerator.generateReport();
                        case 2 -> transactorStatementProcessor.mostExpensiveOrMostCheap();
                        case 3 -> transactorStatementProcessor.getTransactionByNumber(transactions);
                        case 4 -> transactorStatementProcessor.getTransactionByDate();
                        case 5 -> transactorInfoDisplay.showTransactionsByNumbersRange();
                        case 6 -> transactorInfoDisplay.showTransactionsByDatesRange();
                        case 7 -> transactorInfoDisplay.showTransactionsByAmountsRange();
                        case 8 -> transactorReportGenerator.generateDiagram();
                        case 9 -> transactorInfoDisplay.showAllDescriptions();
                        case 10 -> TransactorValidator.checkValidatorNotifications();
                        case 11 -> {
                            scan.close();
                            restart = true;
                            exit = true;
                        }
                        case 12 -> {
                            transactions.clear();
                            Transaction.expenseTransactions.clear();
                            Transaction.incomeTransactions.clear();
                            TransactorValidator.transactionsInvalid.clear();
                            restart = true;
                        }
                        case 13 -> transactorInfoDisplay.showExpensesByDescription();
                    }
                } catch (InputMismatchException | IllegalArgumentException | NullPointerException e) {
                    System.out.println("\nОшибка ввода, введите корректный запрос.\n");
                } catch (InvalidFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
