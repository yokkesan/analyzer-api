package com.skillchecker.analyzer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "analysis_result_details")
public class AnalysisResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "analysis_result_id",
            nullable = false)
    private AnalysisResult analysisResult;

    private String framework;

    private Integer readmeScore;

    private Integer gitScore;

    private Integer securityScore;

    private Integer namingScore;

    private Integer readabilityScore;

    private Integer duplicateCodeScore;

    private Integer totalScore;

    private LocalDateTime createdAt;
}