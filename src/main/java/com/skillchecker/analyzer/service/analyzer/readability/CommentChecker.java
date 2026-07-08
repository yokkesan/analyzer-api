package com.skillchecker.analyzer.service.analyzer.readability;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class CommentChecker {

    public int analyze(
            File repositoryDirectory) {

        int violationCount = 0;

        return violationCount;
    }

    public List<AnalysisIssue> getIssues(
            File repositoryDirectory) {

        return List.of();
    }
}