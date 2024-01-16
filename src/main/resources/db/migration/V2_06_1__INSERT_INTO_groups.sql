-- Groups
ALTER SEQUENCE IF EXISTS "groups_id_seq" RESTART WITH 1;
INSERT INTO public."groups" (id, capacity, "name") VALUES(1, 30, 'Defaults');
INSERT INTO public."groups" (id, capacity, "name") VALUES(2, 30, 'Group #2');
INSERT INTO public."groups" (id, capacity, "name") VALUES(3, 10, 'Group #3');
INSERT INTO public."groups" (id, capacity, "name") VALUES(4, 10, 'Group #4');
INSERT INTO public."groups" (id, capacity, "name") VALUES(5, 10, 'Group #5');
INSERT INTO public."groups" (id, capacity, "name") VALUES(6, 30, 'Group #6');
ALTER SEQUENCE IF EXISTS "groups_id_seq" RESTART WITH 101;
