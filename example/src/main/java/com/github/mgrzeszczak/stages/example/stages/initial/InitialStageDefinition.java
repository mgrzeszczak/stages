package com.github.mgrzeszczak.stages.example.stages.initial;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.mgrzeszczak.stages.core.StageDefinition;
import com.github.mgrzeszczak.stages.example.stages.last.LastJobStageInputData;
import com.github.mgrzeszczak.stages.example.stages.last.LastStageDefinition;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Component
@Slf4j
@RequiredArgsConstructor
public class InitialStageDefinition implements StageDefinition<InitialJobStageInputData, LastJobStageInputData> {

    @Override
    public LastJobStageInputData execute(InitialJobStageInputData data) throws Exception {
        return new LastJobStageInputData();
    }

    @Override
    public void onError(InitialJobStageInputData data, Throwable error) {
        log.error("onError");
    }

    @Override
    public Class<InitialJobStageInputData> getInputDataClass() {
        return InitialJobStageInputData.class;
    }

    @Override
    public Optional<Class<? extends StageDefinition<LastJobStageInputData, ?>>> getNextStage() {
        return Optional.of(LastStageDefinition.class);
    }

}
