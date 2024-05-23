package org.example;

@FunctionalInterface
public interface BankTransactionFilter {
    boolean test (BankTransaction bankTransaction);
}
