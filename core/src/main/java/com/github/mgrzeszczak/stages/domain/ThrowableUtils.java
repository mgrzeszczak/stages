package com.github.mgrzeszczak.stages.domain;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThrowableUtils {

    public static String stackTraceToString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }


}
