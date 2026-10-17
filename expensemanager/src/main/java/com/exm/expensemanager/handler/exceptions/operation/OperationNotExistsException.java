package com.exm.expensemanager.handler.exceptions.operation;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class OperationNotExistsException extends CustomException {
    public OperationNotExistsException() {
        super("This operation does not exists!", HttpStatus.NOT_FOUND);
    }
}
