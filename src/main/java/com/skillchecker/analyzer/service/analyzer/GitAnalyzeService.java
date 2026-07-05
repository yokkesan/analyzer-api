package com.skillchecker.analyzer.service.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class GitAnalyzeService {

        private static final int MAX_SCORE = 15;

        public int analyze(
                        File repositoryDirectory) {

                try {

                        ProcessBuilder processBuilder = new ProcessBuilder(
                                        "git",
                                        "rev-list",
                                        "--count",
                                        "HEAD");

                        processBuilder.directory(
                                        repositoryDirectory);

                        Process process = processBuilder.start();

                        BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(
                                                        process.getInputStream()));

                        String line = reader.readLine();

                        process.waitFor();

                        if (line == null) {

                                return 0;
                        }

                        int commitCount = Integer.parseInt(line);

                        return calculateScore(
                                        commitCount);

                } catch (Exception e) {

                        e.printStackTrace();

                        return 0;
                }
        }

        private int calculateScore(
                        int commitCount) {

                if (commitCount >= 100) {
                        return 15;
                }

                if (commitCount >= 50) {
                        return 10;
                }

                if (commitCount >= 20) {
                        return 5;
                }

                if (commitCount >= 10) {
                        return 4;
                }

                if (commitCount >= 5) {
                        return 3;
                }

                if (commitCount >= 2) {
                        return 2;
                }

                if (commitCount >= 1) {
                        return 1;
                }

                return 0;
        }
}