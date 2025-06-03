package com.amirdhesh.expenseTracker.dto;

import com.amirdhesh.expenseTracker.entity.User;
import lombok.Data;

import java.util.UUID;

@Data
public class ExpenseAdd {
    private double expense;
    private UUID userid;
}
