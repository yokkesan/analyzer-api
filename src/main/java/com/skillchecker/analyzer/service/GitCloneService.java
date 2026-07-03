package com.skillchecker.analyzer.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class GitCloneService {

    public void cloneRepository(
            Long repositoryId,
            String githubUrl) throws Exception {

        File repositoryDirectory =
                new File(
                        "repositories/" + repositoryId);

        repositoryDirectory.mkdirs();

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "git",
                        "clone",
                        githubUrl,
                        "repositories/" + repositoryId);

        processBuilder.redirectErrorStream(
                true);

        Process process =
                processBuilder.start();

        int exitCode =
                process.waitFor();

        System.out.println(
                "Clone Exit Code : " + exitCode);
    }
}