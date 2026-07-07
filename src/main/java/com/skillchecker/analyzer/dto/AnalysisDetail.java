package com.skillchecker.analyzer.dto;

public class AnalysisDetail {

    private String category;

    private int score;

    private int maxScore;

    private String message;

    public AnalysisDetail(
            String category,
            int score,
            int maxScore,
            String message) {

        this.category = category;
        this.score = score;
        this.maxScore = maxScore;
        this.message = message;
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
}