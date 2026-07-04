CREATE TABLE analysis_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    repository_url VARCHAR(500) NOT NULL,

    total_score INT NOT NULL,

    created_at DATETIME NOT NULL
);