package com.skillchecker.analyzer.dto;

public class AnalyzeResult {

    private String language;

    private String framework;

    private int score;

    public AnalyzeResult(
            String language,
            String framework,
            int score) {

        this.language = language;

        this.framework = framework;

        this.score = score;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(
            String language) {

        this.language = language;
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