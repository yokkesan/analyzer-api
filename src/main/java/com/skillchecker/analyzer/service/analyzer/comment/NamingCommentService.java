package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class NamingCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score,
            List<AnalysisIssue> issues) {

        return new AnalysisDetail(
                "命名規則",
                score,
                MAX_SCORE,
                createMessage(score),
                createComment(issues),
                issues);
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "命名規則は概ね統一されています";
        }

        if (score >= 5) {
            return "命名規則は一部改善の余地があります";
        }

        return "命名にばらつきが見られます";
    }

    private String createComment(
            List<AnalysisIssue> issues) {

        if (issues.isEmpty()) {
            return "対象がありませんでした";
        }

        return issues.size() + "件検出されました";
    }
}