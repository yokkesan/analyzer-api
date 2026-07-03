package com.skillchecker.analyzer.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalyzeResult;

@Service
public class AnalyzeService {

    private final GitCloneService gitCloneService;

    private final RepositoryDeleteService repositoryDeleteService;

    private final LanguageAnalyzeService languageAnalyzeService;

    public AnalyzeService(
            GitCloneService gitCloneService,
            RepositoryDeleteService repositoryDeleteService,
            LanguageAnalyzeService languageAnalyzeService) {

        this.gitCloneService =
                gitCloneService;

        this.repositoryDeleteService =
                repositoryDeleteService;

        this.languageAnalyzeService =
                languageAnalyzeService;
    }

    public AnalyzeResult analyze(
            Long repositoryId,
            String githubUrl) {

        File repositoryDirectory =
                new File(
                        "repositories/" + repositoryId);

        try {

            System.out.println(
                    "Analyze Service Start");

            gitCloneService.cloneRepository(
                    repositoryId,
                    githubUrl);

            String language =
                    languageAnalyzeService.analyze(
                            repositoryDirectory);

            System.out.println(
                    "Language : " + language);

            return new AnalyzeResult(
                    language);

        } catch (Exception e) {

            e.printStackTrace();

            return new AnalyzeResult(
                    "Unknown");

        } finally {

            try {

                repositoryDeleteService
                        .deleteRepository(
                                repositoryDirectory);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}