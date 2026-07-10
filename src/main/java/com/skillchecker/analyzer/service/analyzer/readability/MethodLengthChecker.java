package com.skillchecker.analyzer.service.analyzer.readability;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.config.common.TargetFileConfig;

@Service
public class MethodLengthChecker {

    private static final int MAX_METHOD_LINES = 50;

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

            boolean inMethod = false;

            int methodStartLine = 0;

            int braceCount = 0;

            for (int i = 0; i < lines.size(); i++) {

                String line =
                        lines.get(i).trim();

                if (!inMethod
                        && isMethodDeclaration(
                                line)) {

                    inMethod = true;

                    methodStartLine = i + 1;

                    braceCount = 1;

                    continue;
                }

                if (!inMethod) {

                    continue;
                }

                for (char c : line.toCharArray()) {

                    if (c == '{') {

                        braceCount++;
                    }

                    if (c == '}') {

                        braceCount--;
                    }
                }

                if (braceCount == 0) {

                    int methodLength =
                            (i + 1)
                                    - methodStartLine
                                    + 1;

                    if (methodLength
                            > MAX_METHOD_LINES) {

                        issues.add(
                                new AnalysisIssue(
                                        file.getPath(),
                                        methodStartLine,
                                        i + 1,
                                        line,
                                        "メソッドが"
                                                + MAX_METHOD_LINES
                                                + "行を超えています"));
                    }

                    inMethod = false;
                }
            }
        }
    }

    private boolean isMethodDeclaration(
            String line) {

        return line.matches(
                        ".*\\w+\\s+\\w+\\s*\\([^)]*\\)\\s*\\{\\s*$")
                || line.matches(
                        ".*function\\s+\\w+\\s*\\([^)]*\\)\\s*\\{\\s*$");
    }
}