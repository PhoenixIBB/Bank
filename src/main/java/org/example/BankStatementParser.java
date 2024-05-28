package org.example;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public interface BankStatementParser {
    public static DateTimeFormatter DATE_PATTERN = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .toFormatter();
    List<BankTransaction> parseLinesFrom(List<String> lines);
    List<BankTransaction> collectValidatedTransactions(List<String> lines);

}
