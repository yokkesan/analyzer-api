package com.skillchecker.analyzer.service.analyzer.readability;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class LineLengthChecker {

    private static final int MAX_LINE_LENGTH = 120;

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

            for (int i = 0; i < lines.size(); i++) {

                String line =
                        lines.get(i);

                if (line.length()
                        <= MAX_LINE_LENGTH) {

                    continue;
                }

                int lineNumber =
                        i + 1;

                issues.add(
                        new AnalysisIssue(
                                file.getPath(),
                                lineNumber,
                                lineNumber,
                                line,
                                "1行が"
                                        + MAX_LINE_LENGTH
                                        + "文字を超えています"));
            }
        }
    }
}