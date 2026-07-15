package com.skillchecker.analyzer.service.analyzer.naming.kotlin;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.naming.common.NamingRuleChecker;

@Service
public class KotlinNamingRuleChecker
                implements NamingRuleChecker {

        private static final Pattern CLASS_PATTERN = Pattern.compile(
                        "\\b(?:data\\s+class|sealed\\s+class|enum\\s+class|class|interface|object)\\s+([A-Za-z_][A-Za-z0-9_]*)");

        private static final Pattern METHOD_PATTERN = Pattern.compile(
                        "\\bfun\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*\\(");

        private static final Pattern CONSTANT_PATTERN = Pattern.compile(
                        "\\bconst\\s+val\\s+([A-Za-z_][A-Za-z0-9_]*)");

        private static final Pattern VARIABLE_PATTERN = Pattern.compile(
                        "\\b(?:private|protected|internal|public)?\\s*(?:lateinit\\s+)?(?:val|var)\\s+([A-Za-z_][A-Za-z0-9_]*)");

        @Override
        public boolean supports(
                        File file) {

                return file.getName()
                                .endsWith(
                                                ".kt");
        }

        @Override
        public List<AnalysisIssue> analyzeClass(
                        File file) {

                List<AnalysisIssue> issues = new ArrayList<>();

                List<String> lines = readLines(
                                file);

                for (int i = 0; i < lines.size(); i++) {

                        checkClassName(
                                        file,
                                        lines.get(i),
                                        i + 1,
                                        issues);
                }

                return issues;
        }

        @Override
        public List<AnalysisIssue> analyzeMethod(
                        File file) {

                List<AnalysisIssue> issues = new ArrayList<>();

                List<String> lines = readLines(
                                file);

                for (int i = 0; i < lines.size(); i++) {

                        String previousLine =
                                        i > 0
                                                        ? lines.get(
                                                                        i - 1)
                                                        : "";

                        checkMethodName(
                                        file,
                                        previousLine,
                                        lines.get(i),
                                        i + 1,
                                        issues);
                }

                return issues;
        }

        @Override
        public List<AnalysisIssue> analyzeVariable(
                        File file) {

                List<AnalysisIssue> issues = new ArrayList<>();

                List<String> lines = readLines(
                                file);

                for (int i = 0; i < lines.size(); i++) {

                        checkVariableName(
                                        file,
                                        lines.get(i),
                                        i + 1,
                                        issues);
                }

                return issues;
        }

        @Override
        public List<AnalysisIssue> analyzeConstant(
                        File file) {

                List<AnalysisIssue> issues = new ArrayList<>();

                List<String> lines = readLines(
                                file);

                for (int i = 0; i < lines.size(); i++) {

                        checkConstantName(
                                        file,
                                        lines.get(i),
                                        i + 1,
                                        issues);
                }

                return issues;
        }

        private List<String> readLines(
                        File file) {

                try {

                        return Files.readAllLines(
                                        file.toPath());

                } catch (Exception e) {

                        e.printStackTrace();

                        return List.of();
                }
        }

        private void checkClassName(
                        File file,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                Matcher matcher = CLASS_PATTERN.matcher(
                                line);

                while (matcher.find()) {

                        String name = matcher.group(
                                        1);

                        if (isPascalCase(
                                        name)) {

                                continue;
                        }

                        issues.add(
                                        new AnalysisIssue(
                                                        file.getPath(),
                                                        lineNumber,
                                                        lineNumber,
                                                        line.trim(),
                                                        "クラス名はPascalCaseで命名してください"));
                }
        }

        private void checkMethodName(
                        File file,
                        String previousLine,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                Matcher matcher = METHOD_PATTERN.matcher(
                                line);

                while (matcher.find()) {

                        String name = matcher.group(
                                        1);

                        boolean isComposable =
                                        previousLine.contains(
                                                        "@Composable");

                        if (isComposable) {

                                if (isPascalCase(
                                                name)) {

                                        continue;
                                }

                        } else {

                                if (isCamelCase(
                                                name)) {

                                        continue;
                                }
                        }

                        issues.add(
                                        new AnalysisIssue(
                                                        file.getPath(),
                                                        lineNumber,
                                                        lineNumber,
                                                        line.trim(),
                                                        "メソッド名はcamelCaseで命名してください"));
                }
        }

        private void checkVariableName(
                        File file,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                String fileName = file.getName();

                if (fileName.equals(
                                "Color.kt")
                                || fileName.equals(
                                                "Theme.kt")
                                || fileName.equals(
                                                "Type.kt")) {

                        return;
                }

                Matcher matcher = VARIABLE_PATTERN.matcher(
                                line);

                while (matcher.find()) {

                        String name = matcher.group(
                                        1);

                        if (isCamelCase(
                                        name)) {

                                continue;
                        }

                        issues.add(
                                        new AnalysisIssue(
                                                        file.getPath(),
                                                        lineNumber,
                                                        lineNumber,
                                                        line.trim(),
                                                        "変数名「"
                                                                        + name
                                                                        + "」はcamelCaseで命名してください"));
                }
        }

        private void checkConstantName(
                        File file,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                Matcher matcher = CONSTANT_PATTERN.matcher(
                                line);

                while (matcher.find()) {

                        String name = matcher.group(
                                        1);

                        if (isUpperSnakeCase(
                                        name)) {

                                continue;
                        }

                        issues.add(
                                        new AnalysisIssue(
                                                        file.getPath(),
                                                        lineNumber,
                                                        lineNumber,
                                                        line.trim(),
                                                        "定数名はUPPER_SNAKE_CASEで命名してください"));
                }
        }

        private boolean isPascalCase(
                        String name) {

                return name.matches(
                                "^[A-Z][A-Za-z0-9]*$");
        }

        private boolean isCamelCase(
                        String name) {

                return name.matches(
                                "^[a-z][A-Za-z0-9]*$");
        }

        private boolean isUpperSnakeCase(
                        String name) {

                return name.matches(
                                "^[A-Z][A-Z0-9]*(?:_[A-Z0-9]+)*$");
        }
}