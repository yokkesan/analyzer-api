package com.skillchecker.analyzer.service.analyzer.readability;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.config.common.TargetFileConfig;

@Service
public class NestingChecker {

    private static final int MAX_NESTING_LEVEL = 3;

    public int analyze(
            File repositoryDirectory) {

        return getIssues(
                repositoryDirectory).size();
    }

    public List<AnalysisIssue> getIssues(
            File repositoryDirectory) {

        List<AnalysisIssue> issues =
                new ArrayList<>();

        try {

            scanDirectory(
                    repositoryDirectory,
                    issues);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return issues;
    }

    private void scanDirectory(
            File directory,
            List<AnalysisIssue> issues)
            throws Exception {

        File[] files =
                directory.listFiles();

        if (files == null) {

            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                scanDirectory(
                        file,
                        issues);

                continue;
            }

            if (!TargetFileConfig.isTargetFile(
                    file)) {

                continue;
            }

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            int nestingLevel = 0;

            for (int i = 0; i < lines.size(); i++) {

                String line =
                        lines.get(i).trim();

                if (line.startsWith("<")) {

                    continue;
                }

                if (isControlStatement(
                                line)
                        && line.contains(
                                        "{")) {

                    nestingLevel++;

                    if (nestingLevel
                            > MAX_NESTING_LEVEL) {

                        issues.add(
                                new AnalysisIssue(
                                        file.getPath(),
                                        i + 1,
                                        i + 1,
                                        line,
                                        "ネストが"
                                                + MAX_NESTING_LEVEL
                                                + "階層を超えています"));
                    }
                }

                if (line.contains(
                                "}")) {

                    nestingLevel =
                            Math.max(
                                    0,
                                    nestingLevel - 1);
                }
            }
        }
    }

    private boolean isControlStatement(
            String line) {

        return line.startsWith(
                        "if")
                || line.startsWith(
                                "for")
                || line.startsWith(
                                "while")
                || line.startsWith(
                                "switch")
                || line.startsWith(
                                "catch")
                || line.startsWith(
                                "foreach")
                || line.startsWith(
                                "else if")
                || line.startsWith(
                                "else");
    }
}