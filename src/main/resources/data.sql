INSERT INTO roles (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

INSERT INTO users (username, password, is_enabled)
VALUES
    ('admin', '$2a$10$FCiUfA5GmapizwvLSucRZepo9tBw9O24fmA5E1xHiyHo5yMhNOVhq', true);

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (1, 2);