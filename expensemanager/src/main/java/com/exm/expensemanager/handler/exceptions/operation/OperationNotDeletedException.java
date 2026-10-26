package com.exm.expensemanager.handler.exceptions.operation;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class OperationNotDeletedException extends CustomException {
    public OperationNotDeletedException(String text) {
        super(Objects.equals(text, "ALL")
                        ? "These operations were not deleted!"
                        : "This operation was not deleted!",
                HttpStatus.BAD_REQUEST);
    }
}
