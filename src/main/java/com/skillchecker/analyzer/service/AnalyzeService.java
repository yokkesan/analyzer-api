package com.skillchecker.analyzer.service;

import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    public void analyze(
        Long repositoryId,
        String githubUrl
    ) {

        System.out.println(
            "Analyze Service Start"
        );

        System.out.println(
            repositoryId
        );

        System.out.println(
            githubUrl
        );
    }
}