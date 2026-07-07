package com.skillchecker.analyzer.service.analyzer.comment;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

@Service
public class GitCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score) {

        return new AnalysisDetail(
                "Git運用",
                score,
                MAX_SCORE,
                createMessage(score));
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "コミット履歴は十分です";
        }

        if (score >= 5) {
            return "コミット履歴は一定数あります";
        }

        return "コミット履歴が少ないため改善の余地があります";
    }
}