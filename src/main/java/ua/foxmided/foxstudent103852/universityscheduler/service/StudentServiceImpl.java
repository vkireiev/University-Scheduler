package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.repository.StudentRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    @Value("${err.msg.service.jdbc.student.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.student.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.student.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.student.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.student.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.student.count.fail}")
    private String countFailMessage;

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Student add(Student student) {
        try {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            return studentRepository.save(student);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, student), e);
        }
    }

    @Override
    public Optional<Student> get(Long id) {
        try {
            return studentRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Student> getAll() {
        try {
            return studentRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Student update(Student student) {
        try {
            if (studentRepository.existsById(student.getId())) {
                return studentRepository.save(student);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, student));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, student), e);
        }
    }

    @Override
    public boolean delete(Student student) {
        try {
            studentRepository.delete(student);
            return !studentRepository.existsById(student.getId());
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, student), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            studentRepository.deleteById(id);
            return !studentRepository.existsById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, id), e);
        }
    }

    @Override
    public long count() {
        try {
            return studentRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return studentRepository.existsById(id);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            return studentRepository.existsByUsernameIgnoringCase(username);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, username), e);
        }
    }

    @Transactional
    @Override
    public boolean updateEmail(Long id, String email) {
        try {
            return studentRepository.updateEmail(id, email) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updatePassword(Long id, String password) {
        try {
            return studentRepository.updatePassword(id, passwordEncoder.encode(password)) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateLocked(Long id, boolean locked) {
        try {
            return studentRepository.updateLocked(id, locked) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateProfile(Long id, Student student) {
        try {
            return studentRepository.updateProfile(id,
                    student.getFirstName(), student.getLastName(),
                    student.getBirthday(), student.getPhoneNumber()) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Override
    public boolean updateUserRoles(Long id, Set<UserRole> userRoles) {
        try {
            Optional<Student> studentForUpdate = studentRepository.findById(id);
            if (studentForUpdate.isPresent()) {
                studentForUpdate.get().setUserRoles(new HashSet<>(userRoles));
                Student updatedStudent = studentRepository.save(studentForUpdate.get());
                return userRoles.containsAll(updatedStudent.getUserRoles())
                        && updatedStudent.getUserRoles().containsAll(userRoles);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateGroup(Long id, Group group) {
        try {
            return studentRepository.updateGroup(id, group) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

}
