package com.skillchecker.analyzer.dto;

public class AnalyzeResult {

    private String language;

    public AnalyzeResult(
            String language) {

        this.language = language;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(
            String language) {

        this.language = language;
    }
}