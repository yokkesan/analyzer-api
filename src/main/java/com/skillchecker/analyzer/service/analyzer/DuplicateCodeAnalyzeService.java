package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.config.common.DuplicateCodeLine;
import com.skillchecker.analyzer.service.analyzer.config.common.IgnoreLineConfigFactory;
import com.skillchecker.analyzer.service.analyzer.config.common.LanguageIgnoreLineConfig;
import com.skillchecker.analyzer.service.analyzer.config.common.TargetFileConfig;

@Service
public class DuplicateCodeAnalyzeService {

        private static final int MAX_SCORE = 10;

        private static final int BLOCK_SIZE = 5;

        public int analyze(
                        File repositoryDirectory) {

                try {

                        List<List<DuplicateCodeLine>> fileLineGroups = collectTargetLineGroups(
                                        repositoryDirectory);

                        int blockDuplicateCount = countDuplicateBlocks(
                                        fileLineGroups);

                        return calculateScore(
                                        blockDuplicateCount);

                } catch (Exception e) {

                        e.printStackTrace();

                        return 0;
                }
        }

        public List<AnalysisIssue> getIssues(
                        File repositoryDirectory) {

                try {

                        List<List<DuplicateCodeLine>> fileLineGroups = collectTargetLineGroups(
                                        repositoryDirectory);

                        Map<String, List<DuplicateCodeLine>> blockMap = createBlockMap(
                                        fileLineGroups);

                        List<AnalysisIssue> issues = new ArrayList<>();

                        for (Map.Entry<String, List<DuplicateCodeLine>> entry : blockMap
                                        .entrySet()) {

                                List<DuplicateCodeLine> duplicates = entry.getValue();

                                if (duplicates.size() < 2) {

                                        continue;
                                }

                                DuplicateCodeLine first = duplicates.get(
                                                0);

                                issues.add(
                                                new AnalysisIssue(
                                                                first.getFile(),
                                                                first.getLineNumber(),
                                                                first.getLineNumber()
                                                                                + BLOCK_SIZE
                                                                                - 1,
                                                                entry.getKey(),
                                                                "重複コードの可能性があります（"
                                                                                + duplicates.size()
                                                                                + "箇所）"));
                        }

                        return issues;

                } catch (Exception e) {

                        e.printStackTrace();

                        return List.of();
                }
        }

        private List<List<DuplicateCodeLine>> collectTargetLineGroups(
                        File directory)
                        throws Exception {

                List<List<DuplicateCodeLine>> fileLineGroups = new ArrayList<>();

                File[] files = directory.listFiles();

                if (files == null) {

                        return fileLineGroups;
                }

                for (File file : files) {

                        if (file.isDirectory()) {

                                fileLineGroups.addAll(
                                                collectTargetLineGroups(
                                                                file));

                                continue;
                        }

                        if (!TargetFileConfig.isTargetFile(
                                        file)) {

                                continue;
                        }

                        List<DuplicateCodeLine> fileLines = collectTargetLinesFromFile(
                                        file);

                        if (fileLines.size() < BLOCK_SIZE) {

                                continue;
                        }

                        fileLineGroups.add(
                                        fileLines);
                }

                return fileLineGroups;
        }

        private List<DuplicateCodeLine> collectTargetLinesFromFile(
                        File file)
                        throws Exception {

                List<DuplicateCodeLine> fileLines = new ArrayList<>();

                List<String> lines = Files.readAllLines(
                                file.toPath());

                LanguageIgnoreLineConfig config = IgnoreLineConfigFactory.get(
                                file);

                for (int i = 0; i < lines.size(); i++) {

                        String normalizedLine = normalize(
                                        lines.get(
                                                        i));

                        if (config != null
                                        && config.shouldIgnoreLine(
                                                        normalizedLine)) {

                                continue;
                        }

                        fileLines.add(
                                        new DuplicateCodeLine(
                                                        file.getPath(),
                                                        i + 1,
                                                        normalizedLine));
                }

                return fileLines;
        }

        private Map<String, List<DuplicateCodeLine>> createBlockMap(
                        List<List<DuplicateCodeLine>> fileLineGroups) {

                Map<String, List<DuplicateCodeLine>> blockMap = new HashMap<>();

                for (List<DuplicateCodeLine> fileLines : fileLineGroups) {

                        for (int i = 0; i <= fileLines.size()
                                        - BLOCK_SIZE; i++) {

                                String blockText = createBlockText(
                                                fileLines,
                                                i);

                                blockMap.computeIfAbsent(
                                                blockText,
                                                key -> new ArrayList<>())
                                                .add(
                                                                fileLines.get(
                                                                                i));
                        }
                }

                return blockMap;
        }

        private int countDuplicateBlocks(
                        List<List<DuplicateCodeLine>> fileLineGroups) {

                Map<String, List<DuplicateCodeLine>> blockMap = createBlockMap(
                                fileLineGroups);

                int duplicateCount = 0;

                for (List<DuplicateCodeLine> duplicates : blockMap.values()) {

                        if (duplicates.size() > 1) {

                                duplicateCount++;
                        }
                }

                return duplicateCount;
        }

        private String createBlockText(
                        List<DuplicateCodeLine> lines,
                        int startIndex) {

                StringBuilder block = new StringBuilder();

                for (int i = 0; i < BLOCK_SIZE; i++) {

                        block.append(
                                        lines.get(
                                                        startIndex + i)
                                                        .getContent())
                                        .append(
                                                        "\n");
                }

                return block.toString();
        }

        private String normalize(
                        String content) {

                return content
                                .replaceAll(
                                                "//.*",
                                                "")
                                .replaceAll(
                                                "#.*",
                                                "")
                                .trim();
        }

        private int calculateScore(
                        int blockDuplicateCount) {

                if (blockDuplicateCount == 0) {

                        return MAX_SCORE;
                }

                if (blockDuplicateCount <= 2) {

                        return 8;
                }

                if (blockDuplicateCount <= 5) {

                        return 5;
                }

                if (blockDuplicateCount <= 10) {

                        return 2;
                }

                return 0;
        }
}