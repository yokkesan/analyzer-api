package com.skillchecker.analyzer.service.analyzer.config.kotlin;

import java.io.File;
import java.util.List;

import com.skillchecker.analyzer.service.analyzer.config.common.LanguageIgnoreLineConfig;

public class KotlinIgnoreLineConfig
        implements LanguageIgnoreLineConfig {

    private static final List<String> LINES =
            List.of(
                    "{",
                    "}",
                    "(",
                    ")",
                    "},",
                    ");",
                    "return",
                    "return null",
                    "continue",
                    "break");

    @Override
    public boolean supports(
            File file) {

        return file.getName()
                .endsWith(".kt");
    }

    @Override
    public boolean shouldIgnoreLine(
            String line) {

        String trimmedLine =
                line.trim();

        if (trimmedLine.isBlank()) {

            return true;
        }

        if (trimmedLine.startsWith("package ")
                || trimmedLine.startsWith("import ")) {

            return true;
        }

        if (trimmedLine.startsWith("//")
                || trimmedLine.startsWith("/*")
                || trimmedLine.startsWith("*")
                || trimmedLine.startsWith("*/")) {

            return true;
        }

        if (trimmedLine.startsWith("@")) {

            return true;
        }

        if (trimmedLine.equals(
                        "expanded = false")
                || trimmedLine.equals(
                                "expanded = expanded")
                || trimmedLine.equals(
                                "keyboardType = KeyboardType.Decimal")
                || trimmedLine.equals(
                                "DropdownMenu(")
                || trimmedLine.equals(
                                "Text(\"kg\")")) {

            return true;
        }

        if (trimmedLine.startsWith(
                        "modifier = Modifier.")) {

            return true;
        }

        if (trimmedLine.matches(
                        "^[\\[\\]\\{\\}\\(\\),;]+$")) {

            return true;
        }

        return LINES.contains(
                trimmedLine);
    }
}