package com.skillchecker.analyzer.service.analyzer.comment;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

@Service
public class ReadabilityCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score) {

        return new AnalysisDetail(
                "可読性",
                score,
                MAX_SCORE,
                createMessage(score));
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
}