create table users
(
    id       bigserial    not null,
    login    varchar(255) not null unique,
    password varchar(255) not null,
    primary key (id)
);