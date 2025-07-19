package com.tmuungani.todo.dto;

public record ServiceResponse<T>(
        boolean success,
        String message,
        T data
) {
}
