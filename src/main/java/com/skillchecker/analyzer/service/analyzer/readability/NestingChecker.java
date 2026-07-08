package com.skillchecker.analyzer.service.analyzer.readability;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;

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

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            int nestingLevel = 0;

            for (int i = 0; i < lines.size(); i++) {

                String line =
                        lines.get(i);

                for (char c : line.toCharArray()) {

                    if (c == '{') {

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

                    if (c == '}') {

                        nestingLevel =
                                Math.max(
                                        0,
                                        nestingLevel - 1);
                    }
                }
            }
        }
    }
}