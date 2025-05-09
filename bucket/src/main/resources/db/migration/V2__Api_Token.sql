INSERT INTO roles (name) VALUES ('ROLE_API');

CREATE TABLE api_token (
                           id BIGSERIAL PRIMARY KEY,
                           token VARCHAR(255) NOT NULL UNIQUE,
                           revoked BOOLEAN NOT NULL DEFAULT FALSE,
                           user_id BIGINT NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_api_token_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE
);
