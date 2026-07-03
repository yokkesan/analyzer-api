package com.skillchecker.analyzer.service.analyzer;

import org.springframework.stereotype.Service;

@Service
public class ScoreCalculateService {

        private static final int LANGUAGE_SCORE = 10;

        private static final int FRAMEWORK_SCORE = 10;

        public int calculate(
                        String language,
                        String framework,
                        int readmeScore,
                        int gitScore,
                        int securityScore,
                        int namingScore,
                        int readabilityScore,
                        int duplicateCodeScore) {

                int totalScore = 0;

                if (!"Unknown".equals(language)) {

                        totalScore += LANGUAGE_SCORE;
                }

                if (!"Unknown".equals(framework)) {

                        totalScore += FRAMEWORK_SCORE;
                }

                totalScore += readmeScore;

                totalScore += gitScore;

                totalScore += securityScore;

                totalScore += namingScore;

                totalScore += readabilityScore;

                totalScore += duplicateCodeScore;

                return Math.min(
                                totalScore,
                                100);
        }
}