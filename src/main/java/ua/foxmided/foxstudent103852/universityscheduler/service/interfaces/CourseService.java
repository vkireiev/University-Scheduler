package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import ua.foxmided.foxstudent103852.universityscheduler.model.Course;

public interface CourseService extends CrudService<Course, Long> {

    long count();

}
