package com.skillchecker.analyzer.service.analyzer.config.common;

import java.io.File;
import java.util.List;

import com.skillchecker.analyzer.service.analyzer.config.php.PhpIgnoreLineConfig;

public final class IgnoreLineConfigFactory {

        private static final List<LanguageIgnoreLineConfig> CONFIGS = List.of(
                        new PhpIgnoreLineConfig());

        private IgnoreLineConfigFactory() {
        }

        public static LanguageIgnoreLineConfig get(
                        File file) {

                return CONFIGS.stream()
                                .filter(
                                                config -> config.supports(
                                                                file))
                                .findFirst()
                                .orElse(null);
        }
}