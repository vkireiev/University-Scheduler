package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;

@Validated
public interface EmployeeService extends AdminPersonService<Employee, Long> {

    long count();

    List<Employee> findAllByEmployeeType(@NotNull EmployeeType employeeType);

    long countByEmployeeType(@NotNull EmployeeType employeeType);

    boolean updateCourses(@NotNull Long id, @NotNull Set<Course> courses);

}
