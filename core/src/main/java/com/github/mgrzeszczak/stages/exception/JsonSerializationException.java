package com.github.mgrzeszczak.stages.exception;

public class JsonSerializationException extends RuntimeException {

    private static final String MESSAGE = "Failed to serialize data";

    private JsonSerializationException(Exception cause) {
        super(MESSAGE, cause);
    }

    public static JsonSerializationException create(Exception cause) {
        return new JsonSerializationException(cause);
    }

}
