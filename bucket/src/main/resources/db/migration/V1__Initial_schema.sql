-- Initial database schema for Bucket service
-- Creates users, roles, and authentication tables

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN
);

CREATE TABLE IF NOT EXISTS users_roles (
    users_id INT,
    roles_id INT,
    PRIMARY KEY (users_id, roles_id),
    FOREIGN KEY (users_id) REFERENCES users(id),
    FOREIGN KEY (roles_id) REFERENCES roles(id)
);

-- Insert default roles
INSERT INTO roles (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER'),
    ('ROLE_SCRAPPER'),
    ('ROLE_API');
