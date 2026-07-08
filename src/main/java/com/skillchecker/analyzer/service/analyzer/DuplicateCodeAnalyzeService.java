package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.config.DuplicateCodeLine;
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

                        List<DuplicateCodeLine> allLines = collectTargetLines(
                                        repositoryDirectory);

                        int blockDuplicateCount = countDuplicateBlocks(
                                        allLines);

                        int lineDuplicateCount = countHeavyDuplicateLines(
                                        allLines);

                        return calculateScore(
                                        blockDuplicateCount,
                                        lineDuplicateCount);

                } catch (Exception e) {

                        e.printStackTrace();

                        return 0;
                }
        }

        public List<AnalysisIssue> getIssues(
                        File repositoryDirectory) {

                try {

                        List<DuplicateCodeLine> allLines = collectTargetLines(
                                        repositoryDirectory);

                        Map<String, List<DuplicateCodeLine>> lineMap = new HashMap<>();

                        for (DuplicateCodeLine line : allLines) {

                                lineMap.computeIfAbsent(
                                                line.getContent(),
                                                key -> new ArrayList<>())
                                                .add(line);
                        }

                        List<AnalysisIssue> issues = new ArrayList<>();

                        for (List<DuplicateCodeLine> duplicateLines : lineMap.values()) {

                                if (duplicateLines.size() < LINE_DUPLICATE_THRESHOLD) {

                                        continue;
                                }

                                DuplicateCodeLine first = duplicateLines.get(0);

                                issues.add(
                                                new AnalysisIssue(
                                                                first.getFile(),
                                                                first.getLineNumber(),
                                                                first.getLineNumber(),
                                                                first.getContent(),
                                                                "重複コードが "
                                                                                + duplicateLines.size()
                                                                                + " 回出現しています"));
                        }

                        return issues;

                } catch (Exception e) {

                        e.printStackTrace();

                        return List.of();
                }
        }

        private List<DuplicateCodeLine> collectTargetLines(
                        File directory)
                        throws Exception {

                List<DuplicateCodeLine> allLines = new ArrayList<>();

                File[] files = directory.listFiles();

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

                        List<String> lines = Files.readAllLines(
                                        file.toPath());

                        for (int i = 0; i < lines.size(); i++) {

                                String normalizedLine = normalize(
                                                lines.get(i));

                                if (IgnoreLineConfig.shouldIgnoreLine(
                                                normalizedLine)) {

                                        continue;
                                }

                                allLines.add(
                                                new DuplicateCodeLine(
                                                                file.getPath(),
                                                                i + 1,
                                                                normalizedLine));
                        }
                }

                return allLines;
        }

        private int countDuplicateBlocks(
                        List<DuplicateCodeLine> lines) {

                Map<String, Integer> blockCounts = new HashMap<>();

                for (int i = 0; i <= lines.size() - BLOCK_SIZE; i++) {

                        StringBuilder block = new StringBuilder();

                        for (int j = 0; j < BLOCK_SIZE; j++) {

                                block.append(
                                                lines.get(i + j)
                                                                .getContent())
                                                .append("\n");
                        }

                        String blockText = block.toString();

                        blockCounts.put(
                                        blockText,
                                        blockCounts.getOrDefault(
                                                        blockText,
                                                        0) + 1);
                }

                int duplicateCount = 0;

                for (int count : blockCounts.values()) {

                        if (count > 1) {

                                duplicateCount++;
                        }
                }

                return duplicateCount;
        }

        private int countHeavyDuplicateLines(
                        List<DuplicateCodeLine> lines) {

                Map<String, Integer> lineCounts = new HashMap<>();

                for (DuplicateCodeLine line : lines) {

                        lineCounts.put(
                                        line.getContent(),
                                        lineCounts.getOrDefault(
                                                        line.getContent(),
                                                        0) + 1);
                }

                int duplicateCount = 0;

                for (int count : lineCounts.values()) {

                        if (count >= LINE_DUPLICATE_THRESHOLD) {

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

                int duplicatePoint = blockDuplicateCount * 2
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