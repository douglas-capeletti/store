package com.wex.store.application.exception;

import java.util.List;

public class BusinessException extends RuntimeException {

    private final List<String> errors;

    public BusinessException(String message) {
        errors = List.of(message);
    }

    public BusinessException(List<String> messages) {
        errors = messages;
    }

    public List<String> getErrors() {
        return errors;
    }
}
