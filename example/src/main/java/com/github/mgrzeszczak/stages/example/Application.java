package com.github.mgrzeszczak.stages.example;

import java.time.ZoneOffset;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.github.mgrzeszczak.stages.core.StageScheduler;
import com.github.mgrzeszczak.stages.core.StagesConfig;
import com.github.mgrzeszczak.stages.example.stages.initial.InitialJobStageInputData;
import com.github.mgrzeszczak.stages.example.stages.initial.InitialStageDefinition;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
@Import(StagesConfig.class)
public class Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        SpringApplication.run(Application.class, args);
    }

    private final StageScheduler stageScheduler;

    @PostConstruct
    public void postConstruct() {
        stageScheduler.schedule(InitialStageDefinition.class, new InitialJobStageInputData());
    }

}
