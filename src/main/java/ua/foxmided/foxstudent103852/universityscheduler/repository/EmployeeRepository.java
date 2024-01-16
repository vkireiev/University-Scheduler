package ua.foxmided.foxstudent103852.universityscheduler.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByEmployeeType(EmployeeType employeeType);

    long countByEmployeeType(EmployeeType employeeType);

    boolean existsByUsernameIgnoringCase(String username);

    @Modifying
    @Query("UPDATE Employee e SET e.password = :password WHERE e.id = :id")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("UPDATE Employee e SET e.email = :email WHERE e.id = :id")
    int updateEmail(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE Employee e SET e.firstName = :firstname, e.lastName = :lastname, "
            + "e.birthday = :birthday, e.phoneNumber = :phoneNumber, "
            + "e.employeeType = :employeeType "
            + "WHERE e.id = :id")
    int updateProfile(@Param("id") Long id,
            @Param("firstname") String firstName, @Param("lastname") String lastName,
            @Param("birthday") LocalDate birthday, @Param("phoneNumber") String phoneNumber,
            @Param("employeeType") EmployeeType employeeType);

    @Modifying
    @Query("UPDATE Employee e SET e.locked = :locked "
            + "WHERE e.id = :id")
    int updateLocked(@Param("id") Long id, @Param("locked") boolean locked);

}
