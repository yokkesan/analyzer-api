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
                    file.getName();

            if (fileName.endsWith(".png")
                    || fileName.endsWith(".jpg")
                    || fileName.endsWith(".jpeg")
                    || fileName.endsWith(".gif")
                    || file.length() > 1024 * 1024) {

                continue;
            }

            List<String> lines =
                    Files.readAllLines(
                            file.toPath());

            for (String line : lines) {

                if (line.contains("password")) {

                    riskCount++;
                }

                if (line.contains("secret")) {

                    riskCount++;
                }

                if (line.contains("api_key")) {

                    riskCount++;
                }

                if (line.contains("access_key")) {

                    riskCount++;
                }

                if (line.contains("private_key")) {

                    riskCount++;
                }
            }
        }

        return riskCount;
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