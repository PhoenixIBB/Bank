package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SecondHTMLParser implements BankStatementParser {

    public static DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<BankTransaction> parseLinesFrom (List<String> lines) {

        List<BankTransaction> bankTransactions = new ArrayList<>();

        String regex = "\\s*(<\\w+>)(.+(\\s\\w+)*)(</\\w+>)";
        int i = 0;
        LocalDate date = null;
        double amount = 0;
        String description = null;

        try {

        for (String line : lines) {

            if(line.matches("\\s*(<\\w+>)(.+(\\s\\w+)*)(</\\w+>)")) {

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String lineFormatted = matcher.group(1);

                    if (lineFormatted.matches("(<td>)(\\d{2}-\\d{2}-\\d{4})(</td>)")) {
                        date = LocalDate.parse(line, DATE_PATTERN);
                        i++;
                    } else if (lineFormatted.matches("(<td>)(.?\\d+)(</td>)")) {
                        amount = Double.parseDouble(lineFormatted);
                        i++;
                    } else if (lineFormatted.matches("(<td>)(\\w+)(\\s\\w+)*(</td>)")) {
                        description = lineFormatted;
                        i++;
                    }
                }
            }
            if (i == 3) {
                BankTransaction bankTransaction = BankTransaction.validatedConstructor(date, amount, description);
                bankTransactions.add(bankTransaction);
                i = 0;
            }
        }
    } catch (NullPointerException e) {
        System.out.println("\nОшибка XML-парсера. Одно из полей содержит null: " + e.getMessage() + "\n");
    } catch (
    PatternSyntaxException e) {
        System.out.println("\nОшибка XML-парсера. Несоответствие переданной строки паттерну: " + e.getMessage() + "\n");
    } catch (
    DateTimeParseException e) {
        System.out.println("\nОшибка XML-парсера. Несоответствие даты паттерну: " + e.getMessage() + "\n");
    } catch (
    NoSuchElementException e) {
        System.out.println("\nОшибка XML-парсера. Не найден нужный элемент: " + e.getMessage() + "\n");
    } catch (NumberFormatException e) {
        System.out.println("\nОшибка XML-парсера. Несоответствие числа паттерну: " + e.getMessage() + "\n");
    } catch (Exception e) {
        System.out.println("\nПроизошла непредвиденная ошибка: " + e.getMessage() + "\n");
    }
        return bankTransactions;
    }


    public List<BankTransaction> collectValidatedTransactions (List<String> lines) {
        List<BankTransaction> bankTransactionsValid = new ArrayList<>();
        int operationNumber = 0;

        for (BankTransaction bankTransaction : parseLinesFrom(lines)) {

            if (bankTransaction.validated) {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                bankTransactionsValid.add(bankTransaction);
            } else {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                bankTransactionsValid.add(bankTransaction);
            }

        }
        return bankTransactionsValid;
    }

}
