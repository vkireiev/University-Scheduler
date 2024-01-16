package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest {

    @Autowired
    private PersonRepository testRepository;

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
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
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
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
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
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
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
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
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
    void getPersonByUsername_WhenCalledAndNotExistsPersons_ThenReturnNull() {
        assertFalse(testRepository.count() > 0);
        String searchUsername = ConstantsTestStudent
                .getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1)
                .getUsername();

        assertNull(testRepository.getPersonByUsername(searchUsername));

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        assertNull(testRepository.getPersonByUsername(searchUsername));

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        assertNull(testRepository.getPersonByUsername(searchUsername));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
    void getPersonByUsername_WhenCalledAndNotExistPersonWithSuchUsername_ThenReturnNull() {
        assertTrue(testRepository.count() > 0);
        String searchUsername = ConstantsTestStudent.STUDENT_USERNAME_FOR_UPDATE;

        assertNull(testRepository.getPersonByUsername(searchUsername));

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        assertNull(testRepository.getPersonByUsername(searchUsername));

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        assertNull(testRepository.getPersonByUsername(searchUsername));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql" })
    void getPersonByUsername_WhenCalledAndExistPersonWithSuchUsername_ThenReturnPerson() {
        assertTrue(testRepository.count() > 0);
        Person expectedPerson = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        String searchUsername = expectedPerson.getUsername();

        Person returnedPerson1 = testRepository.getPersonByUsername(searchUsername);
        assertTrue(returnedPerson1 instanceof Person);
        assertNotNull(returnedPerson1);
        assertThat(returnedPerson1)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "username", "password", "locked")
                .isEqualTo(expectedPerson);

        String usernameInLowerCase = searchUsername.toLowerCase();
        assertNotEquals(searchUsername, usernameInLowerCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInLowerCase));
        Person returnedPerson2 = testRepository.getPersonByUsername(searchUsername);
        assertTrue(returnedPerson2 instanceof Person);
        assertNotNull(returnedPerson2);
        assertThat(returnedPerson2)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "username", "password", "locked")
                .isEqualTo(expectedPerson);

        String usernameInUpperCase = searchUsername.toUpperCase();
        assertNotEquals(searchUsername, usernameInUpperCase);
        assertTrue(searchUsername.equalsIgnoreCase(usernameInUpperCase));
        Person returnedPerson3 = testRepository.getPersonByUsername(searchUsername);
        assertTrue(returnedPerson3 instanceof Person);
        assertNotNull(returnedPerson3);
        assertThat(returnedPerson3)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "username", "password", "locked")
                .isEqualTo(expectedPerson);
    }

}
