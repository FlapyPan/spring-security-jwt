create table public.t_account
(
    id         serial
        constraint t_account_pk primary key,
    username   varchar(255)                           not null,
    password   varchar(255)                           not null,
    email      varchar(255),
    roles      varchar(255) default 'USER'            not null,
    locked     boolean      default false             not null,
    enabled    boolean      default false             not null,
    created_at timestamp    default CURRENT_TIMESTAMP not null
);

insert into public.t_account (username, password, email, roles, locked, enabled)
values ('admin', '$2a$10$wb5k5Hrc6rkJ/azEldiTm.xJcpMCDZJ6/e2qF/kSiVSs8rO9bvzh6', null, 'USER,ADMIN', false, true);
