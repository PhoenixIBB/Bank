package org.example;

@FunctionalInterface
public interface TransactionFilter {
    boolean test (Transaction transaction);
}
