package ua.foxmided.foxstudent103852.universityscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.universityscheduler.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
