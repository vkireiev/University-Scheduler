package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.repository.CourseRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @Value("${err.msg.service.jdbc.course.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.course.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.course.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.course.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.course.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.course.delete.fail.integrity.violation}")
    private String deleteFailIntegrityViolationMessage;

    @Value("${err.msg.service.jdbc.course.count.fail}")
    private String countFailMessage;

    private final CourseRepository courseRepository;

    @Override
    public Course add(Course course) {
        try {
            return courseRepository.save(course);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, course), e);
        }
    }

    @Override
    public Optional<Course> get(Long id) {
        try {
            return courseRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Course> getAll() {
        try {
            return courseRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Course update(Course course) {
        try {
            if (courseRepository.existsById(course.getId())) {
                return courseRepository.save(course);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, course));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, course), e);
        }
    }

    @Override
    public boolean delete(Course course) {
        try {
            courseRepository.delete(course);
            return !courseRepository.existsById(course.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, course), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            courseRepository.deleteById(id);
            return !courseRepository.existsById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, id), e);
        }
    }

    @Override
    public long count() {
        try {
            return courseRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

}
