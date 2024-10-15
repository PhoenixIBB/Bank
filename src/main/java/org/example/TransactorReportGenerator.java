package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class TransactorReportGenerator {

    // Основная информация
    public static String userName = "";
    public TreeMap<String, Double> categories = new TreeMap<>();
    List<Transaction> transactions;
    double mostExpensive1 = 0;
    double mostExpensive2 = 0;
    double mostExpensive3 = 0;
    double mostExpensive4 = 0;
    double mostExpensive5 = 0;
    String mostExpensiveDescr1 = null;
    String mostExpensiveDescr2 = null;
    String mostExpensiveDescr3 = null;
    String mostExpensiveDescr4 = null;
    String mostExpensiveDescr5 = null;
    String header;
    double roundedAmountExpense;
    double roundedAmountIncome;
    LocalDate startOfPeriod;
    LocalDate endOfPeriod;
    int redRandom;
    int greenRandom;
    int blueRandom;

    public TransactorReportGenerator(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public LocalDate getStartOfPeriod(List<Transaction> transactions) {
        LocalDate startOfPeriod = LocalDate.MAX;
        for (Transaction transaction : transactions) {
            LocalDate tempoDate = transaction.getDate();
            if (tempoDate.isBefore(startOfPeriod)) {
                startOfPeriod = tempoDate;
            }
        }
        return startOfPeriod;
    }

    public LocalDate getEndOfPeriod(List<Transaction> transactions) {
        LocalDate endOfPeriod = LocalDate.MIN;
        for (Transaction transaction : transactions) {
            LocalDate tempoDate = transaction.getDate();
            if (tempoDate.isAfter(endOfPeriod)) {
                endOfPeriod = tempoDate;
            }
        }
        return endOfPeriod;
    }

    // Общие данные по транзакциям:
    public void collectInfo() {
        startOfPeriod = getStartOfPeriod(transactions);
        endOfPeriod = getEndOfPeriod(transactions);

        roundedAmountExpense = (double) Math.round(amountSumExpensesCalculator() * 100) / 100;
        roundedAmountIncome = (double) Math.round(amountSumIncomesCalculator() * 100) / 100;

        for (Map.Entry<String, Double> entry : categories.entrySet()) {
            double tempo = entry.getValue();
            String tempoDescription = entry.getKey();

            if (!entry.getKey().equals("Банковские операции") && !entry.getKey().equals("Перевод на карту") && !entry.getKey().equals("Перевод с карты") && !entry.getKey().equals("Выдача наличных") && entry.getValue() < 0) {

                if (tempo < mostExpensive1) {
                    mostExpensive5 = mostExpensive4;
                    mostExpensiveDescr5 = mostExpensiveDescr4;
                    mostExpensive4 = mostExpensive3;
                    mostExpensiveDescr4 = mostExpensiveDescr3;
                    mostExpensive3 = mostExpensive2;
                    mostExpensiveDescr3 = mostExpensiveDescr2;
                    mostExpensive2 = mostExpensive1;
                    mostExpensiveDescr2 = mostExpensiveDescr1;
                    mostExpensive1 = tempo;
                    mostExpensiveDescr1 = tempoDescription;
                } else if (tempo < mostExpensive2 && tempo > mostExpensive1) {
                    mostExpensive5 = mostExpensive4;
                    mostExpensiveDescr5 = mostExpensiveDescr4;
                    mostExpensive4 = mostExpensive3;
                    mostExpensiveDescr4 = mostExpensiveDescr3;
                    mostExpensive3 = mostExpensive2;
                    mostExpensiveDescr3 = mostExpensiveDescr2;
                    mostExpensive2 = tempo;
                    mostExpensiveDescr2 = tempoDescription;
                } else if (tempo < mostExpensive3 && tempo > mostExpensive2) {
                    mostExpensive5 = mostExpensive4;
                    mostExpensiveDescr5 = mostExpensiveDescr4;
                    mostExpensive4 = mostExpensive3;
                    mostExpensiveDescr4 = mostExpensiveDescr3;
                    mostExpensive3 = tempo;
                    mostExpensiveDescr3 = tempoDescription;
                } else if (tempo < mostExpensive4 && tempo > mostExpensive3) {
                    mostExpensive5 = mostExpensive4;
                    mostExpensiveDescr5 = mostExpensiveDescr4;
                    mostExpensive4 = tempo;
                    mostExpensiveDescr4 = tempoDescription;
                } else if (tempo < mostExpensive5 && tempo > mostExpensive4) {
                    mostExpensive5 = tempo;
                    mostExpensiveDescr5 = tempoDescription;
                }
            }
        }
    }

    public double amountSumExpensesCalculator() {
        double sum = 0;
        for (Transaction transaction : Transaction.expenseTransactions) {
            sum += transaction.getAmount();
        }
        return sum;
    }

    public double amountSumIncomesCalculator() {
        double sum = 0;
        for (Transaction transaction : Transaction.incomeTransactions) {
            sum += transaction.getAmount();
        }
        return sum;
    }

    // Сборка инфы по транзакциям
    public TreeMap<String, Double> getTransactionsInfo() {

        for (Transaction transaction : Transaction.expenseTransactions) {
            if (categories.containsKey(transaction.getDescription())) {
                Double summOfSame = (double) Math.round((categories.get(transaction.getDescription()) + transaction.getAmount()) * 100) / 100;
                categories.replace(transaction.getDescription(), summOfSame);
            } else {
                categories.put(transaction.getDescription().trim(), transaction.getAmount());
            }
        }
        return categories;
    }

    // Диаграмма
    public void generateDiagram() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        categories = getTransactionsInfo();
        header = "Диаграмма расходов за " + transactions.getFirst().getDate().getYear() + " год.";

        for (Map.Entry<String, Double> entry : categories.entrySet()) {
//            System.out.println("Категория расходов: " + entry.getKey() + "\n Сумма: " + entry.getValue());
            if (!entry.getKey().equals("Банковские операции") && !entry.getKey().equals("Перевод на карту") && !entry.getKey().equals("Перевод с карты") && !entry.getKey().equals("Выдача наличных") && entry.getValue() < 0) {
                dataset.setValue(Math.abs(entry.getValue()), "СУММА", entry.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(header, "Категории", "Значения", dataset);

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("Serif", Font.BOLD, 28));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelFont(new Font("Serif", Font.BOLD, 28));
        Random random = new Random();
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            redRandom = random.nextInt(256);
            greenRandom = random.nextInt(100);
            blueRandom = random.nextInt(256);
            Color randomColor = new Color(redRandom, greenRandom, blueRandom);
            renderer.setSeriesPaint(0, randomColor); // Устанавливаем цвет для серии
            renderer.setSeriesItemLabelsVisible(0, true); // Включаем отображение меток значений
        }

        int width = 5000;
        int height = 2000;
        File barChart = new File("C:\\Users\\gilma\\Desktop\\BarChart.jpeg");
        try {
            ChartUtils.saveChartAsJPEG(barChart, chart, width, height);
            System.out.println("Диаграмма сохранена в файл BarChart.jpeg");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении диаграммы: " + e.getMessage());
        }
    }

    public void generateReport() throws IOException, InvalidFormatException {
        generateDiagram();
        TransactorExportManager reportExport = new TransactorExportManager();
        this.collectInfo();
        reportExport.writeToWordFile(startOfPeriod, endOfPeriod, userName, roundedAmountExpense, roundedAmountIncome, mostExpensiveDescr1, mostExpensiveDescr2, mostExpensiveDescr3, mostExpensiveDescr4, mostExpensiveDescr5, mostExpensive1, mostExpensive2, mostExpensive3, mostExpensive4, mostExpensive5, redRandom, greenRandom, blueRandom);
    }


}
