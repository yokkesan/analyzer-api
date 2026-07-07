package com.skillchecker.analyzer.service.analyzer.comment;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

@Service
public class DuplicateCodeCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score) {

        return new AnalysisDetail(
                "重複コード",
                score,
                MAX_SCORE,
                createMessage(score));
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
}