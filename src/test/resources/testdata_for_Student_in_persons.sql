-- Students
ALTER SEQUENCE IF EXISTS "persons_id_seq" RESTART WITH 51;
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(51, '2023-07-18', 'Martinez.Daniel@gmain.com', 'Daniel', 'Martinez', false, 'S7HF02ZBYIB3QA7DY549L6O17ND3TY77', 'phone-4724186297', '2023-01-01 00:00:00.000', 'MartinezDanielStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(52, '2023-07-18', 'Garcia.Noah@gmain.com', 'Noah', 'Garcia', false, 'J1UEQ58SHF7OHK64PYWDFCUZ1SUVMHRN', 'phone-1659893076', '2023-01-01 00:00:00.000', 'GarciaNoahStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(53, '2023-07-18', 'Thompson.Mia@gmain.com', 'Mia', 'Thompson', false, 'EFZBMHI0J9UJQQUKKJIZWTVAR86RRF7O', 'phone-9692719092', '2023-01-01 00:00:00.000', 'ThompsonMiaStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(54, '2023-07-18', 'Hall.Sophia@gmain.com', 'Sophia', 'Hall', false, '9DOK8YFRQZJMDE1O6E08GLCU157F1TKP', 'phone-5319360589', '2023-01-01 00:00:00.000', 'HallSophiaStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(55, '2023-07-18', 'Lee.Joseph@gmain.com', 'Joseph', 'Lee', true, '3NVMQYPTVW2E6JJB8C6WS1XSEC7BW8TQ', 'phone-2141261178', '2023-01-01 00:00:00.000', 'LeeJosephStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(56, '2023-07-18', 'Hernandez.Emily@gmain.com', 'Emily', 'Hernandez', false, 'R9ZU3IUMS9EIQ1IOO709X8DD7KK1WIAN', 'phone-6219916736', '2023-01-01 00:00:00.000', 'HernandezEmilyStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(57, '2023-07-18', 'Lee.James@gmain.com', 'James', 'Lee', false, '78AP3JVNBBIJWB8E9X2ATJIFQGL4YOR1', 'phone-7378352102', '2023-01-01 00:00:00.000', 'LeeJamesStudent', 1);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(58, '2023-07-18', 'Hall.Owen@gmain.com', 'Owen', 'Hall', false, 'GKC8IARVDEIQ8ZEZOME75P9OKQ9Z1QAS', 'phone-4954225404', '2023-01-01 00:00:00.000', 'HallOwenStudent', 1);