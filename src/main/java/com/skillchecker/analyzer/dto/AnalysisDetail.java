package com.skillchecker.analyzer.dto;

import java.util.List;

public class AnalysisDetail {

    private String category;

    private int score;

    private int maxScore;

    private String message;

    private String comment;

    private List<AnalysisIssue> issues;

    public AnalysisDetail(
            String category,
            int score,
            int maxScore,
            String message,
            String comment,
            List<AnalysisIssue> issues) {

        this.category = category;
        this.score = score;
        this.maxScore = maxScore;
        this.message = message;
        this.comment = comment;
        this.issues = issues;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public String getMessage() {
        return message;
    }

    public String getComment() {
        return comment;
    }

    public List<AnalysisIssue> getIssues() {
        return issues;
    }
}