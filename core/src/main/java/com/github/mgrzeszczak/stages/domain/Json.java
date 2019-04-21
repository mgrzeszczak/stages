package com.github.mgrzeszczak.stages.domain;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mgrzeszczak.stages.exception.JsonDeserializationException;
import com.github.mgrzeszczak.stages.exception.JsonSerializationException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(FAIL_ON_EMPTY_BEANS, false);

    public static <T> String write(T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw JsonSerializationException.create(e);
        }
    }

    public static <T> T read(String json, Class<T> resultClass) {
        try {
            return MAPPER.readValue(json, resultClass);
        } catch (IOException e) {
            throw JsonDeserializationException.of(resultClass, e);
        }
    }

}
