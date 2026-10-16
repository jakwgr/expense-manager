package com.exm.expensemanager.handler;

import com.exm.expensemanager.handler.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleCustomException(CustomException ex)
    {
        ApiError er = new ApiError(
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getStatus());

        return ResponseEntity
                .status(ex.getStatus())
                .body(er);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleNotValidException(MethodArgumentNotValidException ex)
    {
        ApiError er = new ApiError(
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(er);
    }
}
