package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class SecurityAnalyzeService {

    private static final int MAX_SCORE = 20;

    public int analyze(
            File repositoryDirectory) {

        try {

            int riskCount =
                    scanDirectory(
                            repositoryDirectory,
                            new ArrayList<>());

            return calculateScore(
                    riskCount);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    public List<AnalysisIssue> getIssues(
            File repositoryDirectory) {

        try {

            List<AnalysisIssue> issues =
                    new ArrayList<>();

            scanDirectory(
                    repositoryDirectory,
                    issues);

            return issues;

        } catch (Exception e) {

            e.printStackTrace();

            return List.of();
        }
    }

    private int scanDirectory(
            File directory,
            List<AnalysisIssue> issues)
            throws Exception {

        int riskCount = 0;

        File[] files =
                directory.listFiles();

        if (files == null) {

            return 0;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                riskCount +=
                        scanDirectory(
                                file,
                                issues);

                continue;
            }

            String fileName =
                    file.getName()
                            .toLowerCase();

            if (!isTargetFile(
                    fileName)) {

                continue;
            }

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            for (int i = 0; i < lines.size(); i++) {

                String line =
                        lines.get(i);

                String lowerLine =
                        line.toLowerCase();

                int lineNumber =
                        i + 1;

                if (lowerLine.contains(
                        "password")) {

                    issues.add(
                            new AnalysisIssue(
                                    file.getPath(),
                                    lineNumber,
                                    lineNumber,
                                    line,
                                    "passwordが含まれています"));

                    riskCount++;
                }

                if (lowerLine.contains(
                        "secret")) {

                    issues.add(
                            new AnalysisIssue(
                                    file.getPath(),
                                    lineNumber,
                                    lineNumber,
                                    line,
                                    "secretが含まれています"));

                    riskCount++;
                }

                if (lowerLine.contains(
                        "api_key")) {

                    issues.add(
                            new AnalysisIssue(
                                    file.getPath(),
                                    lineNumber,
                                    lineNumber,
                                    line,
                                    "api_keyが含まれています"));

                    riskCount++;
                }

                if (lowerLine.contains(
                        "access_key")) {

                    issues.add(
                            new AnalysisIssue(
                                    file.getPath(),
                                    lineNumber,
                                    lineNumber,
                                    line,
                                    "access_keyが含まれています"));

                    riskCount++;
                }

                if (lowerLine.contains(
                        "private_key")) {

                    issues.add(
                            new AnalysisIssue(
                                    file.getPath(),
                                    lineNumber,
                                    lineNumber,
                                    line,
                                    "private_keyが含まれています"));

                    riskCount++;
                }
            }
        }

        return riskCount;
    }

    private boolean isTargetFile(
            String fileName) {

        return fileName.endsWith(".java")
                || fileName.endsWith(".php")
                || fileName.endsWith(".js")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".jsx")
                || fileName.endsWith(".tsx")
                || fileName.endsWith(".vue")
                || fileName.endsWith(".kt")
                || fileName.endsWith(".go")
                || fileName.endsWith(".py")
                || fileName.endsWith(".rb")
                || fileName.endsWith(".cs")
                || fileName.endsWith(".html")
                || fileName.endsWith(".css")
                || fileName.endsWith(".scss")
                || fileName.endsWith(".json")
                || fileName.endsWith(".xml")
                || fileName.endsWith(".yml")
                || fileName.endsWith(".yaml")
                || fileName.endsWith(".properties")
                || fileName.endsWith(".sql")
                || fileName.endsWith(".md")
                || fileName.endsWith(".txt")
                || fileName.equals(".env");
    }

    private int calculateScore(
            int riskCount) {

        if (riskCount == 0) {

            return MAX_SCORE;
        }

        if (riskCount <= 2) {

            return 15;
        }

        if (riskCount <= 5) {

            return 10;
        }

        if (riskCount <= 10) {

            return 5;
        }

        return 0;
    }
}