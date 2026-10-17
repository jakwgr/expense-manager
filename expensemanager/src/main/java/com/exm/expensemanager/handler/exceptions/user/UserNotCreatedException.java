package com.exm.expensemanager.handler.exceptions.user;

import com.exm.expensemanager.handler.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotCreatedException extends CustomException {
    public UserNotCreatedException() {
            super("User was not created!", HttpStatus.BAD_REQUEST);
        }
    }
