package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class WrongOperationIncludeException extends CustomException {
    public WrongOperationIncludeException() {
        super("Provided operation include is not correct! Consider using true/false", HttpStatus.BAD_REQUEST);
    }
}
