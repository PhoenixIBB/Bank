package org.example;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public interface Parsers {
    public static DateTimeFormatter DATE_PATTERN = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yy"))
            .toFormatter();

    public static DateTimeFormatter SINGLE_DATE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<Transaction> parseLinesFrom(List<String> lines);
    List<Transaction> collectValidatedTransactions(List<String> lines);

}
