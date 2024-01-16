CREATE TABLE IF NOT EXISTS persons (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(75) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    register_date timestamp NOT NULL,
    locked boolean NOT NULL,
    user_type INTEGER NOT NULL,
    CONSTRAINT persons_username_unique UNIQUE (username)
);
