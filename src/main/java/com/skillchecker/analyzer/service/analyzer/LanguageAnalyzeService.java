package com.skillchecker.analyzer.service.analyzer;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class LanguageAnalyzeService {

    public String analyze(
            File repositoryDirectory) {

        File[] files =
                repositoryDirectory.listFiles();

        if (files == null) {

            return "Unknown";
        }

        for (File file : files) {

            String fileName =
                    file.getName();

            if (fileName.equals(
                    "build.gradle.kts")) {

                return "Kotlin";
            }

            if (fileName.equals(
                    "pom.xml")) {

                return "Java";
            }

            if (fileName.equals(
                    "composer.json")) {

                return "PHP";
            }

            if (fileName.equals(
                    "package.json")) {

                return "Node.js";
            }
        }

        return "Unknown";
    }
}