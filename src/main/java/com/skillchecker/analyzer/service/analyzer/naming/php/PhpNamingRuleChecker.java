package com.skillchecker.analyzer.service.analyzer.naming.php;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.skillchecker.analyzer.dto.AnalysisIssue;
import com.skillchecker.analyzer.service.analyzer.naming.common.NamingRuleChecker;

@Service
public class PhpNamingRuleChecker
                implements NamingRuleChecker {

        private static final Pattern CLASS_PATTERN = Pattern.compile(
                        "\\b(class|interface|trait|enum)\\s+([A-Za-z_][A-Za-z0-9_]*)");

        private static final Pattern METHOD_PATTERN = Pattern.compile(
                        "\\bfunction\\s+&?\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*\\(");

        private static final Pattern CONSTANT_PATTERN = Pattern.compile(
                        "\\bconst\\s+([A-Za-z_][A-Za-z0-9_]*)");

        private static final Pattern VARIABLE_PATTERN = Pattern.compile(
                        "\\$([A-Za-z_][A-Za-z0-9_]*)");

        private static final Set<String> IGNORE_VARIABLES = Set.of(
                        "this",
                        "GLOBALS",
                        "_SERVER",
                        "_GET",
                        "_POST",
                        "_FILES",
                        "_COOKIE",
                        "_SESSION",
                        "_REQUEST",
                        "_ENV");

        @Override
        public boolean supports(
                        File file) {

                String fileName = file.getName();

                return fileName.endsWith(
                                ".php")
                                || fileName.endsWith(
                                                ".blade.php");
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

                        checkMethodName(
                                        file,
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

                if (line.contains(
                                "new class")) {

                        return;
                }

                Matcher matcher = CLASS_PATTERN.matcher(
                                line);

                if (!matcher.find()) {

                        return;
                }

                String type = matcher.group(
                                1);

                String name = matcher.group(
                                2);

                if (isPascalCase(
                                name)) {

                        return;
                }

                issues.add(
                                new AnalysisIssue(
                                                file.getPath(),
                                                lineNumber,
                                                lineNumber,
                                                line.trim(),
                                                type
                                                                + "名はPascalCaseで命名してください"));
        }

        private void checkMethodName(
                        File file,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                Matcher matcher = METHOD_PATTERN.matcher(
                                line);

                if (!matcher.find()) {

                        return;
                }

                String name = matcher.group(
                                1);

                if (name.startsWith(
                                "__")) {

                        return;
                }

                if (isCamelCase(
                                name)) {

                        return;
                }

                if (file.getPath().contains("/tests/")
                                && name.startsWith("test_")) {

                        return;
                }

                issues.add(
                                new AnalysisIssue(
                                                file.getPath(),
                                                lineNumber,
                                                lineNumber,
                                                line.trim(),
                                                "メソッド名はcamelCaseで命名してください"));
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

        private void checkVariableName(
                        File file,
                        String line,
                        int lineNumber,
                        List<AnalysisIssue> issues) {

                Matcher matcher = VARIABLE_PATTERN.matcher(
                                line);

                while (matcher.find()) {

                        String name = matcher.group(
                                        1);

                        if (IGNORE_VARIABLES.contains(
                                        name)) {

                                continue;
                        }

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
                                                        "変数名「$"
                                                                        + name
                                                                        + "」はcamelCaseで命名してください"));
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