package com.amirdhesh.expenseTracker.Mapper;

import com.amirdhesh.expenseTracker.dto.ExpenseAdd;
import com.amirdhesh.expenseTracker.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseAddMapper implements ToEntityMapper<ExpenseAdd, Expense> {
    @Override
    public Expense toEntity(ExpenseAdd expenseadd) {
        Expense expense = new Expense();
        expense.setExpense(expenseadd.getExpense());
        return expense;
    }
}
