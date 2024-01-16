package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.repository.EmployeeRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${err.msg.service.jdbc.employee.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.employee.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.employee.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.employee.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.employee.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.employee.delete.fail.integrity.violation}")
    private String deleteFailIntegrityViolationMessage;

    @Value("${err.msg.service.jdbc.employee.count.fail}")
    private String countFailMessage;

    @Value("${err.msg.service.jdbc.employee.getall.by.type.fail}")
    private String getAllByTypeFailMessage;

    @Value("${err.msg.service.jdbc.employee.count.by.type.fail}")
    private String countByTypeFailMessage;

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Employee add(Employee employee) {
        try {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            return employeeRepository.save(employee);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, employee), e);
        }
    }

    @Override
    public Optional<Employee> get(Long id) {
        try {
            return employeeRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Employee> getAll() {
        try {
            return employeeRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Employee update(Employee employee) {
        try {
            if (employeeRepository.existsById(employee.getId())) {
                return employeeRepository.save(employee);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, employee));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, employee), e);
        }
    }

    @Override
    public boolean delete(Employee employee) {
        try {
            employeeRepository.delete(employee);
            return !employeeRepository.existsById(employee.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, employee), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            employeeRepository.deleteById(id);
            return !employeeRepository.existsById(id);
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
            return employeeRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public List<Employee> findAllByEmployeeType(EmployeeType employeeType) {
        try {
            return employeeRepository.findAllByEmployeeType(employeeType);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByTypeFailMessage, e);
        }
    }

    @Override
    public long countByEmployeeType(EmployeeType employeeType) {
        try {
            return employeeRepository.countByEmployeeType(employeeType);
        } catch (DataAccessException e) {
            throw new DataProcessingException(countByTypeFailMessage, e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return employeeRepository.existsById(id);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            return employeeRepository.existsByUsernameIgnoringCase(username);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, username), e);
        }
    }

    @Transactional
    @Override
    public boolean updateEmail(Long id, String email) {
        try {
            return employeeRepository.updateEmail(id, email) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updatePassword(Long id, String password) {
        try {
            return employeeRepository.updatePassword(id, passwordEncoder.encode(password)) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateLocked(Long id, boolean locked) {
        try {
            return employeeRepository.updateLocked(id, locked) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateProfile(Long id, Employee employee) {
        try {
            return employeeRepository.updateProfile(id,
                    employee.getFirstName(), employee.getLastName(),
                    employee.getBirthday(), employee.getPhoneNumber(),
                    employee.getEmployeeType()) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Override
    public boolean updateUserRoles(Long id, Set<UserRole> userRoles) {
        try {
            Optional<Employee> employeeForUpdate = employeeRepository.findById(id);
            if (employeeForUpdate.isPresent()) {
                employeeForUpdate.get().setUserRoles(new HashSet<>(userRoles));
                Employee updatedEmployee = employeeRepository.save(employeeForUpdate.get());
                return userRoles.containsAll(updatedEmployee.getUserRoles())
                        && updatedEmployee.getUserRoles().containsAll(userRoles);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Override
    public boolean updateCourses(Long id, Set<Course> courses) {
        try {
            Optional<Employee> employeeForUpdate = employeeRepository.findById(id);
            if (employeeForUpdate.isPresent()) {
                employeeForUpdate.get().setCourses(new HashSet<>(courses));
                Employee updatedEmployee = employeeRepository.save(employeeForUpdate.get());
                return courses.containsAll(updatedEmployee.getCourses())
                        && updatedEmployee.getCourses().containsAll(courses);
            }
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id));
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

}
