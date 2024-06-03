package org.example;

public class HtmlExporter implements BankStatementExporter{
    @Override
    public String export (SummaryStatistics summaryStatistics) {
        String result = "<!doctype html>";
        result += "<html lang = 'en'>";
        result += "<head><title>Bank Transaction Report</title></head>";
        result += "<body>";
        result += "<ul>";
        result += "<li><strong>Сумма всех Ваших транзакций из предоставленных данных:</strong>: " + summaryStatistics.getSum() + "</li>";
        result += "<li><strong>Наиболее затратная статья расходов за каждый месяц:</strong>: " + summaryStatistics.getMax() + "</li>";
        result += "<li><strong>Наименее затратная статья расходов за каждый месяц:</strong>: " + summaryStatistics.getMin() + "</li>";
        result += "</ul>";
        result += "</body>";
        result += "</html>";
        return result;
    }
}
