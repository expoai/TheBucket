ALTER TABLE uploaded_image
    ADD COLUMN publicid UUID UNIQUE;

CREATE INDEX idx_uploaded_image_publicid ON uploaded_image(publicid);