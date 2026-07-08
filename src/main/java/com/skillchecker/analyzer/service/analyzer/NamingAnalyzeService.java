package com.skillchecker.analyzer.service.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.naming.ClassNamingChecker;
import com.skillchecker.analyzer.service.analyzer.naming.ConstantNamingChecker;
import com.skillchecker.analyzer.service.analyzer.naming.MethodNamingChecker;
import com.skillchecker.analyzer.service.analyzer.naming.VariableNamingChecker;

@Service
public class NamingAnalyzeService {

        private static final int MAX_SCORE = 10;

        private final ClassNamingChecker classNamingChecker;

        private final MethodNamingChecker methodNamingChecker;

        private final VariableNamingChecker variableNamingChecker;

        private final ConstantNamingChecker constantNamingChecker;

        public NamingAnalyzeService(
                        ClassNamingChecker classNamingChecker,
                        MethodNamingChecker methodNamingChecker,
                        VariableNamingChecker variableNamingChecker,
                        ConstantNamingChecker constantNamingChecker) {

                this.classNamingChecker = classNamingChecker;

                this.methodNamingChecker = methodNamingChecker;

                this.variableNamingChecker = variableNamingChecker;

                this.constantNamingChecker = constantNamingChecker;
        }

        public int analyze(
                        File repositoryDirectory) {

                int violationCount = 0;

                violationCount += classNamingChecker.analyze(
                                repositoryDirectory);

                violationCount += methodNamingChecker.analyze(
                                repositoryDirectory);

                violationCount += variableNamingChecker.analyze(
                                repositoryDirectory);

                violationCount += constantNamingChecker.analyze(
                                repositoryDirectory);

                return calculateScore(
                                violationCount);
        }

        public List<AnalysisIssue> getIssues(
                        File repositoryDirectory) {

                return new ArrayList<>();
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