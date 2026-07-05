package com.skillchecker.analyzer.dto;

import java.util.List;

public class AnalyzeResult {

    private String framework;

    private int totalScore;

    private String aiComment;

    private List<AnalysisDetail> details;

    public AnalyzeResult(
            String framework,
            int totalScore,
            String aiComment,
            List<AnalysisDetail> details) {

        this.framework = framework;
        this.totalScore = totalScore;
        this.aiComment = aiComment;
        this.details = details;
    }

    public String getFramework() {

        return framework;
    }

    public void setFramework(
            String framework) {

        this.framework = framework;
    }

    public int getTotalScore() {

        return totalScore;
    }

    public void setTotalScore(
            int totalScore) {

        this.totalScore = totalScore;
    }

    public String getAiComment() {

        return aiComment;
    }

    public void setAiComment(
            String aiComment) {

        this.aiComment = aiComment;
    }

    public List<AnalysisDetail> getDetails() {

        return details;
    }

    public void setDetails(
            List<AnalysisDetail> details) {

        this.details = details;
    }
}