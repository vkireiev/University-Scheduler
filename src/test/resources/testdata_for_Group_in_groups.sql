-- Groups
ALTER SEQUENCE IF EXISTS "groups_id_seq" RESTART WITH 1;
INSERT INTO public."groups" (id, capacity, "name") VALUES(1, 10, 'Group #1');
INSERT INTO public."groups" (id, capacity, "name") VALUES(2, 10, 'Group #2');
INSERT INTO public."groups" (id, capacity, "name") VALUES(3, 10, 'Group #3');
INSERT INTO public."groups" (id, capacity, "name") VALUES(4, 10, 'Group #4');
INSERT INTO public."groups" (id, capacity, "name") VALUES(5, 10, 'Group #5');
INSERT INTO public."groups" (id, capacity, "name") VALUES(6, 5, 'Group #6');
INSERT INTO public."groups" (id, capacity, "name") VALUES(7, 17, 'Group #7');
INSERT INTO public."groups" (id, capacity, "name") VALUES(8, 18, 'Group #8');
ALTER SEQUENCE IF EXISTS "groups_id_seq" RESTART WITH 101;
