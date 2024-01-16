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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private StudentRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsUsername_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setUsername(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsPassword_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setPassword(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsEmail_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setEmail(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsFirstName_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setFirstName(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsLastName_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setLastName(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsBirthday_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setBirthday(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsPhoneNumber_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setPhoneNumber(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsUserRoles_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setUserRoles(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsRegisterDate_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setRegisterDate(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNullAsGroup_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setGroup(null);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidUsername_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setUsername(ConstantsTestStudent.STUDENT_USERNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddStudentWithDuplicateUsername_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Optional<Student> existStudent = testRepository.findById(ConstantsTestStudent.STUDENT_ID_VALID_1);
        Student newStudent = ConstantsTestStudent.newValidStudent();
        newStudent.setUsername(existStudent.get().getUsername());
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newStudent);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidPassword_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidEmail_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setEmail(ConstantsTestStudent.STUDENT_EMAIL_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setEmail(ConstantsTestStudent.STUDENT_EMAIL_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setEmail(ConstantsTestStudent.STUDENT_EMAIL_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidFirstName_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidLastName_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setLastName(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setLastName(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setLastName(ConstantsTestStudent.STUDENT_LASTNAME_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithInvalidPhoneNumber_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        newStudent1.setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_INVALID_1);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));

        Student newStudent2 = ConstantsTestStudent.newValidStudent();
        newStudent2.setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_INVALID_2);
        assertNull(this.testEntityManager.getId(newStudent2));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent2));

        Student newStudent3 = ConstantsTestStudent.newValidStudent();
        newStudent3.setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_EMPTY);
        assertNull(this.testEntityManager.getId(newStudent3));
        assertThrows(ValidationException.class, () -> testRepository.save(newStudent3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddStudentWithNotExistGroup_ThenException() {
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        Group notExistGroup = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        notExistGroup.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        newStudent1.setGroup(notExistGroup);
        assertNull(this.testEntityManager.getId(newStudent1));
        assertThrows(DataAccessException.class, () -> testRepository.save(newStudent1),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Group_in_groups.sql" })
    void save_WhenAddValidStudent_ThenShouldAddAndReturnAddedEntity() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Student newStudent1 = ConstantsTestStudent.newValidStudent();
        assertNull(this.testEntityManager.getId(newStudent1));
        assertTrue(testRepository.save(newStudent1) instanceof Student);
        assertNotNull(this.testEntityManager.getId(newStudent1));
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findById_WhenNotExistStudent_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestStudent.STUDENT_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findById_WhenExistStudent_ThenReturnOptionalStudent() {
        Long findStudentId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Student studentExpected = ConstantsTestStudent.getTestStudent(findStudentId);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        group1.addStudent(studentExpected);

        Optional<Student> studentReturned = testRepository.findById(findStudentId);
        assertTrue(studentReturned.isPresent());
        assertThat(studentReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("group")
                .isEqualTo(studentExpected);
        assertEquals(studentExpected.getGroup(), studentReturned.get().getGroup());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsStudents_ThenReturnEmptyListStudents() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findAll_WhenExistsStudents_ThenReturnListStudents() {
        List<Student> studentsExpected = ConstantsTestStudent.getAllTestStudents();

        List<Student> studentsReturned = testRepository.findAll();
        assertThat(studentsReturned).isNotEmpty();
        assertThat(studentsReturned)
                .usingRecursiveComparison()
                .ignoringFields("group")
                .isEqualTo(studentsExpected);
        Map<Long, Group> studentsExpectedGroups = new HashMap<>();
        studentsExpected.forEach(student -> studentsExpectedGroups.put(student.getId(), student.getGroup()));
        for (Student studentReturned : studentsReturned) {
            assertEquals(studentsExpectedGroups.get(studentReturned.getId()), studentReturned.getGroup());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsUsername_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setUsername(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidUsername_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setUsername(ConstantsTestStudent.STUDENT_USERNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsPassword_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setPassword(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidPassword_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setPassword(ConstantsTestStudent.STUDENT_PASSWORD_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setPassword(ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setPassword(ConstantsTestStudent.STUDENT_PASSWORD_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsEmail_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setEmail(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidEmail_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setEmail(ConstantsTestStudent.STUDENT_EMAIL_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setEmail(ConstantsTestStudent.STUDENT_EMAIL_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setEmail(ConstantsTestStudent.STUDENT_EMAIL_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsFirstName_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setFirstName(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidFirstName_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsLastName_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setLastName(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidLastName_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setLastName(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setLastName(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setLastName(ConstantsTestStudent.STUDENT_LASTNAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsBirthday_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setBirthday(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsPhoneNumber_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setPhoneNumber(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidPhoneNumber_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId2 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate2 = testRepository.findById(studentForUpdateId2);
        assertTrue(studentForUpdate2.isPresent());
        studentForUpdate2.get().setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        long studentForUpdateId3 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate3 = testRepository.findById(studentForUpdateId3);
        assertTrue(studentForUpdate3.isPresent());
        studentForUpdate3.get().setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsUserRoles_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setUserRoles(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithInvalidUserRoles_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        studentForUpdate1.get().setUserRoles(ConstantsTestStudent.STUDENT_USER_ROLES_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsRegisterDate_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setRegisterDate(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNullAsGroup_ThenException() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        studentForUpdate.get().setGroup(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithNotExistGroup_ThenException() {
        long studentForUpdateId1 = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate1 = testRepository.findById(studentForUpdateId1);
        assertTrue(studentForUpdate1.isPresent());
        Group notExistGroup = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        notExistGroup.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        studentForUpdate1.get().setGroup(notExistGroup);
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(studentForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidUsername_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String usernameStudentBeforeUpdate = studentForUpdate.get().getUsername();
        assertNotEquals(ConstantsTestStudent.STUDENT_USERNAME_FOR_UPDATE, studentForUpdate.get().getUsername());
        studentForUpdate.get().setUsername(ConstantsTestStudent.STUDENT_USERNAME_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(usernameStudentBeforeUpdate, studentAfterUpdate.get().getUsername());
        assertEquals(ConstantsTestStudent.STUDENT_USERNAME_FOR_UPDATE, studentAfterUpdate.get().getUsername());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithDuplicateUsername_ThenException() {
        long existStudentId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_2;
        Optional<Student> existStudent = testRepository.findById(existStudentId);
        assertTrue(existStudent.isPresent());
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String usernameStudentBeforeUpdate = studentForUpdate.get().getUsername();
        assertNotEquals(existStudent.get().getUsername(), studentForUpdate.get().getUsername());
        studentForUpdate.get().setUsername(existStudent.get().getUsername());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(studentForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(studentForUpdate.get().getUsername(), studentAfterUpdate.get().getUsername());
        assertEquals(usernameStudentBeforeUpdate, studentAfterUpdate.get().getUsername());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidPassword_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String passwordStudentBeforeUpdate = studentForUpdate.get().getPassword();
        assertNotEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, studentForUpdate.get().getPassword());
        studentForUpdate.get().setPassword(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(passwordStudentBeforeUpdate, studentAfterUpdate.get().getPassword());
        assertEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, studentAfterUpdate.get().getPassword());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidEmail_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String emailStudentBeforeUpdate = studentForUpdate.get().getEmail();
        assertNotEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, studentForUpdate.get().getEmail());
        studentForUpdate.get().setEmail(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(emailStudentBeforeUpdate, studentAfterUpdate.get().getEmail());
        assertEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, studentAfterUpdate.get().getEmail());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidFirstName_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String firstNameStudentBeforeUpdate = studentForUpdate.get().getFirstName();
        assertNotEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, studentForUpdate.get().getFirstName());
        studentForUpdate.get().setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(firstNameStudentBeforeUpdate, studentAfterUpdate.get().getFirstName());
        assertEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, studentAfterUpdate.get().getFirstName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidLastName_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String lastNameStudentBeforeUpdate = studentForUpdate.get().getLastName();
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, studentForUpdate.get().getLastName());
        studentForUpdate.get().setLastName(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(lastNameStudentBeforeUpdate, studentAfterUpdate.get().getLastName());
        assertEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, studentAfterUpdate.get().getLastName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidBirthday_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        LocalDate birthdayStudentBeforeUpdate = studentForUpdate.get().getBirthday();
        assertNotEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, studentForUpdate.get().getBirthday());
        studentForUpdate.get().setBirthday(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(birthdayStudentBeforeUpdate, studentAfterUpdate.get().getBirthday());
        assertEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, studentAfterUpdate.get().getBirthday());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidPhoneNumber_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        String phoneNumberStudentBeforeUpdate = studentForUpdate.get().getPhoneNumber();
        assertNotEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, studentForUpdate.get().getPhoneNumber());
        studentForUpdate.get().setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(phoneNumberStudentBeforeUpdate, studentAfterUpdate.get().getPhoneNumber());
        assertEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, studentAfterUpdate.get().getPhoneNumber());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidUserRoles_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        Set<UserRole> userRolesStudentBeforeUpdate = studentForUpdate.get().getUserRoles();
        assertNotEquals(ConstantsTestStudent.STUDENT_USER_ROLES_FOR_UPDATE, studentForUpdate.get().getUserRoles());
        studentForUpdate.get().setUserRoles(ConstantsTestStudent.STUDENT_USER_ROLES_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertThat(userRolesStudentBeforeUpdate)
                .usingRecursiveComparison()
                .isNotEqualTo(studentAfterUpdate.get().getUserRoles());
        assertThat(studentAfterUpdate.get().getUserRoles())
                .usingRecursiveComparison()
                .isEqualTo(ConstantsTestStudent.STUDENT_USER_ROLES_FOR_UPDATE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidRegisterDate_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        LocalDateTime registerDateStudentBeforeUpdate = studentForUpdate.get().getRegisterDate();
        assertNotEquals(ConstantsTestStudent.STUDENT_REGISTER_DATE_FOR_UPDATE,
                studentForUpdate.get().getRegisterDate());
        studentForUpdate.get().setRegisterDate(ConstantsTestStudent.STUDENT_REGISTER_DATE_FOR_UPDATE);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(registerDateStudentBeforeUpdate, studentAfterUpdate.get().getRegisterDate());
        assertEquals(ConstantsTestStudent.STUDENT_REGISTER_DATE_FOR_UPDATE, studentAfterUpdate.get().getRegisterDate());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidLocked_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        boolean lockedStudentBeforeUpdate = studentForUpdate.get().isLocked();
        studentForUpdate.get().setLocked(!studentForUpdate.get().isLocked());
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(lockedStudentBeforeUpdate, studentAfterUpdate.get().isLocked());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistStudentWithValidGroup_ThenShouldUpdate() {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        Group groupStudentBeforeUpdate = studentForUpdate.get().getGroup();
        assertNotEquals(ConstantsTestStudent.STUDENT_GROUP_VALID, studentForUpdate.get().getGroup());
        studentForUpdate.get().setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        testRepository.save(studentForUpdate.get());
        testEntityManager.flush();
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertThat(groupStudentBeforeUpdate)
                .usingRecursiveComparison()
                .isNotEqualTo(studentAfterUpdate.get().getGroup());
        assertThat(studentAfterUpdate.get().getGroup())
                .usingRecursiveComparison()
                .isEqualTo(ConstantsTestStudent.STUDENT_GROUP_VALID);
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteNotExistStudent_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Student studentForDelete = ConstantsTestStudent.newValidStudent(ConstantsTestStudent.STUDENT_ID_NOT_EXIST);
        assertFalse(testRepository.findById(studentForDelete.getId()).isPresent());
        testRepository.delete(studentForDelete);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteExistStudent_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long studentForDeleteId = ConstantsTestStudent.STUDENT_ID_FOR_DELETE;
        Optional<Student> studentForDelete = testRepository.findById(studentForDeleteId);
        assertTrue(studentForDelete.isPresent());
        testRepository.delete(studentForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.findById(studentForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertNotEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsStudents_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void count_WhenExistsStudents_ThenReturnCountStudents() {
        assertEquals(8L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistsStudents_ThenReturnFalse() {
        assertFalse(testRepository.count() > 0);
        String searchUsername = ConstantsTestStudent
                .getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1)
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void existsByUsernameIgnoringCase_WhenCalledWithNullAsUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);

        assertFalse(testRepository.existsByUsernameIgnoringCase(null));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void existsByUsernameIgnoringCase_WhenCalledWithEmptyStringAsUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);

        assertFalse(testRepository.existsByUsernameIgnoringCase(ConstantsTest.EMPTY_STRING));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistStudentWithSuchUsername_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        String searchUsername = ConstantsTestStudent
                .getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1)
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndExistStudentWithSuchUsername_ThenReturnTrue() {
        assertTrue(testRepository.count() > 0);
        String searchUsername = ConstantsTestStudent
                .getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1)
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
    void updatePassword_WhenCalledAndNotExistsStudents_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updatePassword(studentForUpdateId, ConstantsTestStudent.STUDENT_PASSWORD_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updatePassword_WhenCalledAndNotExistStudentWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updatePassword(studentForUpdateId, ConstantsTestStudent.STUDENT_PASSWORD_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updatePassword_WhenCalledAndExistStudentWithSuchIdAndInvalidPassword_ThenException() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updatePassword(studentForUpdateId, null);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt1 = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt1.isPresent());
        assertEquals(studentForUpdate.get().getPassword(), studentAfterUpdateAttempt1.get().getPassword());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updatePassword(studentForUpdateId, ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt2 = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt2.isPresent());
        assertEquals(studentForUpdate.get().getPassword(), studentAfterUpdateAttempt2.get().getPassword());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updatePassword_WhenCalledAndExistStudentWithSuchIdAndValidPassword_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        assertNotEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, studentForUpdate.get().getPassword());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updatePassword(studentForUpdateId,
                    ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE) > 0);
        });
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, studentAfterUpdate.get().getPassword());
        assertNotEquals(studentForUpdate.get().getPassword(), studentAfterUpdate.get().getPassword());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateEmail_WhenCalledAndNotExistsStudents_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateEmail(studentForUpdateId, ConstantsTestStudent.STUDENT_EMAIL_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateEmail_WhenCalledAndNotExistStudentWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateEmail(studentForUpdateId, ConstantsTestStudent.STUDENT_EMAIL_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateEmail_WhenCalledAndExistStudentWithSuchIdAndInvalidEmail_ThenException() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateEmail(studentForUpdateId, null);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt1 = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt1.isPresent());
        assertEquals(studentForUpdate.get().getEmail(), studentAfterUpdateAttempt1.get().getEmail());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateEmail(studentForUpdateId, ConstantsTestStudent.STUDENT_EMAIL_INVALID_2);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt2 = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt2.isPresent());
        assertEquals(studentForUpdate.get().getEmail(), studentAfterUpdateAttempt2.get().getEmail());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateEmail_WhenCalledAndExistStudentWithSuchIdAndValidEmail_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        assertNotEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, studentForUpdate.get().getEmail());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateEmail(studentForUpdateId,
                    ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE) > 0);
        });
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, studentAfterUpdate.get().getEmail());
        assertNotEquals(studentForUpdate.get().getEmail(), studentAfterUpdate.get().getEmail());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateProfile_WhenCalledAndNotExistsStudents_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateProfile(studentForUpdateId,
                ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateProfile_WhenCalledAndNotExistStudentWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateProfile(studentForUpdateId,
                ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateProfile_WhenCalledAndExistStudentWithSuchIdAndInvalidProfile_ThenException() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());
        assertNotNull(studentForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, studentForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, studentForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_2, studentForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, studentForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, studentForUpdate.get().getPhoneNumber());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateProfile(studentForUpdateId,
                            null,
                            ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                            ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                            ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt1 = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt1.isPresent());
        assertEquals(studentForUpdate.get().getFirstName(), studentAfterUpdateAttempt1.get().getFirstName());
        assertEquals(studentForUpdate.get().getLastName(), studentAfterUpdateAttempt1.get().getLastName());
        assertEquals(studentForUpdate.get().getBirthday(), studentAfterUpdateAttempt1.get().getBirthday());
        assertEquals(studentForUpdate.get().getPhoneNumber(), studentAfterUpdateAttempt1.get().getPhoneNumber());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateProfile_WhenCalledAndExistStudentWithSuchIdAndValidProfile_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Student> studentForUpdate = transactionTemplate.execute(status -> {
            Optional<Student> student = testRepository.findById(studentForUpdateId);
            assertTrue(student.isPresent());
            return student;
        });

        assertNotEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, studentForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, studentForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, studentForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, studentForUpdate.get().getPhoneNumber());

        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateProfile(studentForUpdateId,
                    ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE) > 0);
        });
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(studentForUpdate.get().getFirstName(), studentAfterUpdate.get().getFirstName());
        assertNotEquals(studentForUpdate.get().getLastName(), studentAfterUpdate.get().getLastName());
        assertNotEquals(studentForUpdate.get().getBirthday(), studentAfterUpdate.get().getBirthday());
        assertNotEquals(studentForUpdate.get().getPhoneNumber(), studentAfterUpdate.get().getPhoneNumber());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateGroup_WhenCalledAndNotExistsStudents_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateGroup(studentForUpdateId, ConstantsTestStudent.STUDENT_GROUP_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateGroup_WhenCalledAndNotExistStudentWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateGroup(studentForUpdateId, ConstantsTestStudent.STUDENT_GROUP_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateGroup_WhenCalledAndExistStudentWithSuchIdAndInvalidGroup_ThenException() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        Group notExistGroup = new Group();
        notExistGroup.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateGroup(studentForUpdateId, notExistGroup);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Student> studentAfterUpdateAttempt = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdateAttempt.isPresent());
        assertEquals(studentForUpdate.get().getGroup(), studentAfterUpdateAttempt.get().getGroup());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateGroup_WhenCalledAndExistStudentWithSuchIdAndValidGroup_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        assertNotEquals(ConstantsTestStudent.STUDENT_GROUP_VALID, studentForUpdate.get().getGroup());
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateGroup(studentForUpdateId,
                    ConstantsTestStudent.STUDENT_GROUP_VALID) > 0);
        });
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertEquals(ConstantsTestStudent.STUDENT_GROUP_VALID, studentAfterUpdate.get().getGroup());
        assertNotEquals(studentForUpdate.get().getGroup(), studentAfterUpdate.get().getGroup());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateLocked_WhenCalledAndNotExistsStudents_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateLocked(studentForUpdateId, ConstantsTestStudent.STUDENT_LOCKED_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateLocked_WhenCalledAndNotExistStudentWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_INVALID;
        assertFalse(testRepository.existsById(studentForUpdateId));

        assertEquals(0, testRepository.updateLocked(studentForUpdateId, ConstantsTestStudent.STUDENT_LOCKED_VALID));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void updateLocked_WhenCalledAndExistStudentWithSuchIdAndValidLocked_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Student> studentForUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentForUpdate.isPresent());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertTrue(testRepository.updateLocked(studentForUpdateId,
                    !studentForUpdate.get().isLocked()) > 0);
        });
        Optional<Student> studentAfterUpdate = testRepository.findById(studentForUpdateId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(studentForUpdate.get().isLocked(), studentAfterUpdate.get().isLocked());
    }

}
