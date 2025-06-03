package com.amirdhesh.expenseTracker.service;

import com.amirdhesh.expenseTracker.Mapper.ExpenseAddMapper;
import com.amirdhesh.expenseTracker.dto.ExpenseAdd;
import com.amirdhesh.expenseTracker.dto.ExpenseResponse;
import com.amirdhesh.expenseTracker.entity.Expense;
import com.amirdhesh.expenseTracker.entity.User;
import com.amirdhesh.expenseTracker.exception.ExpenseNotFound;
import com.amirdhesh.expenseTracker.exception.UserNotFound;
import com.amirdhesh.expenseTracker.repository.ExpenseRepository;
import com.amirdhesh.expenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ExpenseAddMapper mapper;

    public ExpenseResponse AddExpense(ExpenseAdd expenseDto) {
        UUID userid = expenseDto.getUserid();
        User user = userRepo.findById(userid).orElseThrow(() -> new UserNotFound("User not Found."));
        Expense expense = mapper.toEntity(expenseDto);
        expense.setUser(user);
        Expense savedExpense = expenseRepo.save(expense);
        return new ExpenseResponse(savedExpense.getId(), savedExpense.getExpense(), savedExpense.getDate(), savedExpense.getTime(), savedExpense.getUser().getId());
    }

    public String deleteExpense(UUID id) {
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new ExpenseNotFound(("Expense not found.")));
        expenseRepo.delete(expense);
        return "Success";
    }

    public List<ExpenseResponse> listExpensebyDate(UUID userId, LocalDate date) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not Found."));
        List<Expense> expenses = expenseRepo.findByUserAndDate(user, date);
        return expenses.stream().map(expense -> new ExpenseResponse(expense.getId(), expense.getExpense(), expense.getDate(), expense.getTime(), expense.getUser().getId())).collect(Collectors.toList());
    }

    public Double calculateTotalExpenseOfMonth(UUID userId, int month) {
        Double totalExpense = expenseRepo.totalExpenseOfMonth(userId, month);
        return totalExpense;
    }

}
