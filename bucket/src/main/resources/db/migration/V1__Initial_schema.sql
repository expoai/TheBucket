CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS apod (
                                    id SERIAL PRIMARY KEY,
                                    date VARCHAR(255),
    explanation TEXT,
    hdurl VARCHAR(255),
    media_type VARCHAR(255),
    service_version VARCHAR(255),
    title VARCHAR(255),
    url VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(100),
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

-- Le nom des rôles doit commencer par ROLE_ en Spring Security
INSERT INTO roles (name) VALUES
                             ('ROLE_ADMIN'),
                             ('ROLE_USER'),
                             ('ROLE_SCRAPPER');