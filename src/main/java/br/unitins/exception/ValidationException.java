package br.unitins.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String field;

    public ValidationException(String message) {
        this(message, null);
    }

    public ValidationException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
