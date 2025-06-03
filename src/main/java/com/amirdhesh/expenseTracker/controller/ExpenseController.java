package com.amirdhesh.expenseTracker.controller;

import com.amirdhesh.expenseTracker.dto.ExpenseAdd;
import com.amirdhesh.expenseTracker.dto.ExpenseResponse;
import com.amirdhesh.expenseTracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/Health")
    public String check() {
        return "OK";
    }

    @PostMapping("/addExpense")
    public ResponseEntity<ExpenseResponse> addExpense(@RequestBody ExpenseAdd expense) {
        return new ResponseEntity<>(expenseService.AddExpense(expense), HttpStatus.CREATED);
    }

    @GetMapping("/ListofExpensebyDate")
    public ResponseEntity<List<ExpenseResponse>> ListofExpensebyDate(@RequestParam UUID userId, LocalDate date) {
        return new ResponseEntity<>(expenseService.listExpensebyDate(userId, date), HttpStatus.OK);
    }

    @DeleteMapping("/DeleteExpense/{expenseId}")
    public ResponseEntity<String> DeleteExpense(@PathVariable UUID expenseId) {
        return new ResponseEntity<>(expenseService.deleteExpense(expenseId), HttpStatus.OK);
    }

    @GetMapping("/TotalExpenseofMonth")
    public ResponseEntity<Double> TotalExpenseofMonth(@RequestParam UUID userId, int month) {
        return new ResponseEntity<>(expenseService.calculateTotalExpenseOfMonth(userId, month), HttpStatus.OK);
    }
}
