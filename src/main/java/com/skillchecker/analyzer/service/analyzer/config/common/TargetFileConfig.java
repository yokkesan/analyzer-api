package com.skillchecker.analyzer.service.analyzer.config.common;

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
                        ".jsx",
                        ".tsx",
                        ".kt",
                        ".go",
                        ".py",
                        ".cs");

        public static final List<String> IGNORE_PATHS = List.of(
                        "/vendor/",
                        "/node_modules/",
                        "/dist/",
                        "/build/",
                        "/target/",
                        "/.git/",
                        "/.idea/",
                        "/.vscode/",
                        "/coverage/",
                        "/logs/",
                        "/storage/",
                        "/tmp/",
                        "/temp/");

        public static final List<String> IGNORE_FILES = List.of(
                        "composer.lock",
                        "package-lock.json",
                        "yarn.lock",
                        "pnpm-lock.yaml",
                        "bun.lockb",
                        "welcome.blade.php");

        public static boolean isTargetFile(
                        File file) {

                String path = file.getPath()
                                .replace(
                                                "\\",
                                                "/");

                for (String ignorePath : IGNORE_PATHS) {

                        if (path.contains(
                                        ignorePath)) {

                                return false;
                        }
                }

                String fileName = file.getName();

                if (IGNORE_FILES.contains(
                                fileName)) {

                        return false;
                }

                return EXTENSIONS.stream()
                                .anyMatch(
                                                fileName::endsWith);
        }
}