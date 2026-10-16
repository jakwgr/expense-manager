package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotCreated extends CustomException {
    public UserNotCreated() {
            super("User is not created!", HttpStatus.BAD_REQUEST);
        }
    }
