-- Auditoriums
ALTER SEQUENCE IF EXISTS "auditoriums_id_seq" RESTART WITH 1;
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(1, true, 75, 'A101');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(2, true, 50, 'A102');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(3, false, 75, 'B103-test');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(4, true, 50, 'B104');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(5, true, 50, 'B105');
ALTER SEQUENCE IF EXISTS "auditoriums_id_seq" RESTART WITH 101;
