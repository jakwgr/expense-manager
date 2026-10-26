package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class ExchangeOperationSumException extends CustomException {
    public ExchangeOperationSumException() {
        super("Could not exchange the money!", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
