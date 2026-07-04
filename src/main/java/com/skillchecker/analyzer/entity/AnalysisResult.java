package com.skillchecker.analyzer.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "analysis_results")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repositoryUrl;

    private Integer totalScore;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "analysisResult",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AnalysisResultDetail> details;
}