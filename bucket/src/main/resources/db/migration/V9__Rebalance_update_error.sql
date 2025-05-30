ALTER TABLE uploaded_image
    ADD COLUMN visibility BOOLEAN DEFAULT FALSE;

ALTER TABLE uploaded_image
    ADD COLUMN public_url VARCHAR(255);