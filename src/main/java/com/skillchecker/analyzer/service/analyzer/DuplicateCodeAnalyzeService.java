package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.service.analyzer.config.IgnoreLineConfig;
import com.skillchecker.analyzer.service.analyzer.config.TargetFileConfig;

@Service
public class DuplicateCodeAnalyzeService {

    private static final int MAX_SCORE = 10;
    private static final int BLOCK_SIZE = 10;
    private static final int LINE_DUPLICATE_THRESHOLD = 10;

    public int analyze(
            File repositoryDirectory) {

        try {

            List<String> allLines =
                    collectTargetLines(
                            repositoryDirectory);

            int blockDuplicateCount =
                    countDuplicateBlocks(
                            allLines);

            int lineDuplicateCount =
                    countHeavyDuplicateLines(
                            allLines);

            return calculateScore(
                    blockDuplicateCount,
                    lineDuplicateCount);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    private List<String> collectTargetLines(
            File directory)
            throws Exception {

        List<String> allLines =
                new ArrayList<>();

        File[] files =
                directory.listFiles();

        if (files == null) {
            return allLines;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                allLines.addAll(
                        collectTargetLines(
                                file));

                continue;
            }

            if (!TargetFileConfig.isTargetFile(
                    file)) {

                continue;
            }

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            for (String line : lines) {

                String normalizedLine =
                        normalize(
                                line);

                if (IgnoreLineConfig.shouldIgnoreLine(
                        normalizedLine)) {

                    continue;
                }

                allLines.add(
                        normalizedLine);
            }
        }

        return allLines;
    }

    private int countDuplicateBlocks(
            List<String> lines) {

        Map<String, Integer> blockCounts =
                new HashMap<>();

        for (int i = 0;
                i <= lines.size() - BLOCK_SIZE;
                i++) {

            StringBuilder block =
                    new StringBuilder();

            for (int j = 0;
                    j < BLOCK_SIZE;
                    j++) {

                block.append(
                        lines.get(i + j))
                        .append("\n");
            }

            String blockText =
                    block.toString();

            blockCounts.put(
                    blockText,
                    blockCounts.getOrDefault(
                            blockText,
                            0) + 1);
        }

        int duplicateCount = 0;

        for (int count
                : blockCounts.values()) {

            if (count > 1) {

                duplicateCount++;
            }
        }

        return duplicateCount;
    }

    private int countHeavyDuplicateLines(
            List<String> lines) {

        Map<String, Integer> lineCounts =
                new HashMap<>();

        for (String line : lines) {

            lineCounts.put(
                    line,
                    lineCounts.getOrDefault(
                            line,
                            0) + 1);
        }

        int duplicateCount = 0;

        for (int count
                : lineCounts.values()) {

            if (count >=
                    LINE_DUPLICATE_THRESHOLD) {

                duplicateCount++;
            }
        }

        return duplicateCount;
    }

    private String normalize(
            String content) {

        return content
                .replaceAll("//.*", "")
                .replaceAll("#.*", "")
                .trim();
    }

    private int calculateScore(
            int blockDuplicateCount,
            int lineDuplicateCount) {

        int duplicatePoint =
                blockDuplicateCount * 2
                        + lineDuplicateCount;

        if (duplicatePoint == 0) {
            return MAX_SCORE;
        }

        if (duplicatePoint <= 5) {
            return 8;
        }

        if (duplicatePoint <= 15) {
            return 5;
        }

        if (duplicatePoint <= 30) {
            return 2;
        }

        return 0;
    }
}