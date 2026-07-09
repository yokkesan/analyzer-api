package com.skillchecker.analyzer.service.analyzer.config.php;

import java.io.File;
import java.util.List;

import com.skillchecker.analyzer.service.analyzer.config.common.LanguageIgnoreLineConfig;

public class PhpIgnoreLineConfig
        implements LanguageIgnoreLineConfig {

    private static final List<String> LINES =
            List.of(
                    "<?php",
                    "{",
                    "}",
                    "(",
                    ")",
                    "},",
                    "];",
                    ");",
                    "return;",
                    "return null;",
                    "return [];",
                    "continue;",
                    "break;",
                    "return [");

    @Override
    public boolean supports(
            File file) {

        String fileName =
                file.getName();

        return fileName.endsWith(
                ".php")
                || fileName.endsWith(
                                ".blade.php");
    }

    @Override
    public boolean shouldIgnoreLine(
            String line) {

        String trimmedLine =
                line.trim();

        if (trimmedLine.isBlank()) {

            return true;
        }

        if (trimmedLine.startsWith("<")
                || trimmedLine.endsWith(">")
                || trimmedLine.contains("<svg")
                || trimmedLine.contains("</svg")
                || trimmedLine.contains("<path")
                || trimmedLine.contains("</path")
                || trimmedLine.contains("<div")
                || trimmedLine.contains("</div")
                || trimmedLine.contains("<span")
                || trimmedLine.contains("</span")
                || trimmedLine.contains("<a ")
                || trimmedLine.contains("</a")
                || trimmedLine.contains("<img")
                || trimmedLine.contains("<button")
                || trimmedLine.contains("</button")) {

            return true;
        }

        if (trimmedLine.startsWith("namespace ")
                || trimmedLine.startsWith("use ")
                || trimmedLine.startsWith("#[")
                || trimmedLine.startsWith("@")) {

            return true;
        }

        if (trimmedLine.startsWith("//")
                || trimmedLine.startsWith("#")
                || trimmedLine.startsWith("/*")
                || trimmedLine.startsWith("*")
                || trimmedLine.startsWith("*/")) {

            return true;
        }

        if (trimmedLine.startsWith("'")
                && trimmedLine.contains(
                                "=>")) {

            return true;
        }

        if (trimmedLine.contains(
                        "=> env(")) {

            return true;
        }

        if (trimmedLine.matches(
                        "'.*'\\s*=>.*")) {

            return true;
        }

        return LINES.contains(
                trimmedLine);
    }
}