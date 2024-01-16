-- Auditoriums
ALTER SEQUENCE IF EXISTS "auditoriums_id_seq" RESTART WITH 1;
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(1, true, 75, '101');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(2, true, 50, '102');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(3, true, 75, '103');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(4, true, 50, '104');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(5, true, 50, '105');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(6, true, 75, '201');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(7, true, 25, '202');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(8, true, 25, '203');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(9, true, 25, '204');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(10, true, 50, '205');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(11, true, 75, '206');
INSERT INTO public."auditoriums" (id, available, capacity, "number") VALUES(12, true, 75, '207');
ALTER SEQUENCE IF EXISTS "auditoriums_id_seq" RESTART WITH 101;
