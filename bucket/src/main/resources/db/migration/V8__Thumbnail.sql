ALTER TABLE student_upload
    ADD COLUMN thumbnail_url VARCHAR(255);

ALTER TABLE uploaded_image
    ADD COLUMN public_thumbnail_url VARCHAR(255);
