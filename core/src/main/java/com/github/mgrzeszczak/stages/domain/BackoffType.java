package com.github.mgrzeszczak.stages.domain;

import java.time.Duration;

public enum BackoffType {

    CONSTANT() {
        @Override
        public Duration getDuration(Duration timeSlot, int retry) {
            return timeSlot;
        }
    },
    LINEAR() {
        @Override
        public Duration getDuration(Duration timeSlot, int retry) {
            return timeSlot.multipliedBy(retry);
        }
    },
    EXPONENTIAL() {
        @Override
        public Duration getDuration(Duration timeSlot, int retry) {
            long power = Double.valueOf(Math.pow(2, retry)).longValue();
            return timeSlot.multipliedBy(power);
        }
    };

    public abstract Duration getDuration(Duration timeSlot, int retry);

}
