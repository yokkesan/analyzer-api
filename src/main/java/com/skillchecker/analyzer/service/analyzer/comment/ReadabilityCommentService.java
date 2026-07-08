package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class ReadabilityCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score,
            List<AnalysisIssue> issues) {

        return new AnalysisDetail(
                "可読性",
                score,
                MAX_SCORE,
                createMessage(score),
                createComment(issues),
                issues);
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "コードは読みやすく整理されています";
        }

        if (score >= 5) {
            return "コードの可読性は一定水準です";
        }

        return "コードの可読性に改善の余地があります";
    }

    private String createComment(
            List<AnalysisIssue> issues) {

        if (issues.isEmpty()) {
            return "対象がありませんでした";
        }

        return issues.size() + "件検出されました";
    }
}