package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.repository.GroupRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Value("${err.msg.service.jdbc.group.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.group.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.group.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.group.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.group.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.group.delete.fail.integrity.violation}")
    private String deleteFailIntegrityViolationMessage;

    @Value("${err.msg.service.jdbc.group.count.fail}")
    private String countFailMessage;

    private final GroupRepository groupRepository;

    @Override
    public Group add(Group group) {
        try {
            return groupRepository.save(group);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, group), e);
        }
    }

    @Override
    public Optional<Group> get(Long id) {
        try {
            return groupRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Group> getAll() {
        try {
            return groupRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Group update(Group group) {
        try {
            if (groupRepository.existsById(group.getId())) {
                return groupRepository.save(group);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, group));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, group), e);
        }
    }

    @Override
    public boolean delete(Group group) {
        try {
            groupRepository.delete(group);
            return !groupRepository.existsById(group.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(
                    deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, group), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            groupRepository.deleteById(id);
            return !groupRepository.existsById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(
                    deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, id), e);
        }
    }

    @Override
    public long count() {
        try {
            return groupRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public boolean updateCourses(Long id, Set<Course> courses) {
        try {
            Optional<Group> groupForUpdate = groupRepository.findById(id);
            if (groupForUpdate.isPresent()) {
                groupForUpdate.get().setCourses(new HashSet<>(courses));
                Group updatedGroup = groupRepository.save(groupForUpdate.get());
                return courses.containsAll(updatedGroup.getCourses())
                        && updatedGroup.getCourses().containsAll(courses);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

}
