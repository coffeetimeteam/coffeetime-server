CREATE TABLE IF NOT EXISTS user
(
    id              bigint                                not null auto_increment primary key,
    username        varchar(255)                          not null unique,
    login_type      enum ('EMAIL', 'SOCIAL')              not null,
    password        varchar(255)                          not null,
    nickname        varchar(20)                           not null unique,
    role            enum ('GENERAL_USER', 'SPECIAL_USER') not null,
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
    constraint fk_refresh_token__user
        foreign key (user_id) references user (id)
);

CREATE TABLE IF NOT EXISTS coffee
(
    id            bigint                                             not null auto_increment primary key,
    user_id       bigint,
    remember_date date                                               not null,
    remember_time time                                               not null,
    location_type enum ('HOME', 'OFFICE', 'FRANCHISE', 'LOCAL_CAFE') not null,
    coffee_type   enum ('COFFEE', 'NONE_COFFEE')                     not null,
    size_type     enum ('LARGE', 'MEDIUM', 'SMALL', 'ENOUGH')        not null,
    taste_type    enum ('SOUR', 'NUTTY', 'DELICIOUS', 'DONT_LIKE')   not null,
    price_type    enum ('CHEAP', 'AVERAGE', 'EXPENSIVE')             not null,
    coffee_score  integer                                            not null check (coffee_score >= 1 and coffee_score <= 5),
    constraint fk_coffee__user
        foreign key (user_id) references user (id)
);

CREATE TABLE IF NOT EXISTS image
(
    id         bigint                     not null auto_increment primary key,
    coffee_id  bigint                     null,
    url        varchar(384)               not null unique,
    created_at datetime(6)                not null,
    deleted_at datetime(6)                null,
    status     enum ('USABLE', 'DELETED') not null,
    constraint fk_image__coffee
        foreign key (coffee_id) references coffee (id)
);

