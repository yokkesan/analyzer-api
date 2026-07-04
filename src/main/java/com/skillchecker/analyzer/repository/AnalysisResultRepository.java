package com.skillchecker.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillchecker.analyzer.entity.AnalysisResult;

public interface AnalysisResultRepository
        extends JpaRepository<AnalysisResult, Long> {
}