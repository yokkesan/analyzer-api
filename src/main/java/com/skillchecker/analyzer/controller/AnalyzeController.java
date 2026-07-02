package com.skillchecker.analyzer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyzeController {

    @PostMapping("/analyze")
    public String analyze(
        @RequestBody String body
    ) {

        System.out.println("Analyze API Called");
        System.out.println(body);

        return "Analyze Started";
    }
}