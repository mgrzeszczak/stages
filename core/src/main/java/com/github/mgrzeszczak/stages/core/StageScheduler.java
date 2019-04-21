package com.github.mgrzeszczak.stages.core;

import java.time.ZonedDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.github.mgrzeszczak.stages.domain.Stage;
import com.github.mgrzeszczak.stages.domain.repository.StageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StageScheduler {

    private final StageRepository stageRepository;

    public <IN, OUT> void schedule(Class<? extends StageDefinition<IN, OUT>> stageClass, IN input, ZonedDateTime scheduledDate) {
        Stage newStage = Stage.of(stageClass, input, scheduledDate);
        stageRepository.save(newStage);
    }

    public <IN, OUT> void schedule(Class<? extends StageDefinition<IN, OUT>> stageClass, IN input) {
        schedule(stageClass, input, Time.now());
    }

}
