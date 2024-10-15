package org.example;

@FunctionalInterface
public interface TransactorTransactionFilter {
    boolean test (Transaction transaction);
}
