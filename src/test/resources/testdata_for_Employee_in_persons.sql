-- Lecturers
ALTER SEQUENCE IF EXISTS "persons_id_seq" RESTART WITH 1;
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(1, '2023-07-18', 'Flores.Oliver@gmain.com', 'Oliver', 'Flores', false, '64ZQBM21BRUT0E2M61ZL1RHH9ZEZPERI', 'phone-4893914754', '2023-01-01 00:00:00.000', 'FloresOliverLecturer', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(2, '2023-07-18', 'Smith.Jackson@gmain.com', 'Jackson', 'Smith', false, '625YNNKWLKHHUQT968ZS5778M6BI51V2', 'phone-7485684585', '2023-01-01 00:00:00.000', 'SmithJacksonLecturer', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(3, '2023-07-18', 'Martin.Emily@gmain.com', 'Emily', 'Martin', false, 'N33I9UXL5EQQZH8BQYK5K1FB37LLRL57', 'phone-7992439516', '2023-01-01 00:00:00.000', 'MartinEmilyLecturer', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(4, '2023-07-18', 'Davis.Sebastian@gmain.com', 'Sebastian', 'Davis', false, 'CRFT6IWG03MCQVWXHD9ARRYXQLWINC1E', 'phone-7674550918', '2023-01-01 00:00:00.000', 'DavisSebastianLecturer', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(5, '2023-07-18', 'Williams.Sophia@gmain.com', 'Sophia', 'Williams', true, '8538FJ0LD4EY10JAB87M0L0FPOXLWS6X', 'phone-5896767441', '2023-01-01 00:00:00.000', 'WilliamsSophiaLecturer', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(6, '2023-07-18', 'Flores.Joseph@gmain.com', 'Joseph', 'Flores', true, 'B8PHCBYMQM2J83HSRQQ5F25LUF2KUS9D', 'phone-4575160456', '2023-01-01 00:00:00.000', 'FloresJosephLecturer', 2);
-- Employees
ALTER SEQUENCE IF EXISTS "persons_id_seq" RESTART WITH 26;
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(26, '1989-01-11', 'Miller.Noah@gmain.com', 'Noah', 'Miller', true, 'G58PQA7R7Q9ZJG5HBJB89TFRAECGAS8H', 'phone-0811590512', '2023-01-01 00:00:00.000', 'MillerNoahEmployee', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(27, '2000-07-16', 'Thomas.Levi@gmain.com', 'Levi', 'Thomas', false, 'M6AE9F8BSB84U9IF6DH0A48DFPF06LMJ', 'phone-7330666038', '2023-01-01 00:00:00.000', 'ThomasLeviEmployee', 2);
INSERT INTO public."persons" (id, birthday, email, firstname, lastname, "locked", "password", phone_number, register_date, username, user_type) VALUES(28, '1981-11-03', 'Perez.Logan@gmain.com', 'Logan', 'Perez', false, '8JO9Y2IB4UJOYP2D8FADG9U7YT4KLU8V', 'phone-5887429939', '2023-01-01 00:00:00.000', 'PerezLoganEmployee', 2);
