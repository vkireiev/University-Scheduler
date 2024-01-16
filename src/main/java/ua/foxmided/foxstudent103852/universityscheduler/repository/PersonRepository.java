package ua.foxmided.foxstudent103852.universityscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByUsernameIgnoringCase(String username);

    @Query("SELECT p FROM Person p WHERE p.username = :username")
    public Person getPersonByUsername(@Param("username") String username);

}
