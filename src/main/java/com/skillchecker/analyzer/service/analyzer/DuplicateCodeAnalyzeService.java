package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class DuplicateCodeAnalyzeService {

    private static final int MAX_SCORE = 10;

    public int analyze(
            File repositoryDirectory) {

        try {

            int duplicateCount =
                    scanDirectory(
                            repositoryDirectory);

            return calculateScore(
                    duplicateCount);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    private int scanDirectory(
            File directory)
            throws Exception {

        int duplicateCount = 0;

        File[] files =
                directory.listFiles();

        if (files == null) {

            return 0;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                duplicateCount +=
                        scanDirectory(
                                file);

                continue;
            }

            if (!isTargetFile(file)) {

                continue;
            }

            duplicateCount +=
                    countDuplicateLines(
                            file);
        }

        return duplicateCount;
    }

    private int countDuplicateLines(
            File file)
            throws Exception {

        int duplicateCount = 0;

        Set<String> linesSeen =
                new HashSet<>();

        List<String> lines =
                Files.readAllLines(
                        file.toPath());

        for (String line : lines) {

            String normalizedLine =
                    normalize(line);

            if (normalizedLine.isBlank()) {

                continue;
            }

            if (!linesSeen.add(
                    normalizedLine)) {

                duplicateCount++;
            }
        }

        return duplicateCount;
    }

    private boolean isTargetFile(
            File file) {

        String fileName =
                file.getName();

        return fileName.endsWith(".java")
                || fileName.endsWith(".php")
                || fileName.endsWith(".js")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".kt")
                || fileName.endsWith(".go")
                || fileName.endsWith(".py");
    }

    private String normalize(
            String content) {

        return content
                .replaceAll("//.*", "")
                .replaceAll("#.*", "")
                .trim();
    }

    private int calculateScore(
            int duplicateCount) {

        if (duplicateCount == 0) {

            return MAX_SCORE;
        }

        if (duplicateCount <= 2) {

            return 8;
        }

        if (duplicateCount <= 5) {

            return 5;
        }

        if (duplicateCount <= 10) {

            return 2;
        }

        return 0;
    }
}