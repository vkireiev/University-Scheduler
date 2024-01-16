CREATE TABLE IF NOT EXISTS employees_courses (
    employee_id_as_lecturer BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY (employee_id_as_lecturer, course_id),
    CONSTRAINT employees_courses_employee_id_as_lecturer_fkey FOREIGN KEY(employee_id_as_lecturer) REFERENCES employees(id),
    CONSTRAINT employees_courses_course_id_fkey FOREIGN KEY(course_id) REFERENCES courses(id)
);
