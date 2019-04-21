package com.github.mgrzeszczak.stages.domain;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.github.mgrzeszczak.stages.core.StageDefinition;
import com.github.mgrzeszczak.stages.core.Time;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(of = {}, callSuper = true)
@EqualsAndHashCode(of = {}, callSuper = true)
public class Stage extends BaseEntity<Stage> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "input_value"))
    })
    private JsonData input;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "output_value"))
    })
    private JsonData output;
    private Error error;
    private String stageDefinitionClassName;
    private int retryCount;
    private StageStatus status;
    private ZonedDateTime scheduledDate;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "predecessor_id"))
    })
    private Id predecessorId;

    private Stage() {

    }

    private Stage(String stageDefinitionClassName,
                  Object input,
                  ZonedDateTime scheduledDate,
                  Stage predecessor) {
        this.input = JsonData.of(input);
        this.output = null;
        this.stageDefinitionClassName = stageDefinitionClassName;
        this.scheduledDate = scheduledDate;
        this.status = StageStatus.SCHEDULED;
        this.retryCount = 0;
        this.startDate = this.endDate = null;
        this.predecessorId = Optional.ofNullable(predecessor)
                .map(Stage::getId)
                .orElse(null);
    }

    public static <D, T extends StageDefinition<D, ?>> Stage of(Class<T> jobStageClass,
                                                                D input) {
        return Stage.of(jobStageClass, input, Time.now());
    }

    public static <D, T extends StageDefinition<D, ?>> Stage of(Class<T> jobStageClass,
                                                                D input,
                                                                ZonedDateTime scheduledDate) {
        return Stage.of(jobStageClass, input, scheduledDate, null);
    }

    public static <D, T extends StageDefinition<D, ?>> Stage of(Class<T> jobStageClass,
                                                                D input,
                                                                ZonedDateTime scheduledDate,
                                                                Stage predecessor) {
        Objects.requireNonNull(jobStageClass);
        Objects.requireNonNull(input);
        Objects.requireNonNull(scheduledDate);
        return new Stage(jobStageClass.getName(), input, scheduledDate, predecessor);
    }

    public void start() {
        this.status = StageStatus.IN_PROGRESS;
        this.retryCount = 0;
        this.startDate = Time.now();
    }

    public <T> void complete(T result) {
        this.status = StageStatus.COMPLETED;
        this.endDate = Time.now();
        this.output = JsonData.of(result);
    }

    public void fail(Throwable error) {
        this.status = StageStatus.FAILED;
        this.endDate = Time.now();
        this.error = Error.of(error);
    }

    public void scheduleRetry(BackoffType backoffType, Duration timeSlot) {
        this.retryCount += 1;
        Duration duration = backoffType.getDuration(timeSlot, this.retryCount);
        this.status = StageStatus.RETRYING;
        this.scheduledDate = Time.now().plus(duration);
    }

}
