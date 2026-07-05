package com.skillchecker.analyzer.service.analyzer.comment;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisDetail;

@Service
public class ReadmeCommentService {

    public AnalysisDetail create(
            int score) {

        return new AnalysisDetail(
                "README",
                score,
                createMessage(score));
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
}