package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;

@Validated
public interface GroupService extends CrudService<Group, Long> {

    long count();

    boolean updateCourses(@NotNull Long id, @NotNull Set<Course> courses);

}
