# cmd> psql -U postgres
#postgres=#>
CREATE DATABASE db_university WITH ENCODING 'UTF8';
CREATE USER user_university WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE db_university TO user_university; 

## Connect to 'db_university' as postgres
# cmd> psql -U postgres db_university
## You are now connected to database "db_university" as user "postgres".
# db_university=#> 
GRANT ALL ON SCHEMA public TO user_university;
