package com.amirdhesh.expenseTracker.Mapper;

public interface ToDTOMapper<S, T> {
    S toDTO(T s);
}
