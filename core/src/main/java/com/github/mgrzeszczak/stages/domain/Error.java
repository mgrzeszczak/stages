package com.github.mgrzeszczak.stages.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@RequiredArgsConstructor
public class Error {

    private final String message;
    @Column(columnDefinition = "text")
    private final String stackTrace;

    private Error() {
        this.message = null;
        this.stackTrace = null;
    }

    public static Error of(Throwable t) {
        return new Error(
                t.getMessage(),
                ThrowableUtils.stackTraceToString(t)
        );
    }

}
