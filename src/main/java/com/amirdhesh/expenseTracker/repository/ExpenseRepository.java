package com.amirdhesh.expenseTracker.repository;

import com.amirdhesh.expenseTracker.entity.Expense;
import com.amirdhesh.expenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByUserAndDate(User user, LocalDate date);

    @Query(value = "select sum(e.expense) from Expense e where :userId=e.user_Id and EXTRACT(MONTH FROM e.date)=:month", nativeQuery = true)
    Double totalExpenseOfMonth(@Param("userId") UUID userId, @Param("month") int month);
}
