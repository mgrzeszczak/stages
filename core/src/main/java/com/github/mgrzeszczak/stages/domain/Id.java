package com.github.mgrzeszczak.stages.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import lombok.Value;

@Embeddable
@Value
public class Id implements Serializable {

    private final String id;

    private Id() {
        this.id = null;
    }

    private Id(String id) {
        this.id = id;
    }

    public static Id create() {
        return new Id(UUID.randomUUID().toString());
    }

}
