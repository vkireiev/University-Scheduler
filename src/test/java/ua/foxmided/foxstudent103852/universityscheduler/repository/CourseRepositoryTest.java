package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CourseRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithNullAsName_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setName(null);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithInvalidName_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setName(ConstantsTestCourse.COURSE_NAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));

        Course newCourse2 = ConstantsTestCourse.newValidCourse();
        newCourse2.setName(ConstantsTestCourse.COURSE_NAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newCourse2));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse2));

        Course newCourse3 = ConstantsTestCourse.newValidCourse();
        newCourse3.setName(ConstantsTestCourse.COURSE_NAME_EMPTY);
        assertNull(this.testEntityManager.getId(newCourse3));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithNullAsDescription_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setDescription(null);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithInvalidDescription_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_INVALID_1);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));

        Course newCourse2 = ConstantsTestCourse.newValidCourse();
        newCourse2.setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_INVALID_2);
        assertNull(this.testEntityManager.getId(newCourse2));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse2));

        Course newCourse3 = ConstantsTestCourse.newValidCourse();
        newCourse3.setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_EMPTY);
        assertNull(this.testEntityManager.getId(newCourse3));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithNullAsLecturers_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setLecturers(null);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddCourseWithNullAsGroups_ThenException() {
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        newCourse1.setGroups(null);
        assertNull(this.testEntityManager.getId(newCourse1));
        assertThrows(ValidationException.class, () -> testRepository.save(newCourse1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenAddValidCourse_ThenShouldAddAndReturnAddedEntity() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Course newCourse1 = ConstantsTestCourse.newValidCourse();
        assertNull(this.testEntityManager.getId(newCourse1));
        assertTrue(testRepository.save(newCourse1) instanceof Course);
        assertNotNull(this.testEntityManager.getId(newCourse1));
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
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void findById_WhenNotExistCourse_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestCourse.COURSE_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void findById_WhenExistCourse_ThenReturnOptionalCourse() {
        Long courseExpectedId = ConstantsTestCourse.COURSE_ID_VALID_2;
        Course courseExpected = ConstantsTestCourse.getTestCourse(courseExpectedId);
        Employee lecturer1 = ConstantsTestEmployee.getTestEmployee(1L);
        lecturer1.addCourse(courseExpected);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        group2.addCourse(courseExpected);
        group3.addCourse(courseExpected);
        group4.addCourse(courseExpected);

        Optional<Course> courseReturned = testRepository.findById(courseExpectedId);
        assertTrue(courseReturned.isPresent());
        assertThat(courseReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("groups", "lecturers")
                .isEqualTo(courseExpected);
        assertThat(courseReturned.get().getGroups())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(courseExpected.getGroups());
        assertThat(courseReturned.get().getLecturers())
                .usingRecursiveComparison()
                .comparingOnlyFields(Employee.class.getSuperclass().getSimpleName() + ".id")
                .isEqualTo(courseExpected.getLecturers());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsCourses_ThenReturnEmptyListCourses() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void findAll_WhenExistsCourses_ThenReturnListCourses() {
        List<Course> coursesExpected = ConstantsTestCourse.getAllTestCourses();

        List<Course> coursesReturned = testRepository.findAll();
        assertThat(coursesReturned).isNotEmpty();
        assertThat(coursesReturned)
                .usingRecursiveComparison()
                .ignoringFields("groups", "lecturers")
                .isEqualTo(coursesExpected);
        Map<Long, Set<Group>> coursesExpectedGroups = new HashMap<>();
        Map<Long, Set<Employee>> coursesExpectedLecturer = new HashMap<>();
        coursesExpected.forEach(course -> {
            coursesExpectedGroups.put(course.getId(), course.getGroups());
            coursesExpectedLecturer.put(course.getId(), course.getLecturers());
        });
        for (Course courseReturned : coursesReturned) {
            assertThat(courseReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(coursesExpectedGroups.get(courseReturned.getId()));
            assertThat(courseReturned.getLecturers())
                    .usingRecursiveComparison()
                    .comparingOnlyFields(Employee.class.getSuperclass().getSimpleName() + ".id")
                    .isEqualTo(coursesExpectedLecturer.get(courseReturned.getId()));
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenUpdateExistCourseInvalidName_ThenException() {
        long courseForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;

        Optional<Course> courseForUpdate1 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate1.isPresent());
        courseForUpdate1.get().setName(ConstantsTestCourse.COURSE_NAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate1.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Course> courseForUpdate2 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate2.isPresent());
        courseForUpdate2.get().setName(ConstantsTestCourse.COURSE_NAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate2.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Course> courseForUpdate3 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate3.isPresent());
        courseForUpdate3.get().setName(ConstantsTestCourse.COURSE_NAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate3.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenUpdateExistCourseInvalidDescription_ThenException() {
        long courseForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;

        Optional<Course> courseForUpdate1 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate1.isPresent());
        courseForUpdate1.get().setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate1.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Course> courseForUpdate2 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate2.isPresent());
        courseForUpdate2.get().setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate2.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Course> courseForUpdate3 = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate3.isPresent());
        courseForUpdate3.get().setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(courseForUpdate3.get());
                    this.testEntityManager.flush();

                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenUpdateExistCourseWithValidName_ThenShouldUpdate() {
        long courseForUpdateId = ConstantsTestCourse.COURSE_ID_VALID_1;
        Optional<Course> courseForUpdate = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate.isPresent());
        String nameCourseBeforeUpdate = courseForUpdate.get().getName();
        assertNotEquals(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE, courseForUpdate.get().getName());
        courseForUpdate.get().setName(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE);
        testRepository.save(courseForUpdate.get());
        testEntityManager.flush();
        Optional<Course> courseAfterUpdate = testRepository.findById(courseForUpdateId);
        assertTrue(courseAfterUpdate.isPresent());
        assertNotEquals(nameCourseBeforeUpdate, courseAfterUpdate.get().getName());
        assertEquals(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE, courseAfterUpdate.get().getName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void save_WhenUpdateExistCourseWithValidDescription_ThenShouldUpdate() {
        long courseForUpdateId = ConstantsTestCourse.COURSE_ID_VALID_1;
        Optional<Course> courseForUpdate = testRepository.findById(courseForUpdateId);
        assertTrue(courseForUpdate.isPresent());
        String descriptionCourseBeforeUpdate = courseForUpdate.get().getDescription();
        assertNotEquals(ConstantsTestCourse.COURSE_DESCRIPTION_FOR_UPDATE, courseForUpdate.get().getDescription());
        courseForUpdate.get().setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_FOR_UPDATE);
        testRepository.save(courseForUpdate.get());
        testEntityManager.flush();
        Optional<Course> courseAfterUpdate = testRepository.findById(courseForUpdateId);
        assertTrue(courseAfterUpdate.isPresent());
        assertNotEquals(descriptionCourseBeforeUpdate, courseAfterUpdate.get().getDescription());
        assertEquals(ConstantsTestCourse.COURSE_DESCRIPTION_FOR_UPDATE, courseAfterUpdate.get().getDescription());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteWithNullAsCourse_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.delete(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void delete_WhenDeleteNotExistCourse_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Course courseForDelete = ConstantsTestCourse.newValidCourse(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        assertFalse(testRepository.findById(courseForDelete.getId()).isPresent());
        testRepository.delete(courseForDelete);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void delete_WhenDeleteExistCourseWithAssignedLecturer_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Course> courseForDelete = transactionTemplate.execute(status -> {
            Optional<Course> course = testRepository.findById(courseForDeleteId);
            assertTrue(course.isPresent());
            assertFalse(course.get().getLecturers().isEmpty());
            assertTrue(course.get().getGroups().isEmpty());
            return course;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(courseForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(courseForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void delete_WhenDeleteExistCourseWithAssignedGroup_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE_INVALID_2;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Course> courseForDelete = transactionTemplate.execute(status -> {
            Optional<Course> course = testRepository.findById(courseForDeleteId);
            assertTrue(course.isPresent());
            assertTrue(course.get().getLecturers().isEmpty());
            assertFalse(course.get().getGroups().isEmpty());
            return course;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(courseForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(courseForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void delete_WhenDeleteExistCourseWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;
        Optional<Course> courseForDelete = testRepository.findById(courseForDeleteId);
        assertTrue(courseForDelete.isPresent());
        testRepository.delete(courseForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.findById(courseForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsCourses_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void count_WhenExistsCourses_ThenReturnCountCourses() {
        assertEquals(13L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void deleteById_WhenDeleteWithNullAsCourseId_ThenException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> testRepository.deleteById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void deleteById_WhenDeleteNotExistCourse_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(courseForDeleteId));
        testRepository.deleteById(courseForDeleteId);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void deleteById_WhenDeleteExistCourseWithAssignedLecturer_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            Optional<Course> course = testRepository.findById(courseForDeleteId);
            assertTrue(course.isPresent());
            assertFalse(course.get().getLecturers().isEmpty());
            assertTrue(course.get().getGroups().isEmpty());
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(courseForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(courseForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Employee_in_employees_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void deleteById_WhenDeleteExistCourseWithAssignedGroup_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE_INVALID_2;

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            Optional<Course> course = testRepository.findById(courseForDeleteId);
            assertTrue(course.isPresent());
            assertTrue(course.get().getLecturers().isEmpty());
            assertFalse(course.get().getGroups().isEmpty());
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(courseForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(courseForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql" })
    void deleteById_WhenDeleteExistCourseWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long courseForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;
        assertTrue(testRepository.existsById(courseForDeleteId));
        testRepository.deleteById(courseForDeleteId);
        testEntityManager.flush();
        assertFalse(testRepository.findById(courseForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

}
