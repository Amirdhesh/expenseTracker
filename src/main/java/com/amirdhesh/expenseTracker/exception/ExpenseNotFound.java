package com.amirdhesh.expenseTracker.exception;

public class ExpenseNotFound extends RuntimeException {
    public ExpenseNotFound(String message) {
        super(message);
    }
}
