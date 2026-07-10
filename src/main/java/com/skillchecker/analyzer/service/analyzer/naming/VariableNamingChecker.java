package com.skillchecker.analyzer.service.analyzer.naming;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.naming.common.NamingRuleChecker;

@Service
public class VariableNamingChecker {

    private final List<NamingRuleChecker> checkers;

    public VariableNamingChecker(
            List<NamingRuleChecker> checkers) {

        this.checkers = checkers;
    }

    public int analyze(
            File repositoryDirectory) {

        return getIssues(
                repositoryDirectory)
                .size();
    }

    public List<AnalysisIssue> getIssues(
            File repositoryDirectory) {

        List<AnalysisIssue> issues =
                new ArrayList<>();

        collectIssues(
                repositoryDirectory,
                issues);

        return issues;
    }

    private void collectIssues(
            File directory,
            List<AnalysisIssue> issues) {

        File[] files =
                directory.listFiles();

        if (files == null) {

            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                collectIssues(
                        file,
                        issues);

                continue;
            }

            for (NamingRuleChecker checker : checkers) {

                if (!checker.supports(
                        file)) {

                    continue;
                }

                issues.addAll(
                        checker.analyzeVariable(
                                file));
            }
        }
    }
}