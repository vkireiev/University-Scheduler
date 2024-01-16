CREATE TABLE IF NOT EXISTS lectures_groups (
    lectures_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    PRIMARY KEY (lectures_id, group_id),
    CONSTRAINT lectures_groups_lectures_id_fkey FOREIGN KEY(lectures_id) REFERENCES lectures(id),
    CONSTRAINT lectures_groups_group_id_fkey FOREIGN KEY(group_id) REFERENCES groups(id)
);
