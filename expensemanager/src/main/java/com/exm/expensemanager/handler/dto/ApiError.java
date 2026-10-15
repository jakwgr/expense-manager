package com.exm.expensemanager.handler.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        LocalDateTime timeStamp,
        HttpStatus status
) {
}
