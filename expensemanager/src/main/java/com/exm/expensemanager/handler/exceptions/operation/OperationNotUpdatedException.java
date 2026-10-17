package com.exm.expensemanager.handler.exceptions.operation;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class OperationNotUpdatedException extends CustomException {
    public OperationNotUpdatedException() {
        super("Operation was not updated!", HttpStatus.BAD_REQUEST);
    }
}
