ALTER TABLE student_upload
ADD CONSTRAINT uq_teamid_idexterne UNIQUE (team_id, id_externe);
