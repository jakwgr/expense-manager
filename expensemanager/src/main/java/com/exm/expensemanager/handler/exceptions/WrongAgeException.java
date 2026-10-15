package com.exm.expensemanager.handler.exceptions;

import com.exm.expensemanager.handler.CustomException;
import jdk.jshell.Snippet;
import org.springframework.http.HttpStatus;

public class WrongAgeException extends CustomException {
    public WrongAgeException() {
        super("Age needs to be higher than zero!", HttpStatus.BAD_REQUEST);
    }
}
