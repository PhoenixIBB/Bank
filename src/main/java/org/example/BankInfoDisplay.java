package org.example;

import java.time.Month;
import java.util.*;

public class BankInfoDisplay {
// TestChanges
    //Поля
    private final List<BankTransaction> bankTransactions;
    double monthTransactionsSummary;
    Month currentMonth;
    long sharpQuantityVertical;
// }{yu
    //Конструктор
    public BankInfoDisplay(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    //Метод для вывода сводки на экран
    public void allInformation (BankStatementProcessor bankStatementProcessor) {
        System.out.println("Затрат за всё время: " + bankStatementProcessor.calculateTotalAmount());
    }

    //Метод для вывода гистограммы
    public void generateGystogram() {
        TreeMap<Month, List<BankTransaction>> sortedTransactionsMap = new TreeMap<>();
        // Добавим в мапу пары месяц-транзакции
        for (Month month : Month.values()) {
            sortedTransactionsMap.put(month, new ArrayList<BankTransaction>());
        }
        // Распределим транзакции по коллекциям в зависимости от их месяца
        for (BankTransaction bankTransaction : bankTransactions) {
            currentMonth = bankTransaction.getDate().getMonth();
            sortedTransactionsMap.get(currentMonth).add(bankTransaction);
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
                System.out.print("• " + bankTransaction.getDescription());
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