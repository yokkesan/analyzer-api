package com.skillchecker.analyzer.service.analyzer.config;

public class DuplicateCodeLine {

    private String file;

    private int lineNumber;

    private String content;

    public DuplicateCodeLine(
            String file,
            int lineNumber,
            String content) {

        this.file = file;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    public String getFile() {

        return file;
    }

    public int getLineNumber() {

        return lineNumber;
    }

    public String getContent() {

        return content;
    }
}