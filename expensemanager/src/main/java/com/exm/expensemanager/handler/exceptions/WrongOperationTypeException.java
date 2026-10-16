package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class WrongOperationTypeException extends CustomException {
    public WrongOperationTypeException() {
        super("Provided operation type is not correct!", HttpStatus.BAD_REQUEST);
    }
}
