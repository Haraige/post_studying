package ua.org.code.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String fieldName;

    public ValidationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}
