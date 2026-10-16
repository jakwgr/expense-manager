package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class WrongCurrencyException extends CustomException {
    public WrongCurrencyException() {
        super("Provided currency is not correct!", HttpStatus.BAD_REQUEST);
    }
}
