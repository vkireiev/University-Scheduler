CREATE TABLE IF NOT EXISTS persons_user_roles (
    person_id BIGINT NOT NULL,
    user_role SMALLINT NOT NULL,
    PRIMARY KEY (person_id, user_role),
    CONSTRAINT persons_user_roles_person_id_fkey FOREIGN KEY(person_id) REFERENCES persons(id)
);
