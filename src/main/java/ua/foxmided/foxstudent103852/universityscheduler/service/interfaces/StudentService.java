package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;

@Validated
public interface StudentService extends AdminPersonService<Student, Long> {

    long count();

    boolean updateGroup(@NotNull Long id, @NotNull Group group);

}
