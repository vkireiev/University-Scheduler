CREATE TABLE IF NOT EXISTS groups_courses (
    group_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, course_id),
    CONSTRAINT groups_courses_group_id_fkey FOREIGN KEY(group_id) REFERENCES groups(id),
    CONSTRAINT groups_courses_course_id_fkey FOREIGN KEY(course_id) REFERENCES courses(id)
);
