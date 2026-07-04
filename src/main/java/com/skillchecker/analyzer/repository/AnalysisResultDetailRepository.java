package com.skillchecker.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillchecker.analyzer.entity.AnalysisResultDetail;

public interface AnalysisResultDetailRepository
        extends JpaRepository<AnalysisResultDetail, Long> {
}