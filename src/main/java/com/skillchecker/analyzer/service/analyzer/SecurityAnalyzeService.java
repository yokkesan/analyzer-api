package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SecurityAnalyzeService {

    private static final int MAX_SCORE = 20;

    public int analyze(
            File repositoryDirectory) {

        try {

            int riskCount =
                    scanDirectory(
                            repositoryDirectory);

            return calculateScore(
                    riskCount);

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }

    private int scanDirectory(
            File directory)
            throws Exception {

        int riskCount = 0;

        File[] files =
                directory.listFiles();

        if (files == null) {

            return 0;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                riskCount +=
                        scanDirectory(
                                file);

                continue;
            }

            String fileName =
                    file.getName()
                            .toLowerCase();

            if (!isTargetFile(
                    fileName)) {

                continue;
            }

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            for (String line : lines) {

                String lowerLine =
                        line.toLowerCase();

                if (lowerLine.contains(
                        "password")) {

                    riskCount++;
                }

                if (lowerLine.contains(
                        "secret")) {

                    riskCount++;
                }

                if (lowerLine.contains(
                        "api_key")) {

                    riskCount++;
                }

                if (lowerLine.contains(
                        "access_key")) {

                    riskCount++;
                }

                if (lowerLine.contains(
                        "private_key")) {

                    riskCount++;
                }
            }
        }

        return riskCount;
    }

    private boolean isTargetFile(
            String fileName) {

        return fileName.endsWith(".java")
                || fileName.endsWith(".php")
                || fileName.endsWith(".js")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".jsx")
                || fileName.endsWith(".tsx")
                || fileName.endsWith(".vue")
                || fileName.endsWith(".kt")
                || fileName.endsWith(".go")
                || fileName.endsWith(".py")
                || fileName.endsWith(".rb")
                || fileName.endsWith(".cs")
                || fileName.endsWith(".html")
                || fileName.endsWith(".css")
                || fileName.endsWith(".scss")
                || fileName.endsWith(".json")
                || fileName.endsWith(".xml")
                || fileName.endsWith(".yml")
                || fileName.endsWith(".yaml")
                || fileName.endsWith(".properties")
                || fileName.endsWith(".sql")
                || fileName.endsWith(".md")
                || fileName.endsWith(".txt")
                || fileName.equals(".env");
    }

    private int calculateScore(
            int riskCount) {

        if (riskCount == 0) {

            return MAX_SCORE;
        }

        if (riskCount <= 2) {

            return 15;
        }

        if (riskCount <= 5) {

            return 10;
        }

        if (riskCount <= 10) {

            return 5;
        }

        return 0;
    }
}