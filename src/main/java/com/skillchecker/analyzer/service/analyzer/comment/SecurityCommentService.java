package com.skillchecker.analyzer.service.analyzer.comment;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

@Service
public class SecurityCommentService {

    public AnalysisDetail create(
            int score) {

        return new AnalysisDetail(
                "セキュリティ",
                score,
                createMessage(score));
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
}