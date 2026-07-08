package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class SecurityCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score,
            List<AnalysisIssue> issues) {

        return new AnalysisDetail(
                "セキュリティ",
                score,
                MAX_SCORE,
                createMessage(score),
                createComment(issues),
                issues);
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "重大な問題は検出されませんでした";
        }

        if (score >= 5) {
            return "一部確認が必要な箇所があります";
        }

        return "セキュリティ面で改善の余地があります";
    }

    private String createComment(
            List<AnalysisIssue> issues) {

        if (issues.isEmpty()) {
            return "対象がありませんでした";
        }

        return issues.size() + "件検出されました";
    }
}