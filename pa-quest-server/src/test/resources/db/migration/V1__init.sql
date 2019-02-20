create sequence hibernate_sequence start with 1 increment by 1

create table log_from_web (
    id bigint not null,
    entry_date timestamp,
    level integer,
    message varchar(255),
    primary key (id)
);

create table log_from_web_extra (
    log_id bigint not null,
    extra_info varchar(255)
);

create table roles (
    id bigint not null,
    name varchar(60),
    primary key (id)
);

create table user_roles (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

create table users (
    id bigint not null,
    email varchar(50),
    name varchar(50),
    password varchar(100),
    username varchar(50) not null,
    primary key (id)
);

alter table roles
   add constraint UK_roles_name unique (name);

alter table users
   add constraint UK_users_email unique (email);

alter table users
   add constraint UK_users_username unique (username);

alter table log_from_web_extra
   add constraint FK_log_from_web
   foreign key (log_id)
   references log_from_web;

alter table user_roles
   add constraint FK_roles
   foreign key (role_id)
   references roles;

alter table user_roles
   add constraint FK_users
   foreign key (user_id)
   references users;

INSERT INTO roles(id, name) VALUES(nextval('hibernate_sequence'),'ROLE_USER');
INSERT INTO roles(id, name) VALUES(nextval('hibernate_sequence'), 'ROLE_ADMIN');
