INSERT INTO public.authorities (authority_name) VALUES ('CREATE');

INSERT INTO public.roles (role_name) VALUES ('ADMIN');

INSERT INTO public.role_authorities (rid, aid) VALUES (
    (SELECT role_id FROM roles WHERE role_name='ADMIN'),
    (SELECT authority_id FROM authorities WHERE authority_name='CREATE')
);

INSERT INTO public.users (username, password, role_id)
VALUES ('superuser', '$2a$10$/7KsUP2ZhkU5A/CrHSj4H.g2AHUOthearSQGFipDvxk9lu5CVvMNm', (SELECT role_id FROM roles WHERE role_name='ADMIN'));
