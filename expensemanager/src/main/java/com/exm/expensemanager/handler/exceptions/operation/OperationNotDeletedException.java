package com.exm.expensemanager.handler.exceptions.operation;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class OperationNotDeletedException extends CustomException {
    public OperationNotDeletedException(String text) {
        String exText;
        if(Objects.equals(text, "ALL"))
        {
            exText = "These operations were not deleted!";
        }
        exText = "This operation was not deleted!";
        super(exText, HttpStatus.BAD_REQUEST);
    }
}
