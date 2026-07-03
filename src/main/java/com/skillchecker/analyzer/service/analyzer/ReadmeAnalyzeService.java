package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

@Service
public class ReadmeAnalyzeService {

    private static final int MAX_SCORE = 15;

    public int analyze(
            File repositoryDirectory) {

        File readmeFile =
                new File(
                        repositoryDirectory,
                        "README.md");

        if (!readmeFile.exists()) {

            return 0;
        }

        try {

            String content =
                    Files.readString(
                            readmeFile.toPath());

            int score = 0;

            score += calculateLengthScore(
                    content);

            score += calculateStructureScore(
                    content);

            return Math.min(
                    score,
                    MAX_SCORE);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    private int calculateLengthScore(
            String content) {

        if (content.length() >= 1000) {

            return 7;
        }

        if (content.length() >= 500) {

            return 4;
        }

        return 0;
    }

    private int calculateStructureScore(
            String content) {

        int score = 0;

        if (content.contains("#")) {

            score += 2;
        }

        if (content.contains("##")) {

            score += 2;
        }

        if (content.contains("```")) {

            score += 2;
        }

        if (content.contains("- ")) {

            score += 2;
        }

        return score;
    }
}