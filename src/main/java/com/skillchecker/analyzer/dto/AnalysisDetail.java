package com.skillchecker.analyzer.dto;

public class AnalysisDetail {

    private String category;

    private int score;

    private String message;

    public AnalysisDetail(
        String category,
        int score,
        String message
    ) {
        this.category = category;
        this.score = score;
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    public String getMessage() {
        return message;
    }
}