package com.exm.expensemanager.handler.exceptions.user;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotUpdatedException extends CustomException {
    public UserNotUpdatedException() {
        super("User was not updated!", HttpStatus.BAD_REQUEST);
    }
}
