package com.mybusinessapp.example.madhu.mybusinessapp2.exception;

/**
 * This class is used in report data validations.
 */
public class ValidationException extends Exception {

    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
