create table t_account
(
    id       int(11) auto_increment primary key,
    username varchar(255)                not null,
    password varchar(255)                not null,
    email    varchar(255),
    roles    varchar(255) default 'USER' not null,
    locked   tinyint(1)   default 0      not null,
    enabled  tinyint(1)   default 0      not null
);

insert into t_account (username, password, email, roles, locked, enabled)
values ('admin', '$2a$10$wb5k5Hrc6rkJ/azEldiTm.xJcpMCDZJ6/e2qF/kSiVSs8rO9bvzh6', null, 'USER,ADMIN', 0, 1);
