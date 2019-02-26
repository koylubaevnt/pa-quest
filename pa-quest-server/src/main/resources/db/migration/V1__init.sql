create sequence hibernate_sequence start with 1 increment by 1;

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
    text varchar(60),
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



SET @v_role_admin_id = nextval('hibernate_sequence');
INSERT INTO roles(id, name, text) VALUES(@v_role_admin_id, 'ROLE_ADMIN', 'Администратор');
INSERT INTO roles(id, name, text) VALUES(nextval('hibernate_sequence'), 'ROLE_USER', 'Пользователь');


SET @v_user_admin_id = nextval('hibernate_sequence');
INSERT INTO users(id, email, name, password, username)
VALUES(@v_user_admin_id, 'admin@mail.ru', 'admin', '$2a$04$zfIHQdNS3R0qvDtCK9DylOp2VfgnXDE0DBahjlANa5O4Y9n35gXfO', 'admin');

INSERT INTO user_roles(user_id, role_id) VALUES(@v_user_admin_id, @v_role_admin_id);

create sequence answer_sequence start with 1 increment by 1;
create sequence question_sequence start with 1 increment by 1;
create sequence content_sequence start with 1 increment by 1;
create sequence user_question_sequence start with 1 increment by 1;
create sequence user_quest_sequence start with 1 increment by 1;
create sequence congratulation_sequence start with 1 increment by 1;

create table content (
    id bigint not null,
    content blob,
    type varchar(60),
    extension varchar(10) not null,
    name varchar(1024) not null,
    primary key (id)
);

create table answer (
    id bigint not null,
    text varchar(100) not null ,
    primary key (id)
);

alter table answer
   add constraint UK_answer_text unique (text);

create table question (
    id bigint not null,
    text varchar(255) not null,
    youtube_video_url varchar(1024) not null,
    content_id bigint,
    correct_answer_id bigint not null,
    primary key (id)
);
alter table question
   add constraint FK_question_content
   foreign key (content_id)
   references content;

alter table question
   add constraint FK_question_answer
   foreign key (correct_answer_id)
   references answer;

create table question_answer (
    question_id bigint not null,
    answer_id bigint not null,
    primary key (question_id, answer_id)
);

create table user_quest (
    id bigint not null,
    active boolean default true,
    user_id bigint not null,
    start timestamp,
    finish timestamp,
    primary key (id)

);
alter table user_quest
   add constraint FK_user_quest_user
   foreign key (user_id)
   references users;

create table user_question (
    id bigint not null,
    user_quest_id bigint not null,
    question_id bigint not null,
    count_attempts int not null default 0,
    is_answered boolean not null default false,
    seq int not null default 0,
    start timestamp,
    finish timestamp,
    primary key (id)
);
alter table user_question
   add constraint FK_user_question_user_quest
   foreign key (user_quest_id)
   references user_quest;
alter table user_question
   add constraint FK_user_question_user_question
   foreign key (question_id)
   references question;

create table result (
    id bigint not null,
    user_id bigint not null,
    total_question bigint not null default 0,
    correct_answer_count bigint not null default 0,
    correct_answer_percentage decimal not null default 0,
    primary key (id)
);
alter table result
   add constraint FK_result_user
   foreign key (user_id)
   references users;

create table congratulation(
    id bigint not null,
    video_id varchar(100) not null,
    primary key(id)
);


SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_correct_answer_id, 'Памятник 100 летию Финской демократии');
insert into answer (id, text) values(@v_answer_1_id, 'Памятник Финляндии');
insert into answer (id, text) values(@v_answer_2_id, 'Памятник новаторским идеям');
insert into answer (id, text) values(@v_answer_3_id, 'Памятник памятнику');

SET @v_content_file_id = nextval('content_sequence');
SET @v_content_link_id = nextval('content_sequence');
insert into content(id, name, extension, type, content) values (@v_content_file_id, 'Памятник загадка', 'mp4', 'FILE', '1234');
insert into content(id, name, extension, type, content) values (@v_content_link_id, 'https://wwww/youtube.com/watch?v=HBiPThwCQOM&index=5&list=PL5r1HBOJCfZbr6nEDg6zQZx1pLkqLq4Be', 'mp4', 'LINK', null);


SET @v_question_1_id = nextval('question_sequence');
SET @v_question_2_id = nextval('question_sequence');
SET @v_question_3_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_1_id, 'Что это за памятник?', @v_content_file_id, @v_correct_answer_id, 'HBiPThwCQOM');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_2_id, 'Что это за памятник?', @v_content_link_id, @v_correct_answer_id, '7OoCjdtX5D8');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_3_id, 'Что это за памятник? Ответов меньше 4', @v_content_link_id, @v_answer_2_id, 'OoS6dY5fa60');

insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_3_id);

insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_3_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_correct_answer_id);

insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_3_id);


insert into congratulation(id, video_id) values(nextval('congratulation_sequence'), '2YHpZbsUgK4');