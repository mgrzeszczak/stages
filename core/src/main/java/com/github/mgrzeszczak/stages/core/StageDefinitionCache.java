package com.github.mgrzeszczak.stages.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.mgrzeszczak.stages.exception.JobStageNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SuppressWarnings("unchecked")
class StageDefinitionCache {

    private final Map<String, StageDefinition<?, ?>> jobStageMap;

    public StageDefinitionCache(ApplicationContext context) {
        Collection<StageDefinition> stageDefinitions = context.getBeansOfType(StageDefinition.class).values();
        List<? extends StageDefinition<?, ?>> verifiedStages = stageDefinitions.stream()
                .map(j -> (StageDefinition<?, ?>) j)
                .map(StageDefinition::getNextStage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(context::getBean)
                .collect(Collectors.toList());
        this.jobStageMap = stageDefinitions.stream().collect(Collectors.toMap(x -> x.getClass().getName(), v -> v));
    }

    <IN, OUT> StageDefinition<IN, OUT> getStage(String jobStage) {
        return (StageDefinition<IN, OUT>) Optional.ofNullable(jobStageMap.get(jobStage))
                .orElseThrow(() -> JobStageNotFoundException.of(jobStage));
    }
}
