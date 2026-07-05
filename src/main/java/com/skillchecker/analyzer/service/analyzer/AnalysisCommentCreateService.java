package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

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
            int duplicateCodeScore) {

        List<AnalysisDetail> details =
                new ArrayList<>();

        details.add(
                gitCommentService.create(gitScore));

        details.add(
                namingCommentService.create(namingScore));

        details.add(
                readmeCommentService.create(readmeScore));

        details.add(
                readabilityCommentService.create(readabilityScore));

        details.add(
                securityCommentService.create(securityScore));

        details.add(
                duplicateCodeCommentService.create(duplicateCodeScore));

        return details;
    }
}