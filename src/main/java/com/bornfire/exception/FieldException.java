package com.bornfire.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FieldException extends RuntimeException{
	private static final long serialVersionUID = 1L;
    public FieldException(String message) {
        super(message);
    }
}
