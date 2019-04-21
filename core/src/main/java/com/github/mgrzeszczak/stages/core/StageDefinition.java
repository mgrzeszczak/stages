package com.github.mgrzeszczak.stages.core;

import java.time.Duration;
import java.util.Optional;

import com.github.mgrzeszczak.stages.domain.BackoffType;

public interface StageDefinition<IN, OUT> {

    int DEFAULT_MAX_RETRIES = 3;
    int DEFAULT_TIME_SLOT_MINUTES = 1;

    default Optional<Duration> getTimeout() {
        return Optional.empty();
    }

    OUT execute(IN data) throws Exception;

    void onError(IN data, Throwable error);

    Class<IN> getInputDataClass();

    default BackoffType getBackoffType() {
        return BackoffType.LINEAR;
    }

    default int maxRetries() {
        return DEFAULT_MAX_RETRIES;
    }

    default Duration getTimeSlot() {
        return Duration.ofMinutes(DEFAULT_TIME_SLOT_MINUTES);
    }

    default Optional<Class<? extends StageDefinition<OUT, ?>>> getNextStage() {
        return Optional.empty();
    }

    default boolean isFinal() {
        return !getNextStage().isPresent();
    }

}
