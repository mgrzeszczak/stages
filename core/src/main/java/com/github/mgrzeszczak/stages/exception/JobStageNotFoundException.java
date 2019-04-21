package com.github.mgrzeszczak.stages.exception;

public class JobStageNotFoundException extends RuntimeException {

    private final static String MESSAGE = "StageDefinition '%s' is not registered as a Spring component";

    private JobStageNotFoundException(String name) {
        super(String.format(MESSAGE, name));
    }

    public static JobStageNotFoundException of(String name) {
        return new JobStageNotFoundException(name);
    }

}
