package com.skillchecker.analyzer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.skillchecker.analyzer.dto.AnalyzeRequest;
import com.skillchecker.analyzer.dto.AnalyzeResult;
import com.skillchecker.analyzer.service.AnalyzeService;

@RestController
public class AnalyzeController {

        private final AnalyzeService analyzeService;

        @Value("${analyzer.api-key}")
        private String apiKey;

        public AnalyzeController(
                        AnalyzeService analyzeService) {

                this.analyzeService = analyzeService;
        }

        @PostMapping("/analyze")
        public AnalyzeResult analyze(
                        @RequestHeader(
                                        value = "X-API-KEY",
                                        required = false)
                        String requestApiKey,

                        @RequestBody
                        AnalyzeRequest request) {

                if (!apiKey.equals(requestApiKey)) {
                        throw new UnauthorizedException();
                }

                return analyzeService.analyze(
                                request.getRepositoryId(),
                                request.getGithubUrl());
        }

        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        private static class UnauthorizedException
                        extends RuntimeException {
        }
}