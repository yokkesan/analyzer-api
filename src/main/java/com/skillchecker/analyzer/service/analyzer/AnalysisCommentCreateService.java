package com.skillchecker.analyzer.service.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.comment.DuplicateCodeCommentService;
import com.skillchecker.analyzer.service.analyzer.comment.GitCommentService;
import com.skillchecker.analyzer.service.analyzer.comment.NamingCommentService;
import com.skillchecker.analyzer.service.analyzer.comment.ReadabilityCommentService;
import com.skillchecker.analyzer.service.analyzer.comment.ReadmeCommentService;
import com.skillchecker.analyzer.service.analyzer.comment.SecurityCommentService;

@Service
public class AnalysisCommentCreateService {

        private final GitCommentService gitCommentService;
        private final NamingCommentService namingCommentService;
        private final ReadmeCommentService readmeCommentService;
        private final ReadabilityCommentService readabilityCommentService;
        private final SecurityCommentService securityCommentService;
        private final DuplicateCodeCommentService duplicateCodeCommentService;

        public AnalysisCommentCreateService(
                        GitCommentService gitCommentService,
                        NamingCommentService namingCommentService,
                        ReadmeCommentService readmeCommentService,
                        ReadabilityCommentService readabilityCommentService,
                        SecurityCommentService securityCommentService,
                        DuplicateCodeCommentService duplicateCodeCommentService) {

                this.gitCommentService = gitCommentService;
                this.namingCommentService = namingCommentService;
                this.readmeCommentService = readmeCommentService;
                this.readabilityCommentService = readabilityCommentService;
                this.securityCommentService = securityCommentService;
                this.duplicateCodeCommentService = duplicateCodeCommentService;
        }

        public List<AnalysisDetail> create(
                        int gitScore,
                        int namingScore,
                        int readmeScore,
                        int readabilityScore,
                        int securityScore,
                        int duplicateCodeScore,
                        List<AnalysisIssue> gitIssues,
                        List<AnalysisIssue> namingIssues,
                        List<AnalysisIssue> readmeIssues,
                        List<AnalysisIssue> readabilityIssues,
                        List<AnalysisIssue> securityIssues,
                        List<AnalysisIssue> duplicateCodeIssues) {

                List<AnalysisDetail> details = new ArrayList<>();

                details.add(
                                gitCommentService.create(
                                                gitScore,
                                                gitIssues));

                details.add(
                                namingCommentService.create(
                                                namingScore,
                                                namingIssues));

                details.add(
                                readmeCommentService.create(
                                                readmeScore,
                                                readmeIssues));

                details.add(
                                readabilityCommentService.create(
                                                readabilityScore,
                                                readabilityIssues));

                details.add(
                                securityCommentService.create(
                                                securityScore,
                                                securityIssues));

                details.add(
                                duplicateCodeCommentService.create(
                                                duplicateCodeScore,
                                                duplicateCodeIssues));

                return details;
        }
}