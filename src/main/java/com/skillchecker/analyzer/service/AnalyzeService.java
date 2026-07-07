package com.skillchecker.analyzer.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalyzeResult;
import com.skillchecker.analyzer.entity.AnalysisResult;
import com.skillchecker.analyzer.entity.AnalysisResultDetail;
import com.skillchecker.analyzer.repository.AnalysisResultDetailRepository;
import com.skillchecker.analyzer.repository.AnalysisResultRepository;
import com.skillchecker.analyzer.service.analyzer.DuplicateCodeAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.FrameworkAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.GitAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.NamingAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ReadabilityAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ReadmeAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.ScoreCalculateService;
import com.skillchecker.analyzer.service.analyzer.SecurityAnalyzeService;
import com.skillchecker.analyzer.service.analyzer.AnalysisCommentCreateService;

@Service
public class AnalyzeService {

        private final GitCloneService gitCloneService;
        private final RepositoryDeleteService repositoryDeleteService;
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
        private final AnalysisCommentCreateService analysisCommentCreateService;

        public AnalyzeService(
                        GitCloneService gitCloneService,
                        RepositoryDeleteService repositoryDeleteService,
                        FrameworkAnalyzeService frameworkAnalyzeService,
                        ReadmeAnalyzeService readmeAnalyzeService,
                        GitAnalyzeService gitAnalyzeService,
                        SecurityAnalyzeService securityAnalyzeService,
                        NamingAnalyzeService namingAnalyzeService,
                        ReadabilityAnalyzeService readabilityAnalyzeService,
                        DuplicateCodeAnalyzeService duplicateCodeAnalyzeService,
                        ScoreCalculateService scoreCalculateService,
                        AnalysisResultRepository analysisResultRepository,
                        AnalysisResultDetailRepository analysisResultDetailRepository,
                        AnalysisCommentCreateService analysisCommentCreateService) {

                this.gitCloneService = gitCloneService;
                this.repositoryDeleteService = repositoryDeleteService;
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
                this.analysisCommentCreateService = analysisCommentCreateService;
        }

        @Transactional
        public AnalyzeResult analyze(
                        Long repositoryId,
                        String githubUrl) {

                File repositoryDirectory =
                                new File("repositories/" + repositoryId);

                try {

                        System.out.println("Analyze Service Start");

                        gitCloneService.cloneRepository(
                                        repositoryId,
                                        githubUrl);

                        String framework =
                                        frameworkAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int readmeScore =
                                        readmeAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int gitScore =
                                        gitAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int securityScore =
                                        securityAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int namingScore =
                                        namingAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int readabilityScore =
                                        readabilityAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int duplicateCodeScore =
                                        duplicateCodeAnalyzeService.analyze(
                                                        repositoryDirectory);

                        int totalScore =
                                        scoreCalculateService.calculate(
                                                        framework,
                                                        readmeScore,
                                                        gitScore,
                                                        securityScore,
                                                        namingScore,
                                                        readabilityScore,
                                                        duplicateCodeScore);

                        List<AnalysisDetail> details =
                                        analysisCommentCreateService.create(
                                                        gitScore,
                                                        namingScore,
                                                        readmeScore,
                                                        readabilityScore,
                                                        securityScore,
                                                        duplicateCodeScore);

                        LocalDateTime now =
                                        LocalDateTime.now();

                        AnalysisResult analysisResult =
                                        new AnalysisResult();

                        analysisResult.setRepositoryUrl(
                                        githubUrl);

                        analysisResult.setTotalScore(
                                        totalScore);

                        analysisResult.setCreatedAt(
                                        now);

                        analysisResult =
                                        analysisResultRepository.save(
                                                        analysisResult);

                        AnalysisResultDetail detail =
                                        new AnalysisResultDetail();

                        detail.setAnalysisResult(
                                        analysisResult);

                        detail.setFramework(
                                        framework);

                        detail.setReadmeScore(
                                        readmeScore);

                        detail.setGitScore(
                                        gitScore);

                        detail.setSecurityScore(
                                        securityScore);

                        detail.setNamingScore(
                                        namingScore);

                        detail.setReadabilityScore(
                                        readabilityScore);

                        detail.setDuplicateCodeScore(
                                        duplicateCodeScore);

                        detail.setTotalScore(
                                        totalScore);

                        detail.setCreatedAt(
                                        now);

                        analysisResultDetailRepository.save(
                                        detail);

                        System.out.println(
                                        "Total Score : "
                                                        + totalScore);

                        return new AnalyzeResult(
                                        framework,
                                        totalScore,
                                        null,
                                        details);

                } catch (Exception e) {

                        e.printStackTrace();

                        return new AnalyzeResult(
                                        "Unknown",
                                        0,
                                        null,
                                        List.of());

                } finally {

                        try {

                                repositoryDeleteService.deleteRepository(
                                                repositoryDirectory);

                        } catch (Exception e) {

                                e.printStackTrace();
                        }
                }
        }
}