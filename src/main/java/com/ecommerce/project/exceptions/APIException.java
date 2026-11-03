package com.ecommerce.project.exceptions;


// This is for when we create a duplicates
public class APIException extends RuntimeException{
    private static final long serialVersionUID=1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
