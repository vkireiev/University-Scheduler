package ua.foxmided.foxstudent103852.universityscheduler.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

public interface UserRepository extends JpaRepository<Person, Long> {

    boolean existsByUsernameIgnoringCase(String username);

    @Modifying
    @Query("UPDATE Person p SET p.password = :password WHERE p.id = :id")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("UPDATE Person p SET p.email = :email WHERE p.id = :id")
    int updateEmail(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE Person p SET p.firstName = :firstname, p.lastName = :lastname, "
            + "p.birthday = :birthday, p.phoneNumber = :phoneNumber "
            + "WHERE p.id = :id")
    int updateProfile(@Param("id") Long id,
            @Param("firstname") String firstName, @Param("lastname") String lastName,
            @Param("birthday") LocalDate birthday, @Param("phoneNumber") String phoneNumber);

}
