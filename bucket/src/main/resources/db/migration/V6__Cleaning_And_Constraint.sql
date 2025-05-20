ALTER TABLE users
ADD CONSTRAINT uq_username UNIQUE (username);

ALTER TABLE users
DROP COLUMN IF EXISTS claim_token;

ALTER TABLE users
DROP COLUMN IF EXISTS token_expiration;
