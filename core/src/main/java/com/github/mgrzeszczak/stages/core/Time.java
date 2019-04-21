package com.github.mgrzeszczak.stages.core;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Time {

    private static Clock CLOCK = Clock.systemUTC();

    public static void setClock(Clock clock) {
        CLOCK = clock;
    }

    public static Clock getClock() {
        return CLOCK;
    }

    public static ZoneId getZone() {
        return CLOCK.getZone();
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(getClock());
    }

}
