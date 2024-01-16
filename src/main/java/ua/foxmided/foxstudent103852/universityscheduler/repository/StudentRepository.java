package ua.foxmided.foxstudent103852.universityscheduler.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByUsernameIgnoringCase(String username);

    @Modifying
    @Query("UPDATE Student s SET s.password = :password WHERE s.id = :id")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("UPDATE Student s SET s.email = :email WHERE s.id = :id")
    int updateEmail(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE Student s SET s.firstName = :firstname, s.lastName = :lastname, "
            + "s.birthday = :birthday, s.phoneNumber = :phoneNumber "
            + "WHERE s.id = :id")
    int updateProfile(@Param("id") Long id,
            @Param("firstname") String firstName, @Param("lastname") String lastName,
            @Param("birthday") LocalDate birthday, @Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query("UPDATE Student s SET s.locked = :locked "
            + "WHERE s.id = :id")
    int updateLocked(@Param("id") Long id, @Param("locked") boolean locked);

    @Modifying
    @Query("UPDATE Student s SET s.group = :group WHERE s.id = :id")
    int updateGroup(@Param("id") Long id, @Param("group") Group group);

}
