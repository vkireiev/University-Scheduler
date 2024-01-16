CREATE TABLE IF NOT EXISTS lectures (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    auditorium_id BIGINT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    course_id BIGINT NOT NULL,
    employee_id_as_lecturer BIGINT not null,
    lecture_date DATE NOT NULL,
    lecture_time_slot SMALLINT NOT NULL,
    CONSTRAINT lectures_auditorium_lecture_time_slot_lecture_date_unique UNIQUE (auditorium_id, lecture_time_slot, lecture_date),
    CONSTRAINT lectures_lecturer_lecture_date_lecture_time_slot_unique UNIQUE (employee_id_as_lecturer, lecture_date, lecture_time_slot),
    CONSTRAINT lectures_auditorium_id_fkey FOREIGN KEY(auditorium_id) REFERENCES auditoriums(id),
    CONSTRAINT lectures_course_id_fkey FOREIGN KEY(course_id) REFERENCES courses(id),
    CONSTRAINT lectures_employee_id_as_lecturer_fkey FOREIGN KEY(employee_id_as_lecturer) REFERENCES employees(id)
);
