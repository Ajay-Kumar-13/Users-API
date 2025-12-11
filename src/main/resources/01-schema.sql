create table public.users(
	id uuid primary key default gen_random_uuid(),
	username varchar(20),
	password varchar(255),
	role_id uuid,
	created_at timestamp default now()
);

create table public.roles(
	role_id uuid primary key default gen_random_uuid(),
	role_name varchar(20) not null unique
);

create table public.authorities(
	authority_id uuid primary key default gen_random_uuid(),
	authority_name varchar(10) not null  unique
);

create table public.role_authorities(
	rid uuid not null references public.roles(role_id) on delete cascade,
	aid uuid not null references public.authorities(authority_id) on delete cascade,
	primary key(rid, aid)
);

create table public.user_authorities(
	id uuid default gen_random_uuid(),
	user_id uuid not null references public.users(id) on delete cascade,
	authority_id uuid not null references public.authorities(authority_id) on delete cascade,
	active boolean not null,
	primary key(user_id, authority_id)
);