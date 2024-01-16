CREATE TABLE IF NOT EXISTS auditoriums (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    capacity int2 NOT NULL CHECK (capacity<=300 AND capacity>=10),
    available BOOLEAN NOT NULL,
    CONSTRAINT auditoriums_number_available_unique UNIQUE (number, available)
);
