INSERT INTO public.authorities (authority_name, authority_desc)
VALUES
    ('CREATE', 'Allows the user to create new records in the system.'),
    ('READ', 'Allows the user to read records in the system.'),
    ('UPDATE', 'Allows the user to update records in the system.'),
    ('DELETE', 'Allows the user to delete records in the system.'),

    ('USER_CREATE', 'Allows the user to create new users in the system.'),
    ('USER_READ', 'Allows the user to read users in the system.'),
    ('USER_UPDATE', 'Allows the user to update user records in the system.'),
    ('USER_DELETE', 'Allows the user to delete users in the system.'),

    ('ROLE_CREATE', 'Allows the user to create new roles in the system.'),
    ('ROLE_READ', 'Allows the user to read roles in the system.'),
    ('ROLE_UPDATE', 'Allows the user to update roles in the system.'),
    ('ROLE_DELETE', 'Allows the user to delete roles in the system.')
ON CONFLICT (authority_name) DO NOTHING;



INSERT INTO public.roles (role_name, role_desc)
VALUES ('ROOT', 'This role has all permissions and can perform any action in the system.')
ON CONFLICT (role_name) DO NOTHING;


INSERT INTO public.role_authorities (rid, aid)
SELECT
    (SELECT role_id FROM roles WHERE role_name = 'ROOT'),
    (SELECT authority_id FROM authorities WHERE authority_name = 'CREATE')
WHERE NOT EXISTS (
    SELECT 1
    FROM role_authorities
    WHERE rid = (SELECT role_id FROM roles WHERE role_name = 'ROOT')
      AND aid = (SELECT authority_id FROM authorities WHERE authority_name = 'CREATE')
);

INSERT INTO public.role_authorities (rid, aid)
SELECT
    (SELECT role_id FROM roles WHERE role_name = 'ROOT'),
    (SELECT authority_id FROM authorities WHERE authority_name = 'READ')
WHERE NOT EXISTS (
    SELECT 1
    FROM role_authorities
    WHERE rid = (SELECT role_id FROM roles WHERE role_name = 'ROOT')
      AND aid = (SELECT authority_id FROM authorities WHERE authority_name = 'READ')
);


INSERT INTO public.role_authorities (rid, aid)
SELECT
    (SELECT role_id FROM roles WHERE role_name = 'ROOT'),
    (SELECT authority_id FROM authorities WHERE authority_name = 'UPDATE')
WHERE NOT EXISTS (
    SELECT 1
    FROM role_authorities
    WHERE rid = (SELECT role_id FROM roles WHERE role_name = 'ROOT')
      AND aid = (SELECT authority_id FROM authorities WHERE authority_name = 'UPDATE')
);

INSERT INTO public.role_authorities (rid, aid)
SELECT
    (SELECT role_id FROM roles WHERE role_name = 'ROOT'),
    (SELECT authority_id FROM authorities WHERE authority_name = 'DELETE')
WHERE NOT EXISTS (
    SELECT 1
    FROM role_authorities
    WHERE rid = (SELECT role_id FROM roles WHERE role_name = 'ROOT')
      AND aid = (SELECT authority_id FROM authorities WHERE authority_name = 'DELETE')
);



INSERT INTO public.users (username, email, password, role_id)
SELECT
    'superuser',
    'superuser@gmail.com',
    '$2a$10$/7KsUP2ZhkU5A/CrHSj4H.g2AHUOthearSQGFipDvxk9lu5CVvMNm',
    (SELECT role_id FROM roles WHERE role_name = 'ROOT')
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'superuser'
);
