package com.ann.test.exception.exceptions;

public class IncorrectDataInFileException extends RuntimeException{
    public IncorrectDataInFileException(String message) {
        super(message);
    }
}
