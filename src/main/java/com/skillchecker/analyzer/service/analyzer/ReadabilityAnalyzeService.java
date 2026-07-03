package com.skillchecker.analyzer.service.analyzer;

import java.io.File;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.service.analyzer.readability.CommentChecker;
import com.skillchecker.analyzer.service.analyzer.readability.LineLengthChecker;
import com.skillchecker.analyzer.service.analyzer.readability.MethodLengthChecker;
import com.skillchecker.analyzer.service.analyzer.readability.NestingChecker;

@Service
public class ReadabilityAnalyzeService {

        private static final int MAX_SCORE = 10;

        private final LineLengthChecker lineLengthChecker;

        private final MethodLengthChecker methodLengthChecker;

        private final NestingChecker nestingChecker;

        private final CommentChecker commentChecker;

        public ReadabilityAnalyzeService(
                        LineLengthChecker lineLengthChecker,
                        MethodLengthChecker methodLengthChecker,
                        NestingChecker nestingChecker,
                        CommentChecker commentChecker) {

                this.lineLengthChecker = lineLengthChecker;

                this.methodLengthChecker = methodLengthChecker;

                this.nestingChecker = nestingChecker;

                this.commentChecker = commentChecker;
        }

        public int analyze(
                        File repositoryDirectory) {

                int violationCount = 0;

                violationCount += lineLengthChecker.analyze(
                                repositoryDirectory);

                violationCount += methodLengthChecker.analyze(
                                repositoryDirectory);

                violationCount += nestingChecker.analyze(
                                repositoryDirectory);

                violationCount += commentChecker.analyze(
                                repositoryDirectory);

                return calculateScore(
                                violationCount);
        }

        private int calculateScore(
                        int violationCount) {

                if (violationCount == 0) {

                        return MAX_SCORE;
                }

                if (violationCount <= 2) {

                        return 8;
                }

                if (violationCount <= 5) {

                        return 5;
                }

                if (violationCount <= 10) {

                        return 2;
                }

                return 0;
        }
}