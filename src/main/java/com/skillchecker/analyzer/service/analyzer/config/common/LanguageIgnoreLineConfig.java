package com.skillchecker.analyzer.service.analyzer.config.common;

import java.io.File;

public interface LanguageIgnoreLineConfig {

        boolean supports(
                        File file);

        boolean shouldIgnoreLine(
                        String line);
}