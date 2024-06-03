package org.example;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public interface BankStatementParser {
    public static DateTimeFormatter DATE_PATTERN = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yy"))
            .toFormatter();

    public static DateTimeFormatter SINGLE_DATE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<BankTransaction> parseLinesFrom(List<String> lines);
    List<BankTransaction> collectValidatedTransactions(List<String> lines);

}
