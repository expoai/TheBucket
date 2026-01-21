-- Migration for Task table
-- Creates the task table for task management system

CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(50) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    assigned_to INT,
    created_by INT NOT NULL,
    due_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    completed_at TIMESTAMP,

    FOREIGN KEY (assigned_to) REFERENCES users(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Create indexes for better query performance
CREATE INDEX idx_task_status ON task(status);
CREATE INDEX idx_task_priority ON task(priority);
CREATE INDEX idx_task_assigned_to ON task(assigned_to);
CREATE INDEX idx_task_created_by ON task(created_by);
CREATE INDEX idx_task_due_date ON task(due_date);
CREATE INDEX idx_task_created_at ON task(created_at);
