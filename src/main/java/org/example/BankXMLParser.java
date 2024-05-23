package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.example.BankTransaction.validatedConstructor;

public class BankXMLParser implements BankStatementParser {

    static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public List<BankTransaction> parseLinesFrom(List<String> lines) {

        List<BankTransaction> bankTransactions = new ArrayList<>();
        String regex = "\\s*(<\\w+>)(.+(\\s?.+)*)(</\\w+>)";
        Queue<String> fields = new ArrayDeque<>();

        try {

            for (String line : lines) {

                if (!line.matches(" *<\\w+>")) {

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.find()) {
                        String lineExtracted = matcher.group(2);

                        if (lineExtracted.matches("\\d{2}-\\d{2}-\\d{4}")) {
                            fields.add(lineExtracted);

                        } else if (lineExtracted.matches(".?\\d+")) {
                            fields.add(lineExtracted);

                        } else if (lineExtracted.matches("[а-яА-Яa-zA-Z]+(\\s?[а-яА-Яa-zA-Z])*")) {
                            fields.add(lineExtracted);
                        }
                    }
                }
                if (fields.size() == 3) {
                    LocalDate date = LocalDate.parse(fields.poll(), DATE_PATTERN);
                    double amount = Double.parseDouble(fields.poll());
                    String description = fields.poll();
                    bankTransactions.add(validatedConstructor(date, amount, description));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("\nОшибка XML-парсера. Одно из полей содержит null: " + e.getMessage() + "\n");
        } catch (PatternSyntaxException e) {
            System.out.println("\nОшибка XML-парсера. Несоответствие переданной строки паттерну: " + e.getMessage() + "\n");
        } catch (DateTimeParseException e) {
            System.out.println("\nОшибка XML-парсера. Несоответствие даты паттерну: " + e.getMessage() + "\n");
        } catch (NoSuchElementException e) {
            System.out.println("\nОшибка XML-парсера. Не найден нужный элемент: " + e.getMessage() + "\n");
        } catch (NumberFormatException e) {
            System.out.println("\nОшибка XML-парсера. Несоответствие числа паттерну: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\nПроизошла непредвиденная ошибка: " + e.getMessage() + "\n");
        }

        return bankTransactions;
    }



    public List<BankTransaction> collectValidatedTransactions(List<String> lines) {
            final List<BankTransaction> bankTransactionsValid = new ArrayList<>();
        int operationNumber = 0;
        for (BankTransaction bankTransaction : parseLinesFrom(lines)) {
            if(bankTransaction.validated) {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                bankTransactionsValid.add(bankTransaction);
            } else {
                operationNumber++;
                bankTransaction.setOperationNumber(operationNumber);
                Validator.bankTransactionsInvalid.add(bankTransaction);
            }
        }
        return bankTransactionsValid;
    }
}


