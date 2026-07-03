package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

@Service
public class FrameworkAnalyzeService {

    public String analyze(
            File repositoryDirectory) {

        try {

            File artisan =
                    new File(
                            repositoryDirectory,
                            "artisan");

            if (artisan.exists()) {
                return "Laravel";
            }

            File packageJson =
                    new File(
                            repositoryDirectory,
                            "package.json");

            if (packageJson.exists()) {

                String content =
                        Files.readString(
                                packageJson.toPath());

                if (content.contains("\"next\"")) {
                    return "Next.js";
                }

                if (content.contains("\"react\"")) {
                    return "React";
                }

                if (content.contains("\"vue\"")) {
                    return "Vue";
                }

                if (content.contains("\"nuxt\"")) {
                    return "Nuxt";
                }
            }

            File pomXml =
                    new File(
                            repositoryDirectory,
                            "pom.xml");

            if (pomXml.exists()) {

                String content =
                        Files.readString(
                                pomXml.toPath());

                if (content.contains(
                        "spring-boot")) {

                    return "Spring Boot";
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "Unknown";
    }
}