package com.example.carpooling.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ContextUtil {
    private final String context;

    public ContextUtil() throws IOException {
        this.context = new String(Files.readAllBytes(Paths.get("src/main/resources/ai-context.txt")), StandardCharsets.UTF_8);
    }
    public String getContext() {
        return context;
    }
}

