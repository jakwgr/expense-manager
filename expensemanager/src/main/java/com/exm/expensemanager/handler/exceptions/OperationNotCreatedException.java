package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class OperationNotCreatedException extends CustomException {
    public OperationNotCreatedException() {
        super("Operation was not created!", HttpStatus.BAD_REQUEST);
    }
}
