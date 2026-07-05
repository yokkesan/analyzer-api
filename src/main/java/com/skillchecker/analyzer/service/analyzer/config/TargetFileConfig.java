package com.skillchecker.analyzer.service.analyzer.config;

import java.io.File;
import java.util.List;

public final class TargetFileConfig {

        private TargetFileConfig() {
        }

        public static final List<String> EXTENSIONS = List.of(
                        ".java",
                        ".php",
                        ".js",
                        ".ts",
                        ".kt",
                        ".go",
                        ".py");

        public static boolean isTargetFile(
                        File file) {

                String fileName = file.getName();

                return EXTENSIONS
                                .stream()
                                .anyMatch(
                                                fileName::endsWith);
        }
}