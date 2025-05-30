INSERT INTO users (username, password, enabled) VALUES
('admin1', '$2a$10$tkCQcP0M/uTh7TIelJ1Fd.1X/TE0sJbUkpk7RPz8FBAinlPTR.wCq', true),
('user1', '$2a$10$tkCQcP0M/uTh7TIelJ1Fd.1X/TE0sJbUkpk7RPz8FBAinlPTR.wCq',  true);


INSERT INTO users_roles (users_id, roles_id) VALUES
(1, 1),
(2, 2)
;

