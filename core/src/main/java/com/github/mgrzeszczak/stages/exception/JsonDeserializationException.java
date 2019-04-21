package com.github.mgrzeszczak.stages.exception;

public class JsonDeserializationException extends RuntimeException {

    private static final String TEMPLATE = "Failed to deserialize data as %s";

    private JsonDeserializationException(Class<?> resultClass, Exception cause) {
        super(String.format(TEMPLATE, resultClass.getName()), cause);
    }

    public static <T> JsonDeserializationException of(Class<T> resultClass, Exception cause) {
        return new JsonDeserializationException(resultClass, cause);
    }
}
