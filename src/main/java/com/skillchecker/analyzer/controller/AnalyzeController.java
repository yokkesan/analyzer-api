package com.skillchecker.analyzer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skillchecker.analyzer.dto.AnalyzeRequest;
import com.skillchecker.analyzer.dto.AnalyzeResult;
import com.skillchecker.analyzer.service.AnalyzeService;

@RestController
public class AnalyzeController {

        private final AnalyzeService analyzeService;

        public AnalyzeController(
                        AnalyzeService analyzeService) {

                this.analyzeService = analyzeService;
        }

        @PostMapping("/analyze")
        public AnalyzeResult analyze(
                        @RequestBody AnalyzeRequest request) {

                return analyzeService.analyze(
                                request.getRepositoryId(),
                                request.getGithubUrl());
        }
}