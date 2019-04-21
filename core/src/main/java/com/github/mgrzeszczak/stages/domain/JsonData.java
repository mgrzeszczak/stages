package com.github.mgrzeszczak.stages.domain;

import java.util.Optional;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonData {

    private static final String EMPTY_JSON_OBJ_STRING = "{}";

    @Getter(AccessLevel.NONE)
    private final String value;

    private JsonData() {
        this.value = null;
    }

    public static <T> JsonData of(T value) {
        return Optional.ofNullable(value)
                .map(Json::write)
                .map(JsonData::new)
                .orElseGet(JsonData::empty);
    }

    public static JsonData empty() {
        return new JsonData(
                EMPTY_JSON_OBJ_STRING
        );
    }

    public <T> T getData(Class<T> resultClass) {
        return Json.read(this.value, resultClass);
    }

}
