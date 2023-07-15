create table t_account
(
    id       int(11) auto_increment primary key,
    username varchar(255)                not null,
    password varchar(255)                not null,
    email    varchar(255),
    roles    varchar(255) default 'USER' not null,
);

insert into t_account (username, password, email, roles)
values ('admin', '$2a$10$wb5k5Hrc6rkJ/azEldiTm.xJcpMCDZJ6/e2qF/kSiVSs8rO9bvzh6', null, 'USER,ADMIN');
