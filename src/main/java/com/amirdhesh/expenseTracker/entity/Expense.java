package com.amirdhesh.expenseTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "\"expense\"")
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double expense;

    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @PrePersist
    private void create() {
        date = LocalDate.now();
        time = LocalTime.now();
    }

}
