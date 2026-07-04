package com.skillchecker.analyzer.service;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillchecker.analyzer.dto.AnalyzeResult;
import com.skillchecker.analyzer.entity.AnalysisResult;
import com.skillchecker.analyzer.entity.AnalysisResultDetail;
import com.skillchecker.analyzer.repository.AnalysisResultDetailRepository;
import com.skillchecker.analyzer.repository.AnalysisResultRepository;
import com.skillchecker.analyzer.service.analyzer.DuplicateCodeAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.FrameworkAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.GitAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.LanguageAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.NamingAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ReadabilityAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ReadmeAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ScoreCalculateService;
import com.skillchecker.analyzer.service.analyzer.SecurityAnalyzeService;

@Service
public class AnalyzeService {

        private final GitCloneService gitCloneService;
        private final RepositoryDeleteService repositoryDeleteService;
        private final LanguageAnalyzeService languageAnalyzeService;
        private final FrameworkAnalyzeService frameworkAnalyzeService;
        private final ReadmeAnalyzeService readmeAnalyzeService;
        private final GitAnalyzeService gitAnalyzeService;
        private final SecurityAnalyzeService securityAnalyzeService;
        private final NamingAnalyzeService namingAnalyzeService;
        private final ReadabilityAnalyzeService readabilityAnalyzeService;
        private final DuplicateCodeAnalyzeService duplicateCodeAnalyzeService;
        private final ScoreCalculateService scoreCalculateService;
        private final AnalysisResultRepository analysisResultRepository;
        private final AnalysisResultDetailRepository analysisResultDetailRepository;

        public AnalyzeService(
                        GitCloneService gitCloneService,
                        RepositoryDeleteService repositoryDeleteService,
                        LanguageAnalyzeService languageAnalyzeService,
                        FrameworkAnalyzeService frameworkAnalyzeService,
                        ReadmeAnalyzeService readmeAnalyzeService,
                        GitAnalyzeService gitAnalyzeService,
                        SecurityAnalyzeService securityAnalyzeService,
                        NamingAnalyzeService namingAnalyzeService,
                        ReadabilityAnalyzeService readabilityAnalyzeService,
                        DuplicateCodeAnalyzeService duplicateCodeAnalyzeService,
                        ScoreCalculateService scoreCalculateService,
                        AnalysisResultRepository analysisResultRepository,
                        AnalysisResultDetailRepository analysisResultDetailRepository) {

                this.gitCloneService = gitCloneService;
                this.repositoryDeleteService = repositoryDeleteService;
                this.languageAnalyzeService = languageAnalyzeService;
                this.frameworkAnalyzeService = frameworkAnalyzeService;
                this.readmeAnalyzeService = readmeAnalyzeService;
                this.gitAnalyzeService = gitAnalyzeService;
                this.securityAnalyzeService = securityAnalyzeService;
                this.namingAnalyzeService = namingAnalyzeService;
                this.readabilityAnalyzeService = readabilityAnalyzeService;
                this.duplicateCodeAnalyzeService = duplicateCodeAnalyzeService;
                this.scoreCalculateService = scoreCalculateService;
                this.analysisResultRepository = analysisResultRepository;
                this.analysisResultDetailRepository = analysisResultDetailRepository;
        }

        @Transactional
        public AnalyzeResult analyze(Long repositoryId, String githubUrl) {

                File repositoryDirectory = new File("repositories/" + repositoryId);

                try {

                        System.out.println("Analyze Service Start");

                        gitCloneService.cloneRepository(repositoryId, githubUrl);

                        String language = languageAnalyzeService.analyze(repositoryDirectory);
                        String framework = frameworkAnalyzeService.analyze(repositoryDirectory);

                        int readmeScore = readmeAnalyzeService.analyze(repositoryDirectory);
                        int gitScore = gitAnalyzeService.analyze(repositoryDirectory);
                        int securityScore = securityAnalyzeService.analyze(repositoryDirectory);
                        int namingScore = namingAnalyzeService.analyze(repositoryDirectory);
                        int readabilityScore = readabilityAnalyzeService.analyze(repositoryDirectory);
                        int duplicateCodeScore = duplicateCodeAnalyzeService.analyze(repositoryDirectory);

                        int totalScore = scoreCalculateService.calculate(
                                        language,
                                        framework,
                                        readmeScore,
                                        gitScore,
                                        securityScore,
                                        namingScore,
                                        readabilityScore,
                                        duplicateCodeScore);

                        LocalDateTime now = LocalDateTime.now();

                        AnalysisResult analysisResult = new AnalysisResult();
                        analysisResult.setRepositoryUrl(githubUrl);
                        analysisResult.setTotalScore(totalScore);
                        analysisResult.setCreatedAt(now);

                        analysisResult = analysisResultRepository.save(analysisResult);

                        AnalysisResultDetail detail = new AnalysisResultDetail();
                        detail.setAnalysisResult(analysisResult);
                        detail.setLanguage(language);
                        detail.setFramework(framework);
                        detail.setReadmeScore(readmeScore);
                        detail.setGitScore(gitScore);
                        detail.setSecurityScore(securityScore);
                        detail.setNamingScore(namingScore);
                        detail.setReadabilityScore(readabilityScore);
                        detail.setDuplicateCodeScore(duplicateCodeScore);
                        detail.setTotalScore(totalScore);
                        detail.setCreatedAt(now);

                        analysisResultDetailRepository.save(detail);

                        System.out.println("Total Score : " + totalScore);

                        return new AnalyzeResult(
                                        language,
                                        framework,
                                        totalScore);

                } catch (Exception e) {

                        e.printStackTrace();

                        return new AnalyzeResult(
                                        "Unknown",
                                        "Unknown",
                                        0);

                } finally {

                        try {
                                repositoryDeleteService.deleteRepository(repositoryDirectory);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }
}