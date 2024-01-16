package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EmployeeRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsUsername_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setUsername(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsPassword_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setPassword(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsEmail_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setEmail(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsFirstName_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setFirstName(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsLastName_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setLastName(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsBirthday_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setBirthday(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsPhoneNumber_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setPhoneNumber(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsUserRoles_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setUserRoles(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsRegisterDate_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setRegisterDate(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsEmployeeType_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setEmployeeType(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithNullAsCourses_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setCourses(null);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidUsername_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql" })
    void save_WhenAddEmployeeWithDuplicateUsername_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Optional<Employee> existEmployee = testRepository.findById(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Employee newEmployee = ConstantsTestEmployee.newValidEmployee();
        newEmployee.setUsername(existEmployee.get().getUsername());
        assertNull(this.testEntityManager.getId(newEmployee));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newEmployee);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidPassword_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidEmail_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidFirstName_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidLastName_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddEmployeeWithInvalidPhoneNumber_ThenException() {
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_INVALID_1);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee1));

        Employee newEmployee2 = ConstantsTestEmployee.newValidEmployee();
        newEmployee2.setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_INVALID_2);
        assertNull(this.testEntityManager.getId(newEmployee2));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee2));

        Employee newEmployee3 = ConstantsTestEmployee.newValidEmployee();
        newEmployee3.setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_EMPTY);
        assertNull(this.testEntityManager.getId(newEmployee3));
        assertThrows(ValidationException.class, () -> testRepository.save(newEmployee3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newEmployee3));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Course_in_courses.sql" })
    void save_WhenAddEmployeeWithNotExistCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        newEmployee1.addCourse(notExistCourse);
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newEmployee1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddValidEmployee_ThenShouldAddAndReturnAddedEntity() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Employee newEmployee1 = ConstantsTestEmployee.newValidEmployee();
        assertNull(this.testEntityManager.getId(newEmployee1));
        assertTrue(testRepository.save(newEmployee1) instanceof Employee);
        assertNotNull(this.testEntityManager.getId(newEmployee1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt < countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenIdIsNull_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.findById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql" })
    void findById_WhenNotExistEmployee_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void findById_WhenExistEmployee_ThenReturnOptionalEmployee() {
        Long findEmployeeId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeExpected = ConstantsTestEmployee.getTestEmployee(findEmployeeId);
        Course course5 = ConstantsTestCourse.getTestCourse(5L);
        Course course6 = ConstantsTestCourse.getTestCourse(6L);
        employeeExpected.addCourse(course5);
        employeeExpected.addCourse(course6);

        Optional<Employee> employeeReturned = testRepository.findById(findEmployeeId);
        assertTrue(employeeReturned.isPresent());
        assertThat(employeeReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("courses")
                .isEqualTo(employeeExpected);
        assertTrue(employeeExpected.getCourses().containsAll(employeeReturned.get().getCourses()));
        assertTrue(employeeReturned.get().getCourses().containsAll(employeeExpected.getCourses()));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsEmployees_ThenReturnEmptyListEmployees() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void findAll_WhenExistsEmployees_ThenReturnListEmployees() {
        List<Employee> employeesExpected = ConstantsTestEmployee.getAllTestEmployees();

        List<Employee> employeesReturned = testRepository.findAll();
        assertThat(employeesReturned).isNotEmpty();
        assertThat(employeesReturned)
                .usingRecursiveComparison()
                .ignoringFields("courses")
                .isEqualTo(employeesExpected);
        Map<Long, Set<Course>> employeesExpectedCourses = new HashMap<>();
        employeesExpected.forEach(employee -> employeesExpectedCourses.put(employee.getId(), employee.getCourses()));
        for (Employee employeeReturned : employeesReturned) {
            assertTrue(employeesExpectedCourses.get(employeeReturned.getId())
                    .containsAll(employeeReturned.getCourses()));
            assertTrue(employeeReturned.getCourses()
                    .containsAll(employeesExpectedCourses.get(employeeReturned.getId())));
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsUsername_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setUsername(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidUsername_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsPassword_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setPassword(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidPassword_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsEmail_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setEmail(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidEmail_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsFirstName_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setFirstName(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidFirstName_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsLastName_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setLastName(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidLastName_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsBirthday_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setBirthday(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsPhoneNumber_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setPhoneNumber(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidPhoneNumber_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId2 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate2 = testRepository.findById(employeeForUpdateId2);
        assertTrue(employeeForUpdate2.isPresent());
        employeeForUpdate2.get().setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long employeeForUpdateId3 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate3 = testRepository.findById(employeeForUpdateId3);
        assertTrue(employeeForUpdate3.isPresent());
        employeeForUpdate3.get().setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsUserRoles_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setUserRoles(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithInvalidUserRoles_ThenException() {
        long employeeForUpdateId1 = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> employeeForUpdate1 = testRepository.findById(employeeForUpdateId1);
        assertTrue(employeeForUpdate1.isPresent());
        employeeForUpdate1.get().setUserRoles(ConstantsTestEmployee.EMPLOYEE_USER_ROLES_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsRegisterDate_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setRegisterDate(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsEmployeeType_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setEmployeeType(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithNullAsCourses_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        employeeForUpdate.get().setCourses(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeAndAddNotExistCourse_ThenException() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        assertFalse(employeeForUpdate.get().getCourses().contains(notExistCourse));
        employeeForUpdate.get().addCourse(notExistCourse);
        assertTrue(employeeForUpdate.get().getCourses().contains(notExistCourse));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidUsername_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String usernameEmployeeBeforeUpdate = employeeForUpdate.get().getUsername();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_USERNAME_FOR_UPDATE, employeeForUpdate.get().getUsername());
        employeeForUpdate.get().setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(usernameEmployeeBeforeUpdate, employeeAfterUpdate.get().getUsername());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_USERNAME_FOR_UPDATE, employeeAfterUpdate.get().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithDuplicateUsername_ThenException() {
        long existEmployeeId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_2;
        Optional<Employee> existEmployee = testRepository.findById(existEmployeeId);
        assertTrue(existEmployee.isPresent());
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String usernameEmployeeForUpdate = employeeForUpdate.get().getUsername();
        assertNotEquals(existEmployee.get().getUsername(), employeeForUpdate.get().getUsername());
        employeeForUpdate.get().setUsername(existEmployee.get().getUsername());
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(employeeForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(employeeForUpdate.get().getUsername(), employeeAfterUpdate.get().getUsername());
        assertEquals(usernameEmployeeForUpdate, employeeAfterUpdate.get().getUsername());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidPassword_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String passwordEmployeeBeforeUpdate = employeeForUpdate.get().getPassword();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE, employeeForUpdate.get().getPassword());
        employeeForUpdate.get().setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(passwordEmployeeBeforeUpdate, employeeAfterUpdate.get().getPassword());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE, employeeAfterUpdate.get().getPassword());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidEmail_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String emailEmployeeBeforeUpdate = employeeForUpdate.get().getEmail();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE, employeeForUpdate.get().getEmail());
        employeeForUpdate.get().setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(emailEmployeeBeforeUpdate, employeeAfterUpdate.get().getEmail());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE, employeeAfterUpdate.get().getEmail());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidFirstName_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String firstNameEmployeeBeforeUpdate = employeeForUpdate.get().getFirstName();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE, employeeForUpdate.get().getFirstName());
        employeeForUpdate.get().setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(firstNameEmployeeBeforeUpdate, employeeAfterUpdate.get().getFirstName());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE, employeeAfterUpdate.get().getFirstName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidLastName_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String lastNameEmployeeBeforeUpdate = employeeForUpdate.get().getLastName();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE, employeeForUpdate.get().getLastName());
        employeeForUpdate.get().setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(lastNameEmployeeBeforeUpdate, employeeAfterUpdate.get().getLastName());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE, employeeAfterUpdate.get().getLastName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidBirthday_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        LocalDate birthdayEmployeeBeforeUpdate = employeeForUpdate.get().getBirthday();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE, employeeForUpdate.get().getBirthday());
        employeeForUpdate.get().setBirthday(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(birthdayEmployeeBeforeUpdate, employeeAfterUpdate.get().getBirthday());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE, employeeAfterUpdate.get().getBirthday());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidPhoneNumber_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        String phoneNumberEmployeeBeforeUpdate = employeeForUpdate.get().getPhoneNumber();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                employeeForUpdate.get().getPhoneNumber());
        employeeForUpdate.get().setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(phoneNumberEmployeeBeforeUpdate, employeeAfterUpdate.get().getPhoneNumber());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                employeeAfterUpdate.get().getPhoneNumber());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidUserRoles_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        Set<UserRole> userRolesEmployeeBeforeUpdate = employeeForUpdate.get().getUserRoles();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_USER_ROLES_FOR_UPDATE, employeeForUpdate.get().getUserRoles());
        employeeForUpdate.get().setUserRoles(ConstantsTestEmployee.EMPLOYEE_USER_ROLES_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertThat(userRolesEmployeeBeforeUpdate).usingRecursiveComparison()
                .isNotEqualTo(employeeAfterUpdate.get().getUserRoles());
        assertThat(employeeAfterUpdate.get().getUserRoles())
                .usingRecursiveComparison()
                .isEqualTo(ConstantsTestEmployee.EMPLOYEE_USER_ROLES_FOR_UPDATE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidRegisterDate_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        LocalDateTime registerDateEmployeeBeforeUpdate = employeeForUpdate.get().getRegisterDate();
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_REGISTER_DATE_FOR_UPDATE,
                employeeForUpdate.get().getRegisterDate());
        employeeForUpdate.get().setRegisterDate(ConstantsTestEmployee.EMPLOYEE_REGISTER_DATE_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(registerDateEmployeeBeforeUpdate, employeeAfterUpdate.get().getRegisterDate());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_REGISTER_DATE_FOR_UPDATE,
                employeeAfterUpdate.get().getRegisterDate());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidLocked_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        boolean lockedEmployeeBeforeUpdate = employeeForUpdate.get().isLocked();
        employeeForUpdate.get().setLocked(!employeeForUpdate.get().isLocked());
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(lockedEmployeeBeforeUpdate, employeeAfterUpdate.get().isLocked());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeWithValidEmployeeType_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        EmployeeType employeeTypeEmployeeBeforeUpdate = employeeForUpdate.get().getEmployeeType();
        assertNotEquals(ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE,
                employeeForUpdate.get().getEmployeeType());
        employeeForUpdate.get().setEmployeeType(ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE);
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(employeeTypeEmployeeBeforeUpdate, employeeAfterUpdate.get().getEmployeeType());
        assertEquals(ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE,
                employeeAfterUpdate.get().getEmployeeType());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeAndAddValidCourse_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        Set<Course> coursesEmployeeBeforeUpdate = new HashSet<>(employeeForUpdate.get().getCourses());
        Course courseForAdd = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_2);
        assertFalse(employeeForUpdate.get().getCourses().contains(courseForAdd));
        employeeForUpdate.get().addCourse(courseForAdd);
        assertTrue(employeeForUpdate.get().getCourses().contains(courseForAdd));
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertTrue(employeeAfterUpdate.get().getCourses().contains(courseForAdd));
        assertThat(employeeAfterUpdate.get().getCourses())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isNotEqualTo(coursesEmployeeBeforeUpdate);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void save_WhenUpdateExistEmployeeAndRemoveValidCourse_ThenShouldUpdate() {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        assertFalse(employeeForUpdate.get().getCourses().isEmpty());
        Course courseForRemove = employeeForUpdate.get().getCourses().stream()
                .findFirst().get();
        assertTrue(employeeForUpdate.get().getCourses().contains(courseForRemove));
        employeeForUpdate.get().removeCourse(courseForRemove);
        assertFalse(employeeForUpdate.get().getCourses().contains(courseForRemove));
        testRepository.save(employeeForUpdate.get());
        testEntityManager.flush();
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertFalse(employeeAfterUpdate.get().getCourses().contains(courseForRemove));
        assertThat(employeeAfterUpdate.get().getCourses())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(employeeForUpdate.get().getCourses());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteWithNullAsEmployee_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.delete(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void delete_WhenDeleteNotExistEmployee_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Employee employeesForDelete = ConstantsTestEmployee
                .newValidEmployee(ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST);
        assertFalse(testRepository.findById(employeesForDelete.getId()).isPresent());
        testRepository.delete(employeesForDelete);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void delete_WhenDeleteExistEmployeeWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long employeeForDeleteId = ConstantsTestEmployee.EMPLOYEE_ID_FOR_DELETE;
        Optional<Employee> employeeForDelete = testRepository.findById(employeeForDeleteId);
        assertTrue(employeeForDelete.isPresent());
        testRepository.delete(employeeForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.findById(employeeForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void delete_WhenDeleteExistEmployeeWithAssignedCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long employeeForDeleteId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Employee> employeeForDelete = transactionTemplate.execute(status -> {
            Optional<Employee> employee = testRepository.findById(employeeForDeleteId);
            assertTrue(employee.isPresent());
            assertFalse(employee.get().getCourses().isEmpty());
            return employee;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(employeeForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(employeeForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsEmployees_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void count_WhenExistsEmployees_ThenReturnCountEmployees() {
        assertEquals(9L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void deleteById_WhenDeleteWithNullAsEmployee_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.deleteById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void deleteById_WhenDeleteNotExistEmployee_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long employeesForDeleteId = ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(employeesForDeleteId));
        testRepository.deleteById(employeesForDeleteId);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void deleteById_WhenDeleteExistEmployeeWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long employeeForDeleteId = ConstantsTestEmployee.EMPLOYEE_ID_FOR_DELETE;
        assertTrue(testRepository.existsById(employeeForDeleteId));
        testRepository.deleteById(employeeForDeleteId);
        testEntityManager.flush();
        assertFalse(testRepository.existsById(employeeForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void deleteById_WhenDeleteExistEmployeeWithAssignedCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long employeeForDeleteId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            Optional<Employee> employee = testRepository.findById(employeeForDeleteId);
            assertTrue(employee.isPresent());
            assertFalse(employee.get().getCourses().isEmpty());
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(employeeForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(employeeForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAllByEmployeeType_WhenNotExistsEmployees_ThenReturnEmptyListEmployees() {
        assertThat(testRepository.findAllByEmployeeType(EmployeeType.EMPLOYEE)).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void findAllByEmployeeType_WhenCalledWithNullAsEmployeeType_ThenReturnEmptyListEmployees() {
        assertThat(testRepository.findAllByEmployeeType(null)).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void findAllByEmployeeType_WhenExistsEmployees_ThenReturnListEmployees() {
        List<Employee> employeesExpected = ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsEmployee();

        List<Employee> employeesReturned = testRepository.findAllByEmployeeType(EmployeeType.EMPLOYEE);
        assertThat(employeesReturned).isNotEmpty();
        assertThat(employeesReturned)
                .usingRecursiveComparison()
                .isEqualTo(employeesExpected);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void countByEmployeeType_WhenCalledWithNullAsEmployeeType_ThenReturnZero() {
        assertEquals(0L, testRepository.countByEmployeeType(null));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void countByEmployeeType_WhenNotExistsEmployees_ThenReturnZero() {
        assertEquals(0L, testRepository.countByEmployeeType(EmployeeType.LECTURER));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void countByEmployeeType_WhenExistsEmployeesWithEmployeeType_ThenReturnCountEmployeesWithEmployeeType() {
        assertEquals(6L, testRepository.countByEmployeeType(EmployeeType.LECTURER));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistsEmployees_ThenReturnFalse() {
        assertFalse(testRepository.count() > 0);
        String searchUsername = ConstantsTestEmployee
                .getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1)
                .getUsername();

        assertFalse(testRepository.existsByUsernameIgnoringCase(searchUsername));

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        assertFalse(testRepository.existsByUsernameIgnoringCase(usernameInLowerCase));

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        assertFalse(testRepository.existsByUsernameIgnoringCase(usernameInUpperCase));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void existsByUsernameIgnoringCase_WhenCalledWithNullAsUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);

        assertFalse(testRepository.existsByUsernameIgnoringCase(null));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void existsByUsernameIgnoringCase_WhenCalledWithEmptyStringAsUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);

        assertFalse(testRepository.existsByUsernameIgnoringCase(ConstantsTest.EMPTY_STRING));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistEmployeeWithSuchUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        String searchUsername = ConstantsTestEmployee
                .getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1)
                .getUsername();
        assertTrue(testRepository.existsByUsernameIgnoringCase(searchUsername));
        searchUsername = searchUsername.repeat(2);

        assertFalse(testRepository.existsByUsernameIgnoringCase(searchUsername));

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        assertFalse(testRepository.existsByUsernameIgnoringCase(usernameInLowerCase));

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        assertFalse(testRepository.existsByUsernameIgnoringCase(usernameInUpperCase));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndExistEmployeeWithSuchUsername_ThenReturnTrue() {
        assertTrue(testRepository.count() > 0);
        String searchUsername = ConstantsTestEmployee
                .getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1)
                .getUsername();

        assertTrue(testRepository.existsByUsernameIgnoringCase(searchUsername));

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        assertTrue(testRepository.existsByUsernameIgnoringCase(usernameInLowerCase));

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        assertTrue(testRepository.existsByUsernameIgnoringCase(usernameInUpperCase));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updatePassword_WhenCalledAndNotExistsEmployees_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updatePassword(employeeForUpdateId,
                ConstantsTestEmployee.EMPLOYEE_PASSWORD_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updatePassword_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updatePassword(employeeForUpdateId,
                ConstantsTestEmployee.EMPLOYEE_PASSWORD_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updatePassword_WhenCalledAndExistEmployeeWithSuchIdAndInvalidPassword_ThenException() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updatePassword(employeeForUpdateId, null);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdateAttempt1 = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdateAttempt1.isPresent());
        assertEquals(employeeForUpdate.get().getPassword(), employeeAfterUpdateAttempt1.get().getPassword());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updatePassword(employeeForUpdateId,
                            ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_2);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdateAttempt2 = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdateAttempt2.isPresent());
        assertEquals(employeeForUpdate.get().getPassword(), employeeAfterUpdateAttempt2.get().getPassword());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updatePassword_WhenCalledAndExistEmployeeWithSuchIdAndValidPassword_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());

        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE, employeeForUpdate.get().getPassword());
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updatePassword(employeeForUpdateId,
                    ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE) > 0);
        });
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE, employeeAfterUpdate.get().getPassword());
        assertNotEquals(employeeForUpdate.get().getPassword(), employeeAfterUpdate.get().getPassword());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateEmail_WhenCalledAndNotExistsEmployees_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateEmail(employeeForUpdateId, ConstantsTestEmployee.EMPLOYEE_EMAIL_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateEmail_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateEmail(employeeForUpdateId, ConstantsTestEmployee.EMPLOYEE_EMAIL_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateEmail_WhenCalledAndExistEmployeeWithSuchIdAndInvalidEmail_ThenException() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateEmail(employeeForUpdateId, null);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdateAttempt1 = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdateAttempt1.isPresent());
        assertEquals(employeeForUpdate.get().getEmail(), employeeAfterUpdateAttempt1.get().getEmail());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateEmail(employeeForUpdateId, ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_2);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdateAttempt2 = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdateAttempt2.isPresent());
        assertEquals(employeeForUpdate.get().getEmail(), employeeAfterUpdateAttempt2.get().getEmail());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateEmail_WhenCalledAndExistEmployeeWithSuchIdAndValidEmail_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());

        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE, employeeForUpdate.get().getEmail());
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateEmail(employeeForUpdateId,
                    ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE) > 0);
        });
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertEquals(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE, employeeAfterUpdate.get().getEmail());
        assertNotEquals(employeeForUpdate.get().getEmail(), employeeAfterUpdate.get().getEmail());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateProfile_WhenCalledAndNotExistsEmployees_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateProfile(employeeForUpdateId,
                ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_EMPLOYEE_TYPE_FOR_UPDATE));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateProfile_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateProfile(employeeForUpdateId,
                ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                ConstantsTestEmployee.EMPLOYEE_EMPLOYEE_TYPE_FOR_UPDATE));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateProfile_WhenCalledAndExistEmployeeWithSuchIdAndInvalidProfile_ThenException() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        assertNotNull(employeeForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE, employeeForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE, employeeForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_2, employeeForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE, employeeForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                employeeForUpdate.get().getPhoneNumber());
        assertNotEquals(ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE,
                employeeForUpdate.get().getEmployeeType());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateProfile(employeeForUpdateId,
                            null,
                            ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE,
                            ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE,
                            ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                            ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Employee> employeeAfterUpdateAttempt1 = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdateAttempt1.isPresent());
        assertEquals(employeeForUpdate.get().getFirstName(), employeeAfterUpdateAttempt1.get().getFirstName());
        assertEquals(employeeForUpdate.get().getLastName(), employeeAfterUpdateAttempt1.get().getLastName());
        assertEquals(employeeForUpdate.get().getBirthday(), employeeAfterUpdateAttempt1.get().getBirthday());
        assertEquals(employeeForUpdate.get().getPhoneNumber(), employeeAfterUpdateAttempt1.get().getPhoneNumber());
        assertEquals(employeeForUpdate.get().getEmployeeType(), employeeAfterUpdateAttempt1.get().getEmployeeType());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateProfile_WhenCalledAndExistEmployeeWithSuchIdAndValidProfile_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE, employeeForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE, employeeForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE, employeeForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                employeeForUpdate.get().getPhoneNumber());
        assertNotEquals(ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE,
                employeeForUpdate.get().getEmployeeType());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateProfile(employeeForUpdateId,
                    ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE,
                    ConstantsTestEmployee.EMPLOYEE_LASTNAME_FOR_UPDATE,
                    ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_FOR_UPDATE,
                    ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_FOR_UPDATE,
                    ConstantsTestEmployee.LECTURER_EMPLOYEE_TYPE_FOR_UPDATE) > 0);
        });
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(employeeForUpdate.get().getFirstName(), employeeAfterUpdate.get().getFirstName());
        assertNotEquals(employeeForUpdate.get().getLastName(), employeeAfterUpdate.get().getLastName());
        assertNotEquals(employeeForUpdate.get().getBirthday(), employeeAfterUpdate.get().getBirthday());
        assertNotEquals(employeeForUpdate.get().getPhoneNumber(), employeeAfterUpdate.get().getPhoneNumber());
        assertNotEquals(employeeForUpdate.get().getEmployeeType(), employeeAfterUpdate.get().getEmployeeType());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateLocked_WhenCalledAndNotExistsEmployees_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateLocked(employeeForUpdateId, ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateLocked_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_INVALID;
        assertFalse(testRepository.existsById(employeeForUpdateId));

        assertEquals(0, testRepository.updateLocked(employeeForUpdateId, ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql" })
    void updateLocked_WhenCalledAndExistEmployeeWithSuchIdAndValidLocked_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Optional<Employee> employeeForUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeForUpdate.isPresent());

        assertNotEquals(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE, employeeForUpdate.get().getEmail());
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateLocked(employeeForUpdateId,
                    !employeeForUpdate.get().isLocked()) > 0);
        });
        Optional<Employee> employeeAfterUpdate = testRepository.findById(employeeForUpdateId);
        assertTrue(employeeAfterUpdate.isPresent());
        assertNotEquals(employeeForUpdate.get().isLocked(), employeeAfterUpdate.get().isLocked());
    }

}
