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
SET @v_role_user_id = nextval('hibernate_sequence');
INSERT INTO roles(id, name, text) VALUES(@v_role_admin_id, 'ROLE_ADMIN', 'Администратор');
INSERT INTO roles(id, name, text) VALUES(@v_role_user_id, 'ROLE_USER', 'Пользователь');


SET @v_user_admin_id = nextval('hibernate_sequence');
INSERT INTO users(id, email, name, password, username)
VALUES(@v_user_admin_id, 'koily@mail.ru', 'Койлубаев Никита Тахирович', '$2a$04$zfIHQdNS3R0qvDtCK9DylOp2VfgnXDE0DBahjlANa5O4Y9n35gXfO', 'koylubaevnt');
INSERT INTO user_roles(user_id, role_id) VALUES(@v_user_admin_id, @v_role_user_id);
INSERT INTO user_roles(user_id, role_id) VALUES(@v_user_admin_id, @v_role_admin_id);


SET @v_user_admin_id = nextval('hibernate_sequence');
INSERT INTO users(id, email, name, password, username)
VALUES(@v_user_admin_id, 'KamenskikhYuV@iris-retail.ru', 'Каменских Юра Висторович', '$2a$04$zfIHQdNS3R0qvDtCK9DylOp2VfgnXDE0DBahjlANa5O4Y9n35gXfO', 'kamenskikhyv');
INSERT INTO user_roles(user_id, role_id) VALUES(@v_user_admin_id, @v_role_user_id);
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

insert into congratulation(id, video_id) values(nextval('congratulation_sequence'), 'YFhcOqw2fj0');

insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'MoiseevaNA@iris-retail.ru','Моисеева Нина Александровна','MoiseevaNA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'chajnikovami@iris-retail.ru','Чайникова Мария Игоревна','chajnikovami','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'GrezinaOS@iris-retail.ru','Грезина Олеся Сергеевна','GrezinaOS','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'DemenevaNN@iris-retail.ru','Деменева Надежда Николаевна','DemenevaNN','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'EpishinaEV@iris-retail.ru','Епишина Елена Владимировна','EpishinaEV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KanisevaIA@iris-retail.ru','Канисева Инна Андреевна','KanisevaIA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'VdovinaEV@iris-retail.ru','Вдовина Елена Викторовна','VdovinaEV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'BogdanovaNP@iris-retail.ru','Богданова Наталия Павловна','BogdanovaNP','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'giljashevasb@iris-retail.ru','Гиляшева Светлана Борисовна','giljashevasb','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KrivosheevaMN@iris-retail.ru','Кривошеева Мария Николаевна','KrivosheevaMN','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'ShilovaLN@iris-retail.ru','Шилова Лариса Николаевна','ShilovaLN','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'ShirevaTV@iris-retail.ru','Ширева Татьяна Викторовна','ShirevaTV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'navodnichajasv@iris-retail.ru','Наводничая Светлана Викторовна','navodnichajasv','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'BelyshevaEI@iris-retail.ru','Белышева Елена Игоревна','BelyshevaEI','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'GilevaIS@iris-retail.ru','Гилева Ирина Сергеевна','GilevaIS','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'ElshinaNL@iris-retail.ru','Ельшина Нелли Леонидовна','ElshinaNL','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'TyapuginaYuYu@iris-retail.ru','Тяпугина Юлия Юрьевна','TyapuginaYuYu','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'kislingov@iris-retail.ru','Кислинг Ольга Валерьевна','kislingov','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'SuneginaEA@iris-retail.ru','Сунегина Екатерина Анатольевна','SuneginaEA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'MakukhaSA@iris-retail.ru','Макуха Светлана Александровна','MakukhaSA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'ZemtsovaON@iris-retail.ru','Земцова Ольга Николаевна','ZemtsovaON','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'VladimirovaNV@iris-retail.ru','Владимирова Наталья Викторовна','VladimirovaNV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'TrushnikovaYuO@iris-retail.ru','Трушникова Юлия Олеговна','TrushnikovaYuO','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'NetsvetaevaVYu@iris-retail.ru','Нецветаева Витта Юрьевна','NetsvetaevaVYu','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'NovokreshchennykhEG@iris-retail.ru','Новокрещенных Екатерина Геннадьевна','NovokreshchennykhEG','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'riabininaia@iris-retail.ru','Рябинина Ирина Александровна','riabininaia','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'LazarevichSK@iris-retail.ru','Лазаревич София Константиновна','LazarevichSK','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KuchumovaET@iris-retail.ru','Кучумова Евгения Таифовна','KuchumovaET','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'GovorukhinaNV@iris-retail.ru','Говорухина Наталья Васильевна','GovorukhinaNV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'TrofimovaMM@iris-retail.ru','Трофимова Марина Михайловна','TrofimovaMM','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'NovoselitskayaEG@iris-retail.ru','Новоселицкая Елена Геннадьевна','NovoselitskayaEG','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'YakubovichYuM@iris-retail.ru','Якубович Юлия Марковна','YakubovichYuM','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'MashkovtsevaEA@iris-retail.ru','Машковцева Екатерина Александровна','MashkovtsevaEA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'GurevaSV@iris-retail.ru','Гурьева Светлана Владимировна','GurevaSV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KalininaAV@iris-retail.ru','Калинина Анна Валерьевна','KalininaAV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KrasilnikovaAA@iris-retail.ru','Красильникова Анастасия Андреевна','KrasilnikovaAA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'PetrovaGN@iris-retail.ru','Петрова Галина Николаевна','PetrovaGN','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'ZhigalovaEA@iris-retail.ru','Жигалова Екатерина Алексеевна','ZhigalovaEA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'SmaznovaNV@iris-retail.ru','Смазнова Наталья Валерьевна','SmaznovaNV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'KolesnikovaMA@iris-retail.ru','Колесникова Марина Алексеевна','KolesnikovaMA','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'VoroshninaEV@iris-retail.ru','Ворошнина Елена Витальевна','VoroshninaEV','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'rusakevicheyu@iris-retail.ru','Русакевич Екатерина Юрьевна','rusakevicheyu','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'DubrovskikhEG@iris-retail.ru','Дубровских Екатерина Геннадьевна','DubrovskikhEG','password');
insert into users(id, email, name, username, password) values(nextval('hibernate_sequence'),'zakirova_elza@mail.ru','Койлубаева Эльза Маратовна','KoylubaevaEM','password');


SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_correct_answer_id, 'Какие деликатесы вы знаете?');
insert into answer (id, text) values(@v_answer_1_id, 'Чем кормят депутатов государственной думы?');
insert into answer (id, text) values(@v_answer_2_id, 'Какое самое популярное блюда на новогоднем столе?');
insert into answer (id, text) values(@v_answer_3_id, 'Что добывают в городе Норильск?');
SET @v_question_1_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_1_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'HFIu3C8xDLM');
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_1_id, @v_answer_3_id);



SET @v_answer_1_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Где снимали фильм Гарри Поттер?');
insert into answer (id, text) values(@v_correct_answer_id, 'Какие самые лучшие университеты мира вы знаете?');
insert into answer (id, text) values(@v_answer_2_id, 'Где учился Илон Маск?');
insert into answer (id, text) values(@v_answer_3_id, 'Какие университеты входят в "Лигу Плюща"?');
SET @v_question_2_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_2_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, '6kONI-49bjo');
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_2_id, @v_answer_3_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Почему вы любите ходить в театр?');
insert into answer (id, text) values(@v_answer_2_id, 'Почему вы любите походы в ресторан?');
insert into answer (id, text) values(@v_answer_3_id, 'Почему вам нравиться праздновать Новый год?');
insert into answer (id, text) values(@v_correct_answer_id, 'Почему вам нравиться ходить в отпуск?');
SET @v_question_3_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_3_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'XhfeAuaHmkA');
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_3_id, @v_answer_3_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Что необычного на спор делали ваши друзья?');
insert into answer (id, text) values(@v_correct_answer_id, 'Назовите необычные индивидуальные рекорды книги Гиннеса?');
insert into answer (id, text) values(@v_answer_2_id, 'Какие суперспособности есть у супергероев?');
insert into answer (id, text) values(@v_answer_3_id, 'Чем вас удивляли ваши коллеги?');
SET @v_question_4_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_4_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'TNqu7fVgTbw');
insert into question_answer(question_id, answer_id) values(@v_question_4_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_4_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_4_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_4_id, @v_answer_3_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Какие профессии более всего востребованы в Китае?');
insert into answer (id, text) values(@v_answer_2_id, 'Работников какой професии нет в компании ИРИС?');
insert into answer (id, text) values(@v_answer_3_id, 'Кем хочет стать ваш ребенок?');
insert into answer (id, text) values(@v_correct_answer_id, 'Какую самую необычную профессию вы знаете?');
SET @v_question_5_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_5_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, '4eCFWQH1-EU');
insert into question_answer(question_id, answer_id) values(@v_question_5_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_5_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_5_id, @v_answer_3_id);
insert into question_answer(question_id, answer_id) values(@v_question_5_id, @v_correct_answer_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Какие черты характера вам нравятся в мужчинах?');
insert into answer (id, text) values(@v_correct_answer_id, 'Опишите в 3-х словах Николая Валуева/Владимира Путина/Ален Делона?');
insert into answer (id, text) values(@v_answer_2_id, 'Опишите в 3-х словах Барака Обаму/Николя Саркози?');
insert into answer (id, text) values(@v_answer_3_id, 'Какими навыками должен обладать чемпион восточных единоборств?');
SET @v_question_6_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_6_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'rWLYxLF9cvo');
insert into question_answer(question_id, answer_id) values(@v_question_6_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_6_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_6_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_6_id, @v_answer_3_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'С какими лозунгами проводят первомайские демонстрации?');
insert into answer (id, text) values(@v_answer_2_id, 'Какой слоган предлагаете использовать на логотипе компании ИРИС?');
insert into answer (id, text) values(@v_correct_answer_id, 'Назовите самый известный лозунг в истории?');
insert into answer (id, text) values(@v_answer_3_id, 'Что вы говорите себе, чтобы повысить работоспособность?');
SET @v_question_7_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_7_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'tuqhEJCAOz8');
insert into question_answer(question_id, answer_id) values(@v_question_7_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_7_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_7_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_7_id, @v_answer_3_id);

SET @v_answer_1_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'Сколько дел осталось дома не сделано?');
insert into answer (id, text) values(@v_correct_answer_id, 'Как рыбаки хвастаются размером улова, размером глаз у рыбы?');
insert into answer (id, text) values(@v_answer_2_id, 'Какого размера буквы вы видите при осмотре у окулиста, что одеваете чтобы видеть лучше?');
insert into answer (id, text) values(@v_answer_3_id, 'Размер вашего монитора на рабочем месте?');
SET @v_question_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'zOawlVkEN6Y');
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_correct_answer_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_3_id);



SET @v_answer_1_id = nextval('answer_sequence');
SET @v_answer_2_id = nextval('answer_sequence');
SET @v_answer_3_id = nextval('answer_sequence');
SET @v_correct_answer_id = nextval('answer_sequence');
insert into answer (id, text) values(@v_answer_1_id, 'В автобусе к вам подошёл кондуктор/водитель и попросил оплатить проезд, ваши действия?');
insert into answer (id, text) values(@v_answer_2_id, 'В подземном переходе вам нравиться как играет музыкант, ваши действия?');
insert into answer (id, text) values(@v_answer_3_id, 'Вы встретились с одноклассником которого давно не видели, что бы вы вместе стали делать?');
insert into answer (id, text) values(@v_correct_answer_id, 'На улице встретили котенка/грабителя, ваши действия?');

SET @v_question_id = nextval('question_sequence');
insert into question(id, text, content_id, correct_answer_id, youtube_video_url) values(@v_question_id, 'Что на самом деле спрашивал ведущий задававший вопросы?', null, @v_correct_answer_id, 'Xh3MhX0cqpg');
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_2_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_3_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_answer_1_id);
insert into question_answer(question_id, answer_id) values(@v_question_id, @v_correct_answer_id);

