package com.skillchecker.analyzer.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class RepositoryDeleteService {

    public void deleteRepository(
            File repositoryDirectory) throws Exception {

        if (!repositoryDirectory.exists()) {

            return;
        }

        Path repositoryPath =
                repositoryDirectory.toPath();

        Files.walk(repositoryPath)
                .sorted(
                        (path1, path2) ->
                                path2.compareTo(path1))
                .forEach(path -> {

                    try {

                        Files.delete(path);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                });

        System.out.println(
                "Repository Deleted");
    }
}