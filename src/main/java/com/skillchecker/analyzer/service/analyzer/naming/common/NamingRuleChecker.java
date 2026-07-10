package com.skillchecker.analyzer.service.analyzer.naming.common;

import java.io.File;
import java.util.List;

import com.skillchecker.analyzer.dto.AnalysisIssue;

public interface NamingRuleChecker {

        boolean supports(
                        File file);

        List<AnalysisIssue> analyzeClass(
                        File file);

        List<AnalysisIssue> analyzeMethod(
                        File file);

        List<AnalysisIssue> analyzeVariable(
                        File file);

        List<AnalysisIssue> analyzeConstant(
                        File file);
}