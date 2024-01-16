CREATE TABLE IF NOT EXISTS employees (
    id BIGINT NOT NULL PRIMARY KEY,
    employee_type SMALLINT NOT NULL,
    CONSTRAINT employees_id_fkey FOREIGN KEY(id) REFERENCES persons(id)
);
