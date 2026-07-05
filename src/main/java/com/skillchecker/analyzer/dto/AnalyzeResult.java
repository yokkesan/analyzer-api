package com.skillchecker.analyzer.dto;

public class AnalyzeResult {

    private String framework;

    private int score;

    public AnalyzeResult(
            String framework,
            int score) {

        this.framework = framework;
        this.score = score;
    }

    public String getFramework() {

        return framework;
    }

    public void setFramework(
            String framework) {

        this.framework = framework;
    }

    public int getScore() {

        return score;
    }

    public void setScore(
            int score) {

        this.score = score;
    }
}