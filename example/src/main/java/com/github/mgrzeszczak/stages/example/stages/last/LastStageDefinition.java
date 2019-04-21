package com.github.mgrzeszczak.stages.example.stages.last;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.mgrzeszczak.stages.core.StageDefinition;
import com.github.mgrzeszczak.stages.domain.BackoffType;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Component
@RequiredArgsConstructor
@Slf4j
public class LastStageDefinition implements StageDefinition<LastJobStageInputData, Void> {

    @Override
    public Optional<Duration> getTimeout() {
        return Optional.of(Duration.ofSeconds(1));
    }

    @Override
    public BackoffType getBackoffType() {
        return BackoffType.CONSTANT;
    }

    @Override
    public Void execute(LastJobStageInputData data) throws Exception {
        Thread.sleep(Duration.ofMinutes(1).toMillis());
        return null;
    }

    @Override
    public void onError(LastJobStageInputData data, Throwable error) {
        log.error("onError");
    }

    @Override
    public Class<LastJobStageInputData> getInputDataClass() {
        return LastJobStageInputData.class;
    }

    @Override
    public Duration getTimeSlot() {
        return Duration.ofSeconds(5);
    }
}
