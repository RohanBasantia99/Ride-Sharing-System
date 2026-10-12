package com.airtribe.ridewise.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IdGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    public String nextId(String prefix) {
        return prefix + "-" + counter.incrementAndGet();
    }
}
