package com.wex.store.application.exception;

import java.util.List;

public class NotFoundException extends RuntimeException {

    private final List<String> errors;

    public NotFoundException(String message) {
        errors = List.of(message);
    }

    public NotFoundException(List<String> messages) {
        errors = messages;
    }

    public List<String> getErrors() {
        return errors;
    }
}