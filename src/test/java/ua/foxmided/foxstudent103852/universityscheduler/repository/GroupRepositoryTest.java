package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private GroupRepository testRepository;

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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithNullAsName_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setName(null);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithNullAsCapacity_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setCapacity(null);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithNullAsStudents_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setStudents(null);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithNullAsCourses_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setCourses(null);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithInvalidName_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setName(ConstantsTestGroup.GROUP_NAME_INVALID_1);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));

        Group newGroup2 = ConstantsTestGroup.newValidGroup();
        newGroup2.setName(ConstantsTestGroup.GROUP_NAME_INVALID_2);
        assertNull(this.testEntityManager.getId(newGroup2));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup2));

        Group newGroup3 = ConstantsTestGroup.newValidGroup();
        newGroup3.setName(ConstantsTestGroup.GROUP_NAME_EMPTY);
        assertNull(this.testEntityManager.getId(newGroup3));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithInvalidCapacity_ThenException() {
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        newGroup1.setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_1);
        assertNull(this.testEntityManager.getId(newGroup1));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup1));

        Group newGroup2 = ConstantsTestGroup.newValidGroup();
        newGroup2.setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_2);
        assertNull(this.testEntityManager.getId(newGroup2));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup2));

        Group newGroup3 = ConstantsTestGroup.newValidGroup();
        newGroup3.setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_3);
        assertNull(this.testEntityManager.getId(newGroup3));
        assertThrows(ValidationException.class, () -> testRepository.save(newGroup3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup3));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenAddGroupWithNotExistCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Group newGroup1 = ConstantsTestGroup.newValidGroup();
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        newGroup1.setCourses(new HashSet<>(Arrays.asList(notExistCourse)));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newGroup1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findById_WhenNotExistGroup_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestGroup.GROUP_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findById_WhenExistGroup_ThenReturnOptionalGroup() {
        Long groupExpectedId = ConstantsTestGroup.GROUP_ID_VALID_1;
        Group groupExpected = ConstantsTestGroup.getTestGroup(groupExpectedId);
        Student student55 = ConstantsTestStudent.getTestStudent(55L);
        groupExpected.addStudent(student55);
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(9L));
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(2L));
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(7L));
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(5L));
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(1L));
        groupExpected.addCourse(ConstantsTestCourse.getTestCourse(8L));

        Optional<Group> groupReturned = testRepository.findById(groupExpectedId);
        assertTrue(groupReturned.isPresent());
        assertThat(groupReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("courses", "students")
                .isEqualTo(groupExpected);
        assertThat(groupReturned.get().getCourses())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(groupExpected.getCourses());
        assertThat(groupReturned.get().getStudents())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(groupExpected.getStudents());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsGroups_ThenReturnEmptyListGroups() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void findAll_WhenExistsGroups_ThenReturnListGroups() {
        List<Group> groupsExpected = ConstantsTestGroup.getAllTestGroups();

        List<Group> groupsReturned = testRepository.findAll();
        assertThat(groupsReturned).isNotEmpty();
        assertThat(groupsReturned)
                .usingRecursiveComparison()
                .ignoringFields("courses", "students")
                .isEqualTo(groupsExpected);
        Map<Long, Set<Course>> groupsExpectedCourses = new HashMap<>();
        Map<Long, Set<Student>> groupsExpectedStudents = new HashMap<>();
        groupsExpected.forEach(group -> {
            groupsExpectedCourses.put(group.getId(), group.getCourses());
            groupsExpectedStudents.put(group.getId(), group.getStudents());
        });
        for (Group groupReturned : groupsReturned) {
            assertThat(groupReturned.getCourses())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(groupsExpectedCourses.get(groupReturned.getId()));
            assertThat(groupReturned.getStudents())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(groupsExpectedStudents.get(groupReturned.getId()));
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupWithInvalidName_ThenException() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;
        Optional<Group> groupForUpdate1 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate1.isPresent());
        groupForUpdate1.get().setName(ConstantsTestGroup.GROUP_NAME_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate1.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Group> groupForUpdate2 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate2.isPresent());
        groupForUpdate2.get().setName(ConstantsTestGroup.GROUP_NAME_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate2.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Group> groupForUpdate3 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate3.isPresent());
        groupForUpdate3.get().setName(ConstantsTestGroup.GROUP_NAME_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate3.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupWithInvalidCapacity_ThenException() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_2;
        Optional<Group> groupForUpdate1 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate1.isPresent());
        groupForUpdate1.get().setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate1.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Group> groupForUpdate2 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate2.isPresent());
        groupForUpdate2.get().setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate2.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Group> groupForUpdate3 = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate3.isPresent());
        groupForUpdate3.get().setCapacity(ConstantsTestGroup.GROUP_CAPACITY_INVALID_3);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(groupForUpdate3.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupWithNotExistCourse_ThenException() {
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_2;
        Optional<Group> groupForUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate.isPresent());
        assertFalse(groupForUpdate.get().getCourses().contains(notExistCourse));
        groupForUpdate.get().addCourse(notExistCourse);
        assertTrue(groupForUpdate.get().getCourses().contains(notExistCourse));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(groupForUpdate.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupWithValidName_ThenShouldUpdate() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;
        Optional<Group> groupForUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate.isPresent());
        String nameGroupBeforeUpdate = groupForUpdate.get().getName();
        assertNotEquals(ConstantsTestGroup.GROUP_NAME_FOR_UPDATE, groupForUpdate.get().getName());
        groupForUpdate.get().setName(ConstantsTestGroup.GROUP_NAME_FOR_UPDATE);
        testRepository.save(groupForUpdate.get());
        testEntityManager.flush();
        Optional<Group> groupAfterUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupAfterUpdate.isPresent());
        assertNotEquals(nameGroupBeforeUpdate, groupAfterUpdate.get().getName());
        assertEquals(ConstantsTestGroup.GROUP_NAME_FOR_UPDATE, groupAfterUpdate.get().getName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupWithValidCapacity_ThenShouldUpdate() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;
        Optional<Group> groupForUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate.isPresent());
        Short capacityGroupBeforeUpdate = groupForUpdate.get().getCapacity();
        assertNotEquals(ConstantsTestGroup.GROUP_CAPACITY_FOR_UPDATE, groupForUpdate.get().getCapacity());
        groupForUpdate.get().setCapacity(ConstantsTestGroup.GROUP_CAPACITY_FOR_UPDATE);
        testRepository.save(groupForUpdate.get());
        testEntityManager.flush();
        Optional<Group> groupAfterUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupAfterUpdate.isPresent());
        assertNotEquals(capacityGroupBeforeUpdate, groupAfterUpdate.get().getCapacity());
        assertEquals(ConstantsTestGroup.GROUP_CAPACITY_FOR_UPDATE, groupAfterUpdate.get().getCapacity());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupAndAddExistCourse_ThenShouldUpdate() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_2;
        Optional<Group> groupForUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate.isPresent());
        Set<Course> coursesGroupBeforeUpdate = new HashSet<>(groupForUpdate.get().getCourses());
        Course addCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        assertFalse(groupForUpdate.get().getCourses().contains(addCourse));
        groupForUpdate.get().addCourse(addCourse);
        testRepository.save(groupForUpdate.get());
        testEntityManager.flush();
        Optional<Group> groupAfterUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupAfterUpdate.isPresent());
        assertTrue(groupAfterUpdate.get().getCourses().contains(addCourse));
        assertThat(groupAfterUpdate.get().getCourses())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isNotEqualTo(coursesGroupBeforeUpdate);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void save_WhenUpdateExistGroupAndRemoveExistCourse_ThenShouldUpdate() {
        long groupForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;
        Optional<Group> groupForUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupForUpdate.isPresent());
        assertFalse(groupForUpdate.get().getCourses().isEmpty());
        Set<Course> coursesGroupBeforeUpdate = new HashSet<>(groupForUpdate.get().getCourses());
        Course removeCourse = groupForUpdate.get().getCourses().stream()
                .findFirst().get();
        groupForUpdate.get().removeCourse(removeCourse);
        assertFalse(groupForUpdate.get().getCourses().contains(removeCourse));
        testRepository.save(groupForUpdate.get());
        testEntityManager.flush();
        Optional<Group> groupAfterUpdate = testRepository.findById(groupForUpdateId);
        assertTrue(groupAfterUpdate.isPresent());
        assertFalse(groupAfterUpdate.get().getCourses().contains(removeCourse));
        assertThat(groupAfterUpdate.get().getCourses())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isNotEqualTo(coursesGroupBeforeUpdate);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteWithNullAsGroup_ThenException() {
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteNotExistGroup_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_NOT_EXIST;
        Group groupForDelete = ConstantsTestGroup.newValidGroup(groupForDeleteId);
        assertFalse(testRepository.existsById(groupForDeleteId));
        testRepository.delete(groupForDelete);
        testEntityManager.flush();
        assertFalse(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteExistGroupWithAssignedStudent_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Group> groupForDelete = transactionTemplate.execute(status -> {
            Optional<Group> group = testRepository.findById(groupForDeleteId);
            assertTrue(group.isPresent());
            assertFalse(group.get().getStudents().isEmpty());
            assertTrue(group.get().getCourses().isEmpty());
            return group;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(groupForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteExistGroupWithAssignedCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE_INVALID_2;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Group> groupForDelete = transactionTemplate.execute(status -> {
            Optional<Group> group = testRepository.findById(groupForDeleteId);
            assertTrue(group.isPresent());
            assertTrue(group.get().getStudents().isEmpty());
            assertFalse(group.get().getCourses().isEmpty());
            return group;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(groupForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void delete_WhenDeleteExistGroupWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;
        Optional<Group> groupForDelete = testRepository.findById(groupForDeleteId);
        assertTrue(groupForDelete.isPresent());
        testRepository.delete(groupForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.existsById(groupForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsGroups_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void count_WhenExistsGroups_ThenReturnCountGroups() {
        assertEquals(8L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void deleteById_WhenDeleteWithNullAsGroupId_ThenException() {
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void deleteById_WhenCalledAndNotExistGroupWithSuchId_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(groupForDeleteId));
        testRepository.deleteById(groupForDeleteId);
        testEntityManager.flush();
        assertFalse(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void deleteById_WhenDeleteExistGroupWithAssignedStudent_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE_INVALID_1;

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            Optional<Group> group = testRepository.findById(groupForDeleteId);
            assertTrue(group.isPresent());
            assertFalse(group.get().getStudents().isEmpty());
            assertTrue(group.get().getCourses().isEmpty());
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(groupForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void deleteById_WhenDeleteExistGroupWithAssignedCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE_INVALID_2;

        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            Optional<Group> group = testRepository.findById(groupForDeleteId);
            assertTrue(group.isPresent());
            assertTrue(group.get().getStudents().isEmpty());
            assertFalse(group.get().getCourses().isEmpty());
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(groupForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertTrue(testRepository.existsById(groupForDeleteId));
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
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Student_in_persons.sql",
            "classpath:testdata_for_Student_in_persons_user_roles.sql",
            "classpath:testdata_for_Student_in_students.sql" })
    void deleteById_WhenDeleteExistGroupWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long groupForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;
        assertTrue(testRepository.existsById(groupForDeleteId));
        testRepository.deleteById(groupForDeleteId);
        testEntityManager.flush();
        assertFalse(testRepository.existsById(groupForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

}
