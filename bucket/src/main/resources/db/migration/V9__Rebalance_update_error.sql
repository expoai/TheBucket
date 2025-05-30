ALTER TABLE uploaded_image
    ADD COLUMN visibility SMALLINT;

ALTER TABLE uploaded_image
    ADD COLUMN public_url VARCHAR(255);