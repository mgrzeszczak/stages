package com.github.mgrzeszczak.stages.core;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.github.mgrzeszczak.stages.domain.Id;
import com.github.mgrzeszczak.stages.domain.Stage;
import com.github.mgrzeszczak.stages.domain.StageStatus;
import com.github.mgrzeszczak.stages.domain.repository.StageRepository;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
class StageWorker {

    private final StageDefinitionCache stageDefinitionCache;
    private final StageRepository stageRepository;

    void execute(Id jobStageId) {
        Stage stage = stageRepository.findOrThrow(jobStageId);
        StageDefinition<Object, Object> definition = stageDefinitionCache.getStage(stage.getStageDefinitionClassName());

        if (stage.getStatus() == StageStatus.SCHEDULED) {
            stage.start();
            stageRepository.save(stage);
        }
        Object data = stage.getInput().getData(definition.getInputDataClass());
        log.info("Executing stage {} - retry {}", definition, stage.getRetryCount());
        try {
            CompletableFuture<Try<Object>> future = CompletableFuture.supplyAsync(() -> Try.of(() -> definition.execute(data)));
            Object result = null;
            if (definition.getTimeout().isPresent()) {
                Duration timeoutDuration = definition.getTimeout().get();
                result = future.get(timeoutDuration.toMillis(), TimeUnit.MILLISECONDS)
                        .getOrElseThrow(Function.identity());
            } else {
                result = future.get().getOrElseThrow(Function.identity());
            }
            stage.complete(result);
            log.info("Stage {} executed successfully", definition);
            if (definition.isFinal()) {
                log.info("Stage {} is final", definition);
            } else {
                Class<? extends StageDefinition<Object, ?>> nextStageClass = definition.getNextStage().get();
                StageDefinition<Object, Object> nextStageDefinition = stageDefinitionCache.getStage(nextStageClass.getName());
                Stage nextStage = Stage.of(nextStageClass, result, Time.now(), stage);
                log.info("Scheduled next stage {} @ {}", nextStageDefinition, nextStage.getScheduledDate());
                stageRepository.save(nextStage);
            }
        } catch (Throwable t) {
            log.error("Failed to execute stage {}", definition, t);
            if (stage.getRetryCount() >= definition.maxRetries()) {
                log.warn("Max retries exceeded ({}), invoking onError handler", stage.getRetryCount());
                try {
                    definition.onError(data, t);
                } catch (Throwable t2) {
                    log.error("Error during onError method of stage {}", definition, t2);
                }
                stage.fail(t);
                log.info("Stage failed {}", definition);
            } else {
                stage.scheduleRetry(definition.getBackoffType(), definition.getTimeSlot());
                log.info("Scheduled retry {} @ {}", stage.getRetryCount(), stage.getScheduledDate());
            }
        }
        stageRepository.save(stage);
    }
}
