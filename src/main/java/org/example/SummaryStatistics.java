package org.example;
import java.time.Month;
import java.util.List;

public class SummaryStatistics {
    private double sum;
    private double max;
    private double min;
    private double average;

    public SummaryStatistics(final double sum, final double max, final double min, final double average) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.average = average;
    }

    public void makingStatistics (List<BankTransaction> bankTransactions) {

        BankTransaction mostExpensiveTransaction = null;
        BankStatementProcessor summaryProcessor = new BankStatementProcessor(bankTransactions);
        sum = summaryProcessor.calculateTotalAmount();

        for (Month month : Month.values()) {
            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getDate().getMonth() == month) {
                    String category = bankTransaction.getDescription();
                    double price = summaryProcessor.calculateTotalAmount(month, category);
                    if (price <= max) {
                        max = price;
                        bankTransaction.setTotalPrice(price);
                        summaryProcessor.mostExpensiveTransaction = bankTransaction;
                    }
                }
            }

        }


    }

    public double getSum()  {
        return sum;
    }
    public double getMax() {
        return max;
    }
    public double getMin() {
        return min;
    }
    public double getAverage() {
        return average;
    }

}
