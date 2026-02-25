INSERT INTO public.authorities (authority_name)
VALUES
    ('CREATE'),
    ('READ'),
    ('UPDATE'),
    ('DELETE'),

    ('USER_CREATE'),
    ('USER_READ'),
    ('USER_UPDATE'),
    ('USER_DELETE'),

    ('ROLE_CREATE'),
    ('ROLE_READ'),
    ('ROLE_UPDATE'),
    ('ROLE_DELETE')
ON CONFLICT (authority_name) DO NOTHING;



INSERT INTO public.roles (role_name)
VALUES ('ROOT')
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
