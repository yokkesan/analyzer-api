package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

@Service
public class FrameworkAnalyzeService {

    public String analyze(
            File repositoryDirectory) {

        try {

            // Laravel
            if (new File(repositoryDirectory, "artisan").exists()) {
                return "Laravel";
            }

            // Symfony
            if (new File(repositoryDirectory, "bin/console").exists()) {
                return "Symfony";
            }

            // CodeIgniter
            if (new File(repositoryDirectory, "spark").exists()) {
                return "CodeIgniter";
            }

            // Angular
            if (new File(repositoryDirectory, "angular.json").exists()) {
                return "Angular";
            }

            // NestJS
            if (new File(repositoryDirectory, "nest-cli.json").exists()) {
                return "NestJS";
            }

            // Django
            if (new File(repositoryDirectory, "manage.py").exists()) {
                return "Django";
            }

            // Ruby on Rails
            if (new File(repositoryDirectory, "Gemfile").exists()) {
                return "Ruby on Rails";
            }

            // JavaScript / TypeScript系
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

                if (content.contains("\"nuxt\"")) {
                    return "Nuxt";
                }

                if (content.contains("\"react\"")) {
                    return "React";
                }

                if (content.contains("\"vue\"")) {
                    return "Vue";
                }

                if (content.contains("\"express\"")) {
                    return "Express";
                }
            }

            // Spring Boot (Maven)
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

                return "Maven";
            }

            // Spring Boot (Gradle)
            File buildGradle =
                    new File(
                            repositoryDirectory,
                            "build.gradle");

            if (buildGradle.exists()) {

                String content =
                        Files.readString(
                                buildGradle.toPath());

                if (content.contains(
                        "spring-boot")) {

                    return "Spring Boot";
                }

                return "Gradle";
            }

            File buildGradleKts =
                    new File(
                            repositoryDirectory,
                            "build.gradle.kts");

            if (buildGradleKts.exists()) {

                String content =
                        Files.readString(
                                buildGradleKts.toPath());

                if (content.contains(
                        "spring-boot")) {

                    return "Spring Boot";
                }

                return "Gradle";
            }

            // Go
            File goMod =
                    new File(
                            repositoryDirectory,
                            "go.mod");

            if (goMod.exists()) {

                String content =
                        Files.readString(
                                goMod.toPath());

                if (content.contains(
                        "github.com/gin-gonic/gin")) {

                    return "Gin";
                }

                if (content.contains(
                        "github.com/labstack/echo")) {

                    return "Echo";
                }

                if (content.contains(
                        "github.com/gofiber/fiber")) {

                    return "Fiber";
                }

                return "Go";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "Unknown";
    }
}