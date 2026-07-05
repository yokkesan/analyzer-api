package com.skillchecker.analyzer.service.analyzer.config;

import java.util.List;

public final class IgnoreLineConfig {

    private IgnoreLineConfig() {
    }

    public static final List<String> LINES =
            List.of(
                    "{",
                    "}",
                    "(",
                    ")",
                    "},",
                    "};",
                    "),",
                    ") {",
                    ");",
                    "return 0;",
                    "return null;",
                    "return;",
                    "continue;",
                    "break;");

    public static boolean shouldIgnoreLine(
            String line) {

        if (line.isBlank()) {

            return true;
        }

        if (line.startsWith("import ")
                || line.startsWith("package ")) {

            return true;
        }

        return LINES.contains(
                line);
    }
}