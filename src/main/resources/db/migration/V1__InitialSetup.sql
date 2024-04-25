create table users
(
    id         bigserial primary key,
    first_name varchar(20) not null,
    last_name  varchar(20) not null,
    email      varchar(70) not null,
    birth_date date        not null,
    address    varchar(50),
    phone      varchar(30)
);