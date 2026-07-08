package com.skillchecker.analyzer.dto;

public class AnalysisIssue {

    private String file;

    private int startLine;

    private int endLine;

    private String code;

    private String reason;

    public AnalysisIssue(
        String file,
        int startLine,
        int endLine,
        String code,
        String reason
    ) {
        this.file = file;
        this.startLine = startLine;
        this.endLine = endLine;
        this.code = code;
        this.reason = reason;
    }

    public String getFile() {
        return file;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}