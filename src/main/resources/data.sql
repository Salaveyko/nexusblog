INSERT INTO roles (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

--login:admin, password:admin
INSERT INTO users (username, password, is_enabled, account_non_expired,
                   account_non_locked, credentials_non_expired)
VALUES
    ('Admin', '$2a$10$oGkZWssxBaLl.S.ROgp82OwQ/9LVE0.Ju1ZMXYNbSigUu/yT1tr/y',
     true, true, true, true);

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (1, 2);

INSERT INTO addresses(country)
VALUES ('Ukraine');

INSERT INTO user_contacts(email, phone)
VALUES ('admin@mail.com', "+380988888888");

INSERT INTO profiles(name, users_id, contact_id, address_id)
VALUES ('admin', 1, 1, 1);

