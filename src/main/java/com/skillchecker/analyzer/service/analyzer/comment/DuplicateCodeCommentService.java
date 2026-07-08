package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class DuplicateCodeCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score,
            List<AnalysisIssue> issues) {

        return new AnalysisDetail(
                "重複コード",
                score,
                MAX_SCORE,
                createMessage(score),
                createComment(issues),
                issues);
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "重複コードは少ない状態です";
        }

        if (score >= 5) {
            return "一部重複コードがあります";
        }

        return "重複コードが多いため改善の余地があります";
    }

    private String createComment(
            List<AnalysisIssue> issues) {

        if (issues.isEmpty()) {
            return "対象がありませんでした";
        }

        return issues.size() + "件検出されました";
    }
}