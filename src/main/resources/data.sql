INSERT INTO roles (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

--login:admin, password:admin
INSERT INTO users (username, password, is_enabled)
VALUES
    ('Admin', '$2a$10$oGkZWssxBaLl.S.ROgp82OwQ/9LVE0.Ju1ZMXYNbSigUu/yT1tr/y', true);

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (1, 2);

INSERT INTO profiles(name, users_id)
VALUES ('admin', 1)