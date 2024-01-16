CREATE TABLE IF NOT EXISTS students (
    id BIGINT NOT NULL PRIMARY KEY,
    group_id BIGINT,
    CONSTRAINT students_id_fkey FOREIGN KEY(id) REFERENCES persons(id),
    CONSTRAINT students_group_id_fkey FOREIGN KEY(group_id) REFERENCES groups(id)
);
