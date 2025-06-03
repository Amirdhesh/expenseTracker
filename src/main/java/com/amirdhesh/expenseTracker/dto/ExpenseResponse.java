package com.amirdhesh.expenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpenseResponse {
    private UUID id;
    private double expense;

    private LocalDate date;
    private LocalTime time;
    private UUID userID;
}
