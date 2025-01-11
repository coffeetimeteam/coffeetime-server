CREATE TABLE IF NOT EXISTS user
(
    id              bigint       not null auto_increment primary key,
    username        varchar(255) not null unique,
    login_type      varchar(20)  not null,
    password        varchar(255) not null,
    nickname        varchar(20)  not null unique,
    role            enum ('GENERAL_USER', 'SPECIAL_USER'),
    modified_at     datetime(6),
    last_login_date datetime(6),
    deleted_at      datetime(6)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id         integer      not null auto_increment primary key,
    token      varchar(256) not null unique,
    user_id    bigint,
    expired_at datetime(6)  not null,
    constraint fk_refresh_token__member
        foreign key (user_id) references user (id)
);

