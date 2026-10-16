package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotExistsException extends CustomException {
    public UserNotExistsException() {
        super("This user does not exists!", HttpStatus.NOT_FOUND);
    }
}
