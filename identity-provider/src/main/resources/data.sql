INSERT INTO app_authority (id, name) VALUES
                                         ('b9b13d69-93a9-4d8e-901d-1f24405e4081', 'AUTHORITY_USER_1'),
                                         ('c2d4b4d7-e8fc-4d4c-9f69-3f9a5bb8dbb0', 'AUTHORITY_USER_2'),
                                         ('84c7d39b-9b7c-4e89-89c7-75b7c7a41f21', 'AUTHORITY_ADMIN_1'),
                                         ('f69b1f17-59b3-43fb-9d23-06a307b98d65', 'AUTHORITY_ADMIN_2');

INSERT INTO app_user_role (id, name) VALUES
                                         ('4f7e6053-8334-4f0b-b3f9-5b53b8bc7016', 'USER'),
                                         ('2b36f17b-16be-481e-bfcf-69c5b58f02fc', 'ADMIN');

INSERT INTO role_authority (role_id, authority_id) VALUES
                                                       ('4f7e6053-8334-4f0b-b3f9-5b53b8bc7016', 'b9b13d69-93a9-4d8e-901d-1f24405e4081'), -- USER <== AUTHORITY_USER_1
                                                       ('4f7e6053-8334-4f0b-b3f9-5b53b8bc7016', 'c2d4b4d7-e8fc-4d4c-9f69-3f9a5bb8dbb0'), -- USER <== AUTHORITY_USER_2
                                                       ('2b36f17b-16be-481e-bfcf-69c5b58f02fc', '84c7d39b-9b7c-4e89-89c7-75b7c7a41f21'), -- ADMIN <== AUTHORITY_ADMIN_1
                                                       ('2b36f17b-16be-481e-bfcf-69c5b58f02fc', 'f69b1f17-59b3-43fb-9d23-06a307b98d65'); -- ADMIN <== AUTHORITY_ADMIN_2

INSERT INTO app_user (id, username, email, created_at, updated_at, is_active, is_email_verified) VALUES
    ('10a676a7-3e5e-4a1b-949f-74dbf6ecb9fd', 'test', 'osk4r.wolny@gmail.com', NOW(), NOW(), true, false);

INSERT INTO app_user_credentials (id, password_hash, created_at, updated_at, user_id) VALUES
    ('ee9f97e7-40a0-45c2-8b89-89b6d4bca456', '{hash}', NOW(), NOW(), '10a676a7-3e5e-4a1b-949f-74dbf6ecb9fd');

INSERT INTO user_role (user_id, role_id) VALUES
                                             ('10a676a7-3e5e-4a1b-949f-74dbf6ecb9fd', '4f7e6053-8334-4f0b-b3f9-5b53b8bc7016'),
                                             ('10a676a7-3e5e-4a1b-949f-74dbf6ecb9fd', '2b36f17b-16be-481e-bfcf-69c5b58f02fc');

INSERT INTO app_user_profile (id, avatar_url, gender, code, user_id) VALUES
    ('3f478963-2b2e-4563-981b-7f578b8e9e9b', 'https://www.google.com', 'MALE', 'PL', '10a676a7-3e5e-4a1b-949f-74dbf6ecb9fd');

INSERT INTO client (client_id_issued_at, client_secret_expires_at, authorization_grant_types, client_authentication_methods, redirect_uris, scopes, client_settings, token_settings, client_id, client_name, client_secret, id) VALUES (null, null, 'refresh_token,authorization_code', 'client_secret_basic', 'http://localhost:3000,https://oauth.pstmn.io/v1/callback', 'openid,profile,email', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",2592000.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}', 'client', '6a40d19e-83a4-45de-8184-5e390a02bcc2', 'secret', '6a40d19e-83a4-45de-8184-5e390a02bcc2');
INSERT INTO client (client_id_issued_at, client_secret_expires_at, authorization_grant_types, client_authentication_methods, redirect_uris, scopes, client_settings, token_settings, client_id, client_name, client_secret, id) VALUES (null, null, 'client_credentials', 'client_secret_basic', '', 'openid,profile,email', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",2592000.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}', 'user-service-client', 'user-service-client', 'secret', 'user-service-client');
