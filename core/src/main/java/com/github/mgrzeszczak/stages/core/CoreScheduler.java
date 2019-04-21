package com.github.mgrzeszczak.stages.core;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.github.mgrzeszczak.stages.domain.Stage;
import com.github.mgrzeszczak.stages.domain.StageStatus;
import com.github.mgrzeszczak.stages.domain.repository.StageRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableScheduling
class CoreScheduler {

    private static final int N_THREADS = 32;
    private static final String CREATED_DATE_FIELD_NAME = "createdDate";

    private final StageRepository stageRepository;
    private final ExecutorService executorService;
    private final StageWorker stageWorker;

    public CoreScheduler(StageRepository stageRepository,
                         StageWorker worker) {
        this.stageRepository = stageRepository;
        this.stageWorker = worker;
        this.executorService = Executors.newFixedThreadPool(N_THREADS);
    }

    @Scheduled(cron = "*/5 * * * * *")
    void schedule() {
        Page<Stage> stages = findStagesToExecute();
        long count = stages.getTotalElements();
        log.info("Found {} stages to execute", count);
        List<CompletableFuture<?>> futures = stages.stream()
                .map(stage -> (Runnable) (() -> stageWorker.execute(stage.getId())))
                .map(runnable -> CompletableFuture.runAsync(runnable, executorService))
                .collect(Collectors.toList());
        futures.forEach(CompletableFuture::join);
        log.info("{} stages executed", count);
    }

    private Page<Stage> findStagesToExecute() {
        return stageRepository.findByStatusInAndScheduledDateBefore(
                Arrays.asList(StageStatus.SCHEDULED, StageStatus.RETRYING),
                Time.now(),
                PageRequest.of(0, N_THREADS, Sort.by(CREATED_DATE_FIELD_NAME).ascending())
        );
    }


}
