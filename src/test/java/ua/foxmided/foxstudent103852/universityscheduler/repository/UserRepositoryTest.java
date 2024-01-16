package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
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

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistsPersons_ThenReturnFalse() {
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndNotExistPersonWithSuchUsername_ThenReturnFalse() {
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void existsByUsernameIgnoringCase_WhenCalledAndExistPersonWithSuchUsername_ThenReturnTrue() {
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
    void updatePassword_WhenCalledAndNotExistsPersons_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updatePassword(entityForUpdateId, ConstantsTestStudent.STUDENT_PASSWORD_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updatePassword_WhenCalledAndNotExistPersonWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updatePassword(entityForUpdateId, ConstantsTestStudent.STUDENT_PASSWORD_VALID));
    }

    @ParameterizedTest
    @NullSource
    @CsvSource({
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2
    })
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updatePassword_WhenCalledAndExistPersonWithSuchIdAndInvalidPassword_ThenException(
            String parameterizedPassword) {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Person> entityForUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updatePassword(entityForUpdateId, parameterizedPassword);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        Optional<Person> entityAfterUpdateAttempt = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdateAttempt.isPresent());
        assertEquals(entityForUpdate.get().getPassword(), entityAfterUpdateAttempt.get().getPassword());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updatePassword_WhenCalledAndExistPersonWithSuchIdAndValidPassword_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Person> entityForUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityForUpdate.isPresent());

        assertNotEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, entityForUpdate.get().getPassword());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertEquals(1, testRepository.updatePassword(entityForUpdateId,
                    ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE));
        });
        Optional<Person> entityAfterUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdate.isPresent());
        assertEquals(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE, entityAfterUpdate.get().getPassword());
        assertNotEquals(entityForUpdate.get().getPassword(), entityAfterUpdate.get().getPassword());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateEmail_WhenCalledAndNotExistsPersons_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updateEmail(entityForUpdateId, ConstantsTestStudent.STUDENT_EMAIL_VALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateEmail_WhenCalledAndNotExistPersonWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updateEmail(entityForUpdateId, ConstantsTestStudent.STUDENT_EMAIL_VALID));
    }

    @ParameterizedTest
    @NullSource
    @CsvSource({
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2
    })
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateEmail_WhenCalledAndExistPersonWithSuchIdAndInvalidEmail_ThenException(
            String parameterizedPassword) {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Person> entityForUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityForUpdate.isPresent());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateEmail(entityForUpdateId, parameterizedPassword);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        Optional<Person> entityAfterUpdateAttempt = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdateAttempt.isPresent());
        assertEquals(entityForUpdate.get().getEmail(), entityAfterUpdateAttempt.get().getEmail());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateEmail_WhenCalledAndExistPersonWithSuchIdAndValidEmail_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Person> entityForUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityForUpdate.isPresent());

        assertNotEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, entityForUpdate.get().getEmail());

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            assertEquals(1, testRepository.updateEmail(entityForUpdateId,
                    ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE));
        });
        Optional<Person> entityAfterUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdate.isPresent());
        assertEquals(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE, entityAfterUpdate.get().getEmail());
        assertNotEquals(entityForUpdate.get().getEmail(), entityAfterUpdate.get().getEmail());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void updateProfile_WhenCalledAndNotExistsPersons_ThenReturnZero() {
        assertFalse(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updateProfile(entityForUpdateId,
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateProfile_WhenCalledAndNotExistPersonWithSuchId_ThenReturnZero() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(entityForUpdateId));

        assertEquals(0, testRepository.updateProfile(entityForUpdateId,
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
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateProfile_WhenCalledAndExistPersonWithSuchIdAndInvalidProfile_ThenException() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Optional<Person> entityForUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityForUpdate.isPresent());
        assertNotNull(entityForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, entityForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, entityForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, entityForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, entityForUpdate.get().getPhoneNumber());

        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.updateProfile(entityForUpdateId,
                            null,
                            ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                            ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                            ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE);
                    testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        Optional<Person> entityAfterUpdateAttempt = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdateAttempt.isPresent());
        assertEquals(entityForUpdate.get().getFirstName(), entityAfterUpdateAttempt.get().getFirstName());
        assertEquals(entityForUpdate.get().getLastName(), entityAfterUpdateAttempt.get().getLastName());
        assertEquals(entityForUpdate.get().getBirthday(), entityAfterUpdateAttempt.get().getBirthday());
        assertEquals(entityForUpdate.get().getPhoneNumber(), entityAfterUpdateAttempt.get().getPhoneNumber());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql" })
    void updateProfile_WhenCalledAndExistPersonWithSuchIdAndValidProfile_ThenUpdateAndReturnNumberAffectedRows() {
        assertTrue(testRepository.count() > 0);
        long entityForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Person> entityForUpdate = transactionTemplate.execute(status -> {
            Optional<Person> entity = testRepository.findById(entityForUpdateId);
            assertTrue(entity.isPresent());
            return entity;
        });

        assertNotEquals(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE, entityForUpdate.get().getFirstName());
        assertNotEquals(ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE, entityForUpdate.get().getLastName());
        assertNotEquals(ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE, entityForUpdate.get().getBirthday());
        assertNotEquals(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE, entityForUpdate.get().getPhoneNumber());

        transactionTemplate.executeWithoutResult(status -> {
            assertEquals(1, testRepository.updateProfile(entityForUpdateId,
                    ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_LASTNAME_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_BIRTHDAY_FOR_UPDATE,
                    ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE));
        });
        Optional<Person> entityAfterUpdate = testRepository.findById(entityForUpdateId);
        assertTrue(entityAfterUpdate.isPresent());
        assertNotEquals(entityForUpdate.get().getFirstName(), entityAfterUpdate.get().getFirstName());
        assertNotEquals(entityForUpdate.get().getLastName(), entityAfterUpdate.get().getLastName());
        assertNotEquals(entityForUpdate.get().getBirthday(), entityAfterUpdate.get().getBirthday());
        assertNotEquals(entityForUpdate.get().getPhoneNumber(), entityAfterUpdate.get().getPhoneNumber());
    }

}
