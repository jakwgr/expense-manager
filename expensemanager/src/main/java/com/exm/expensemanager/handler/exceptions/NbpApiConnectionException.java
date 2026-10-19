package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class NbpApiConnectionException extends CustomException {
    public NbpApiConnectionException()
    {
        super("Error with external NBP API connection!", HttpStatus.BAD_GATEWAY);
    }
}
