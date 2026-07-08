package com.skillchecker.analyzer.service.analyzer.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;
import com.skillchecker.analyzer.dto.AnalysisIssue;

@Service
public class ReadmeCommentService {

    private static final int MAX_SCORE = 10;

    public AnalysisDetail create(
            int score,
            List<AnalysisIssue> issues) {

        return new AnalysisDetail(
                "README",
                score,
                MAX_SCORE,
                createMessage(score),
                createComment(issues),
                issues);
    }

    private String createMessage(
            int score) {

        if (score >= 8) {
            return "READMEは十分に整備されています";
        }

        if (score >= 5) {
            return "READMEは存在します";
        }

        return "READMEの内容を充実させる余地があります";
    }

    private String createComment(
            List<AnalysisIssue> issues) {

        if (issues.isEmpty()) {
            return "対象がありませんでした";
        }

        return issues.size() + "件検出されました";
    }
}