package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotExists extends CustomException {
    public UserNotExists() {
        super("This User Does Not Exists!", HttpStatus.NOT_FOUND);
    }
}
