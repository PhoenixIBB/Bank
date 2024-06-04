package org.example;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class BankInfoDisplay {

    //Поля
    private final List<BankTransaction> bankTransactions;
    double monthTransactionsSummary;
    Month currentMonth;
    long sharpQuantityVertical;

    //Конструктор
    public BankInfoDisplay(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    //Метод для вывода сводки на экран
    public void allInformation (BankStatementProcessor bankStatementProcessor) {
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

    public void showAllCategories () {
        Set<String> categories = new TreeSet<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            categories.add(bankTransaction.getDescription());
        }
        for(String category : categories) {
            System.out.println(category);
        }
    }

    //Метод для вывода гистограммы
    public void generateGystogram() {
        TreeMap<Month, List<BankTransaction>> sortedTransactionsMap = new TreeMap<>();
        // Добавим в мапу пары месяц-транзакции
        for (Month month : Month.values()) {
            sortedTransactionsMap.put(month, new ArrayList<>());
        }
        // Распределим транзакции по коллекциям в зависимости от их месяца
        for (BankTransaction bankTransaction : bankTransactions) {
            sortedTransactionsMap.get(bankTransaction.getDate().getMonth()).add(bankTransaction);
        }
        // Для каждого месяца создадим временную коллекцию транзакций, а также инициализируем временные буферные коллекции
        for (Month month : sortedTransactionsMap.keySet()) {
            List<BankTransaction> transactions = sortedTransactionsMap.get(month);
            // Пройдемся по транзакциям в данном месяце
            Map<String, BankTransaction> transactionMap = new HashMap<>();

            for (BankTransaction bankTransaction : transactions) {
                String description = bankTransaction.getDescription();
                if (transactionMap.containsKey(description)) {
                    BankTransaction existingTransaction = transactionMap.get(description);
                    BankTransaction combinedTransaction = new BankTransaction(existingTransaction.getDate(), existingTransaction.getAmount() + bankTransaction.getAmount(), description, true, bankTransaction.getNotification());
                    transactionMap.put(description, combinedTransaction);
                } else {
                    transactionMap.put(description, bankTransaction);
                }
            }

            // Создаем лист для использования калькулятора и сам калькулятор и рассчитаем расходы за месяц
            BankStatementProcessor calculator = new BankStatementProcessor(transactions);
            monthTransactionsSummary = calculator.calculateTotalAmount();
            TreeSet<BankTransaction> sortedSet = new TreeSet<>(transactionMap.values());

            // Блок вывода начинается
            System.out.println("\n" + month);
            System.out.println("Description:                   Amount:");

            for (BankTransaction bankTransaction : sortedSet) {
                sharpQuantityVertical = Math.round(bankTransaction.getAmount() * 100 / monthTransactionsSummary);
                System.out.print("• " + bankTransaction.getAdditionalDescription());
                for (int i = 1; i < (30 - bankTransaction.getDescription().length()); i++) {
                    System.out.print(" ");
                }
                for (int i = 1; i <= sharpQuantityVertical; i++) {
                    System.out.print("#");
                }
                System.out.print("   " + bankTransaction.getAmount() + "\n");

            }
        }
    }
}