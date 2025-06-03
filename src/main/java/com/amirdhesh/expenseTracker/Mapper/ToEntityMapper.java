package com.amirdhesh.expenseTracker.Mapper;


public interface ToEntityMapper<S, T> {
    T toEntity(S s);
}
