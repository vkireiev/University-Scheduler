CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    capacity int2 NOT NULL CHECK (capacity>=1 AND capacity<=30)
);
