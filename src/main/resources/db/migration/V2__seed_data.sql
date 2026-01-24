INSERT INTO public.authorities (authority_name)
VALUES ('CREATE')
ON CONFLICT (authority_name) DO NOTHING;


INSERT INTO public.roles (role_name)
VALUES ('ADMIN')
ON CONFLICT (role_name) DO NOTHING;


INSERT INTO public.role_authorities (rid, aid)
SELECT
    (SELECT role_id FROM roles WHERE role_name = 'ADMIN'),
    (SELECT authority_id FROM authorities WHERE authority_name = 'CREATE')
WHERE NOT EXISTS (
    SELECT 1
    FROM role_authorities
    WHERE rid = (SELECT role_id FROM roles WHERE role_name = 'ADMIN')
      AND aid = (SELECT authority_id FROM authorities WHERE authority_name = 'CREATE')
);


INSERT INTO public.users (username, password, role_id)
SELECT
    'superuser',
    '$2a$10$/7KsUP2ZhkU5A/CrHSj4H.g2AHUOthearSQGFipDvxk9lu5CVvMNm',
    (SELECT role_id FROM roles WHERE role_name = 'ADMIN')
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'superuser'
);
