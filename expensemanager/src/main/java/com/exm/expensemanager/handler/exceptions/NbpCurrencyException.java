package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class NbpCurrencyException extends CustomException {
    public NbpCurrencyException()
    {
        super("Could not get currency exchange rate", HttpStatus.NOT_FOUND);
    }
}
