CREATE TABLE analysis_result_details (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    analysis_result_id BIGINT NOT NULL,

    language VARCHAR(100),

    framework VARCHAR(100),

    readme_score INT NOT NULL,

    git_score INT NOT NULL,

    security_score INT NOT NULL,

    naming_score INT NOT NULL,

    readability_score INT NOT NULL,

    duplicate_code_score INT NOT NULL,

    total_score INT NOT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_analysis_result_details
        FOREIGN KEY (analysis_result_id)
        REFERENCES analysis_results(id)

);