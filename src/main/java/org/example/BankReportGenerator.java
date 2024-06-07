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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class BankReportGenerator {

    // Основная информация
    public static String userName = "";
    public LocalDate currentDate = LocalDate.now();
    public TreeMap<String, Double> categories = new TreeMap<>();
    List<BankTransaction> bankTransactions;
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

    public BankReportGenerator(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public LocalDate getStartOfPeriod(List<BankTransaction> bankTransactions) {
        LocalDate startOfPeriod = LocalDate.MAX;
        for (BankTransaction bankTransaction : bankTransactions) {
            LocalDate tempoDate = bankTransaction.getDate();
            if (tempoDate.isBefore(startOfPeriod)) {
                startOfPeriod = tempoDate;
            }
        }
        return startOfPeriod;
    }

    public LocalDate getEndOfPeriod(List<BankTransaction> bankTransactions) {
        LocalDate endOfPeriod = LocalDate.MIN;
        for (BankTransaction bankTransaction : bankTransactions) {
            LocalDate tempoDate = bankTransaction.getDate();
            if (tempoDate.isAfter(endOfPeriod)) {
                endOfPeriod = tempoDate;
            }
        }
        return endOfPeriod;
    }

    // Общие данные по транзакциям:
    public void collectInfo() {
        startOfPeriod = getStartOfPeriod(bankTransactions);
        endOfPeriod = getEndOfPeriod(bankTransactions);
//        System.out.println(userName);
        // Общее количество транзакций
//        System.out.println(BankTransaction.detected);
        // Общая сумма расходов
        roundedAmountExpense = (double) Math.round(amountSumExpensesCalculator() * 100) / 100;
        // Общая сумма доходов
        roundedAmountIncome = (double) Math.round(amountSumIncomesCalculator() * 100) / 100;
        // Топ-5 самых дорогих категорий транзакций:
        for (Map.Entry<String, Double> entry : categories.entrySet()) {

            double tempo = entry.getValue();
            String tempoDescription = entry.getKey();

            if (tempo < mostExpensive1) {
                mostExpensive1 = tempo;
                mostExpensiveDescr1 = tempoDescription;
            } else if (tempo < mostExpensive2 && tempo > mostExpensive1) {
                mostExpensive2 = tempo;
                mostExpensiveDescr2 = tempoDescription;
            } else if (tempo < mostExpensive3 && tempo > mostExpensive2) {
                mostExpensive3 = tempo;
                mostExpensiveDescr3 = tempoDescription;
            } else if (tempo < mostExpensive4 && tempo > mostExpensive3) {
                mostExpensive4 = tempo;
                mostExpensiveDescr4 = tempoDescription;
            } else if (tempo < mostExpensive5 && tempo > mostExpensive4) {
                mostExpensive5 = tempo;
                mostExpensiveDescr5 = tempoDescription;
            }
        }
//        System.out.println("Топ-3 самых дорогих транзакций: \n" +
//                "1. " + mostExpensiveDescr1 + ". Её суммарная стоимость составила: " + mostExpensive1 + "\n" +
//                "2. " + mostExpensiveDescr2 + ". Её суммарная стоимость составила: " + mostExpensive2 + "\n" +
//                "3. " + mostExpensiveDescr3 + ". Её суммарная стоимость составила: " + mostExpensive3 + "\n" +
//                "4. " + mostExpensiveDescr4 + ". Её суммарная стоимость составила: " + mostExpensive4 + "\n" +
//                "5. " + mostExpensiveDescr5 + ". Её суммарная стоимость составила: " + mostExpensive5 + "\n");
    }

    public double amountSumExpensesCalculator () {
        double sum = 0;
        for (BankTransaction bankTransaction : BankTransaction.expenseTransactions) {
            sum += bankTransaction.getAmount();
        }
        return sum;
    }

    public double amountSumIncomesCalculator () {
        double sum = 0;
        for (BankTransaction bankTransaction : BankTransaction.incomeTransactions) {
            sum += bankTransaction.getAmount();
        }
        return sum;
    }

    // Сборка инфы по транзакциям
    public TreeMap<String, Double> getTransactionsInfo() {

        for (BankTransaction bankTransaction : BankTransaction.expenseTransactions) {
            if (categories.containsKey(bankTransaction.getDescription())) {
                Double summOfSame = (double) Math.round((categories.get(bankTransaction.getDescription()) + bankTransaction.getAmount()) * 100) / 100;
                categories.replace(bankTransaction.getDescription(), summOfSame);
            } else {
                categories.put(bankTransaction.getDescription().trim(), bankTransaction.getAmount());
            }
        }
        return categories;
    }

    // Диаграмма
    public void generateDiagram() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        categories = getTransactionsInfo();
        header = "Диаграмма расходов за " + bankTransactions.getFirst().getDate().getYear() + " год.";

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
        ExportManager reportExport = new ExportManager();
        this.collectInfo();
        reportExport.writeToWordFile (startOfPeriod, endOfPeriod, userName, roundedAmountExpense, roundedAmountIncome, mostExpensiveDescr1, mostExpensiveDescr2, mostExpensiveDescr3, mostExpensiveDescr4, mostExpensiveDescr5, mostExpensive1, mostExpensive2, mostExpensive3, mostExpensive4, mostExpensive5, redRandom, greenRandom, blueRandom);
    }


}
