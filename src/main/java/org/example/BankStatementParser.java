package org.example;

import java.util.List;

public interface BankStatementParser {
    List<BankTransaction> parseLinesFrom(List<String> lines);
    List<BankTransaction> collectValidatedTransactions(List<String> lines);

}
