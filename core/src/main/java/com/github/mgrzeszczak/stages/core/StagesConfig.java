package com.github.mgrzeszczak.stages.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(StagesConfig.BASE_PACKAGE)
@EntityScan(StagesConfig.BASE_PACKAGE)
@EnableJpaRepositories(StagesConfig.BASE_PACKAGE)
public class StagesConfig {

    static final String BASE_PACKAGE = "com.github.mgrzeszczak.stages";

}
