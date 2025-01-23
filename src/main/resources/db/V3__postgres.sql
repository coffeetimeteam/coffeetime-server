CREATE TYPE login_type_enum AS ENUM ('EMAIL', 'SOCIAL');
CREATE TYPE role_enum AS ENUM ('GENERAL_USER', 'SPECIAL_USER');
CREATE TYPE location_type_enum AS ENUM ('HOME', 'OFFICE', 'FRANCHISE', 'LOCAL_CAFE');
CREATE TYPE coffee_type_enum AS ENUM ('COFFEE', 'NONE_COFFEE');
CREATE TYPE size_type_enum AS ENUM ('LARGE', 'MEDIUM', 'SMALL', 'ENOUGH');
CREATE TYPE taste_type_enum AS ENUM ('SOUR', 'NUTTY', 'DELICIOUS', 'DONT_LIKE');
CREATE TYPE price_type_enum AS ENUM ('CHEAP', 'AVERAGE', 'EXPENSIVE');
CREATE TYPE status_enum AS ENUM ('ACTIVE', 'REMOVED', 'FAILED');

CREATE TABLE IF NOT EXISTS "user"
(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(255)    NOT NULL UNIQUE,
    login_type      login_type_enum NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    nickname        VARCHAR(20)     NOT NULL UNIQUE,
    role            role_enum       NOT NULL,
    modified_at     TIMESTAMP(6),
    last_login_date TIMESTAMP(6),
    deleted_at      TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id         SERIAL PRIMARY KEY,
    token      VARCHAR(256) NOT NULL UNIQUE,
    user_id    BIGINT REFERENCES "user" (id) ON DELETE CASCADE,
    expired_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS coffee
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES "user" (id) ON DELETE CASCADE,
    remember_date DATE               NOT NULL,
    remember_time TIME               NOT NULL,
    location_type location_type_enum NOT NULL,
    coffee_type   coffee_type_enum   NOT NULL,
    size_type     size_type_enum     NOT NULL,
    taste_type    taste_type_enum    NOT NULL,
    price_type    price_type_enum    NOT NULL,
    coffee_score  INTEGER            NOT NULL CHECK (coffee_score >= 1 AND coffee_score <= 5)
);

CREATE TABLE IF NOT EXISTS image
(
    id         BIGSERIAL PRIMARY KEY,
    coffee_id  BIGINT       REFERENCES coffee (id) ON DELETE SET NULL,
    url        VARCHAR(384) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL,
    deleted_at TIMESTAMP(6),
    status     status_enum  NOT NULL
);
