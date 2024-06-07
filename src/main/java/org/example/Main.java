package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import java.util.Scanner;

public class Main {
    private final ImportManager importManager;

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
        this.importManager = new ImportManager("src/main/resources/");
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
            transactions = importManager.collectInformation();
            // Формирование объекта вывода транзакций
            InfoDisplay infoDisplay = new InfoDisplay(transactions);
            // Объект для обработки транзакций
            StatementProcessor statementProcessor = new StatementProcessor(transactions);
            ReportGenerator reportGenerator = new ReportGenerator(transactions);

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
                        case 1 -> reportGenerator.generateReport();
                        case 2 -> statementProcessor.mostExpensiveOrMostCheap();
                        case 3 -> statementProcessor.getTransactionByNumber(transactions);
                        case 4 -> statementProcessor.getTransactionByDate();
                        case 5 -> infoDisplay.showTransactionsByNumbersRange();
                        case 6 -> infoDisplay.showTransactionsByDatesRange();
                        case 7 -> infoDisplay.showTransactionsByAmountsRange();
                        case 8 -> reportGenerator.generateDiagram();
                        case 9 -> infoDisplay.showAllDescriptions();
                        case 10 -> Validator.checkValidatorNotifications();
                        case 11 -> {
                            scan.close();
                            restart = true;
                            exit = true;
                        }
                        case 12 -> {
                            transactions.clear();
                            Transaction.expenseTransactions.clear();
                            Transaction.incomeTransactions.clear();
                            Validator.transactionsInvalid.clear();
                            restart = true;
                        }
                        case 13 -> infoDisplay.showExpensesByDescription();
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
