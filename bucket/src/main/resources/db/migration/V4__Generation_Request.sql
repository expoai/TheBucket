-- Migration for GenerationRequest table
-- Creates the generation_request table for tracking AI generation requests

CREATE TABLE IF NOT EXISTS generation_request (
    id BIGSERIAL PRIMARY KEY,
    prompt VARCHAR(500) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BIGINT
);

-- Create indexes for better query performance
CREATE INDEX idx_generation_request_status ON generation_request(status);
CREATE INDEX idx_generation_request_created_at ON generation_request(created_at);
CREATE INDEX idx_generation_request_user_id ON generation_request(user_id);
