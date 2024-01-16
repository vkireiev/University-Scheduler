-- Lectures
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(1, '2023-05-01', 'Lecture #1', 1, 3, 1, 2);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(2, '2023-05-01', 'Lecture #2', 2, 9, 2, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(3, '2023-05-01', 'Lecture #3', 3, 8, 2, 5);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(4, '2023-05-02', 'Lecture #4', 3, 11, 1, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(5, '2023-05-02', 'Lecture #5', 4, 3, 2, 2);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(6, '2023-05-02', 'Lecture #6', 1, 7, 2, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(7, '2023-05-03', 'Lecture #7', 1, 7, 1, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(8, '2023-05-03', 'Lecture #8', 2, 3, 1, 2);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(9, '2023-05-03', 'Lecture #9', 3, 8, 2, 5);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(10, '2023-05-03', 'Lecture #10', 4, 3, 2, 2);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(11, '2023-05-04', 'Lecture #11', 1, 9, 2, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(12, '2023-05-04', 'Lecture #12', 2, 8, 2, 5);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(13, '2023-05-04', 'Lecture #13', 3, 5, 3, 3);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(14, '2023-05-05', 'Lecture #14', 3, 7, 1, 4);
INSERT INTO public."lectures" (id, lecture_date, subject, auditorium_id, course_id, lecture_time_slot, employee_id_as_lecturer) VALUES(15, '2023-05-05', 'Lecture #15', 4, 3, 2, 2);
ALTER SEQUENCE IF EXISTS "lectures_id_seq" RESTART WITH 101;
