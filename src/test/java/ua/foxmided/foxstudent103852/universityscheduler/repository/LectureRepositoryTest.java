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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LectureRepositoryTest {
    private static final int PAGE_LIMIT = 3;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, PAGE_LIMIT, Sort.by("lectureDate").ascending());

    private TransactionTemplate transactionTemplate;

    @Autowired
    private LectureRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsAuditorium_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setAuditorium(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsSubject_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setSubject(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsCourse_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setCourse(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsGroups_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setGroups(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsLecturer_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setLecturer(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsLectureDate_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setLectureDate(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNullAsTimeSlot_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setTimeSlot(null);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNotExistAuditorium_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        Auditorium notExistAuditorium = ConstantsTestAuditorium
                .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);
        notExistAuditorium.setId(ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST);
        newLecture1.setAuditorium(notExistAuditorium);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithInvalidSubject_ThenException() {
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));

        Lecture newLecture2 = ConstantsTestLecture.newValidLecture();
        newLecture2.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_2);
        assertNull(this.testEntityManager.getId(newLecture2));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture2));

        Lecture newLecture3 = ConstantsTestLecture.newValidLecture();
        newLecture3.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_EMPTY);
        assertNull(this.testEntityManager.getId(newLecture3));
        assertThrows(ValidationException.class, () -> testRepository.save(newLecture3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture3));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNotExistCourse_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        newLecture1.setCourse(notExistCourse);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNotExistGroup_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        Group notExistGroup = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        notExistGroup.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        newLecture1.addGroup(notExistGroup);
        assertTrue(newLecture1.getGroups().size() > 0);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithNotExistLecturer_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        Employee notExistLecturer = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        notExistLecturer.setId(ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST);
        newLecture1.setLecturer(notExistLecturer);
        assertNull(this.testEntityManager.getId(newLecture1));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureAndLecturerIsBusyWithAnotherLecture_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long validAuditoriumId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Auditorium validAuditorium = ConstantsTestAuditorium.getTestAuditorium(validAuditoriumId);
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Lecture> existLecture = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(ConstantsTestLecture.LECTURE_ID_VALID_2);
            assertTrue(lecture.isPresent());
            assertNotEquals(validAuditorium, lecture.get().getAuditorium());
            return lecture;
        });
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setAuditorium(validAuditorium);
        newLecture1.setLectureDate(existLecture.get().getLectureDate());
        newLecture1.setTimeSlot(existLecture.get().getTimeSlot());
        newLecture1.setLecturer(existLecture.get().getLecturer());
        assertNull(this.testEntityManager.getId(newLecture1));
        /*
         * unique constraint "lectures_lecturer_lecture_date_lecture_time_slot_unique"
         */
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddLectureWithOccupiedAuditorium_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Lecture> existLecture = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(ConstantsTestLecture.LECTURE_ID_VALID_2);
            assertTrue(lecture.isPresent());
            return lecture;
        });
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        newLecture1.setAuditorium(existLecture.get().getAuditorium());
        newLecture1.setLectureDate(existLecture.get().getLectureDate());
        newLecture1.setTimeSlot(existLecture.get().getTimeSlot());
        assertNull(this.testEntityManager.getId(newLecture1));
        /*
         * unique constraint
         * "lectures_auditorium_id_lecture_time_slot_lecture_date_unique"
         */
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newLecture1);
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newLecture1));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenAddValidLecture_ThenShouldAddAndReturnAddedEntity() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture newLecture1 = ConstantsTestLecture.newValidLecture();
        assertNull(this.testEntityManager.getId(newLecture1));
        assertTrue(testRepository.save(newLecture1) instanceof Lecture);
        assertNotNull(this.testEntityManager.getId(newLecture1));
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
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findById_WhenNotExistLecture_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestLecture.LECTURE_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findById_WhenExistLecture_ThenReturnOptionalLecture() {
        Long lectureExpectedId = ConstantsTestLecture.LECTURE_ID_VALID_2;
        Lecture lectureExpected = ConstantsTestLecture.getTestLecture(lectureExpectedId);
        lectureExpected.addGroup(ConstantsTestGroup.getTestGroup(1L));
        lectureExpected.addGroup(ConstantsTestGroup.getTestGroup(3L));

        Optional<Lecture> lectureReturned = testRepository.findById(lectureExpectedId);
        assertTrue(lectureReturned.isPresent());
        assertThat(lectureReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lectureExpected);
        assertEquals(lectureReturned.get().getAuditorium(), lectureExpected.getAuditorium());
        assertEquals(lectureReturned.get().getCourse(), lectureExpected.getCourse());
        assertThat(lectureReturned.get().getGroups())
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(lectureExpected.getGroups());
        assertEquals(lectureReturned.get().getLecturer(), lectureExpected.getLecturer());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsLectures_ThenReturnEmptyListLectures() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAll_WhenExistsLectures_ThenReturnListLectures() {
        List<Lecture> lecturesExpected = ConstantsTestLecture.getAllTestLectures();

        List<Lecture> lecturesReturned = testRepository.findAll();
        assertThat(lecturesReturned).isNotEmpty();
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithNotExistAuditorium_ThenException() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Auditorium notExistAuditorium = ConstantsTestAuditorium
                .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);
        notExistAuditorium.setId(ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST);
        lectureForUpdate.get().setAuditorium(notExistAuditorium);
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithInvalidSubject_ThenException() {
        long lectureForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;

        Optional<Lecture> lectureForUpdate1 = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate1.isPresent());
        lectureForUpdate1.get().setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(lectureForUpdate1.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Lecture> lectureForUpdate2 = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate2.isPresent());
        lectureForUpdate2.get().setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(lectureForUpdate2.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Lecture> lectureForUpdate3 = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate3.isPresent());
        lectureForUpdate3.get().setSubject(ConstantsTestLecture.LECTURE_SUBJECT_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(lectureForUpdate3.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithNotExistCourse_ThenException() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Course notExistCourse = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        notExistCourse.setId(ConstantsTestCourse.COURSE_ID_NOT_EXIST);
        lectureForUpdate.get().setCourse(notExistCourse);
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithNotExistGroup_ThenException() {
        Group notExistGroup = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        notExistGroup.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Lecture> lectureForUpdate = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(lectureForUpdateId);
            assertTrue(lecture.isPresent());
            assertFalse(lecture.get().getGroups().contains(notExistGroup));
            return lecture;
        });
        lectureForUpdate.get().addGroup(notExistGroup);
        assertTrue(lectureForUpdate.get().getGroups().contains(notExistGroup));
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithNotExistLecturer_ThenException() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Employee notExistLecturer = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        notExistLecturer.setId(ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST);
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        lectureForUpdate.get().setLecturer(notExistLecturer);
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureAndLecturerIsBusyWithAnotherLecture_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Lecture> existLecture = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(ConstantsTestLecture.LECTURE_ID_VALID_2);
            assertTrue(lecture.isPresent());
            return lecture;
        });
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(lectureForUpdateId);
            assertTrue(lecture.isPresent());
            return lecture;
        });
        lectureForUpdate.get().setLectureDate(existLecture.get().getLectureDate());
        lectureForUpdate.get().setTimeSlot(existLecture.get().getTimeSlot());
        lectureForUpdate.get().setLecturer(existLecture.get().getLecturer());
        /*
         * unique constraint "lectures_lecturer_lecture_date_lecture_time_slot_unique"
         */
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithOccupiedAuditorium_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Lecture> existLecture = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(ConstantsTestLecture.LECTURE_ID_VALID_2);
            assertTrue(lecture.isPresent());
            return lecture;
        });
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = transactionTemplate.execute(status -> {
            Optional<Lecture> lecture = testRepository.findById(lectureForUpdateId);
            assertTrue(lecture.isPresent());
            return lecture;
        });
        lectureForUpdate.get().setLectureDate(existLecture.get().getLectureDate());
        lectureForUpdate.get().setTimeSlot(existLecture.get().getTimeSlot());
        lectureForUpdate.get().setAuditorium(existLecture.get().getAuditorium());
        /*
         * unique constraint
         * "lectures_auditorium_id_lecture_time_slot_lecture_date_unique"
         */
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(lectureForUpdate.get());
                    this.testEntityManager.flush();
                },
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidAuditorium_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Auditorium auditoriumLectureBeforeUpdate = lectureForUpdate.get().getAuditorium();
        assertNotEquals(ConstantsTestLecture.LECTURE_AUDITORIUM_FOR_UPDATE, lectureForUpdate.get().getAuditorium());
        lectureForUpdate.get().setAuditorium(ConstantsTestLecture.LECTURE_AUDITORIUM_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(auditoriumLectureBeforeUpdate, lectureAfterUpdate.get().getAuditorium());
        assertEquals(ConstantsTestLecture.LECTURE_AUDITORIUM_FOR_UPDATE, lectureAfterUpdate.get().getAuditorium());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidSubject_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        String subjectLectureBeforeUpdate = lectureForUpdate.get().getSubject();
        assertNotEquals(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE, lectureForUpdate.get().getSubject());
        lectureForUpdate.get().setSubject(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(subjectLectureBeforeUpdate, lectureAfterUpdate.get().getSubject());
        assertEquals(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE, lectureAfterUpdate.get().getSubject());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidCourse_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Course courseLectureBeforeUpdate = lectureForUpdate.get().getCourse();
        assertNotEquals(ConstantsTestLecture.LECTURE_COURSE_FOR_UPDATE, lectureForUpdate.get().getCourse());
        lectureForUpdate.get().setCourse(ConstantsTestLecture.LECTURE_COURSE_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(courseLectureBeforeUpdate, lectureAfterUpdate.get().getCourse());
        assertEquals(ConstantsTestLecture.LECTURE_COURSE_FOR_UPDATE, lectureAfterUpdate.get().getCourse());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureAndAddValidGroup_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Set<Group> groupsLectureBeforeUpdate = new HashSet<>(lectureForUpdate.get().getGroups());
        assertFalse(lectureForUpdate.get().getGroups().contains(ConstantsTestLecture.LECTURE_GROUP_FOR_ADD));
        lectureForUpdate.get().addGroup(ConstantsTestLecture.LECTURE_GROUP_FOR_ADD);
        assertTrue(lectureForUpdate.get().getGroups().contains(ConstantsTestLecture.LECTURE_GROUP_FOR_ADD));
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(groupsLectureBeforeUpdate, lectureAfterUpdate.get().getGroups());
        assertTrue(lectureAfterUpdate.get().getGroups().contains(ConstantsTestLecture.LECTURE_GROUP_FOR_ADD));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidLecturer_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        Employee lecturerLectureBeforeUpdate = lectureForUpdate.get().getLecturer();
        assertNotEquals(ConstantsTestLecture.LECTURE_LECTURER_FOR_UPDATE, lectureForUpdate.get().getLecturer());
        lectureForUpdate.get().setLecturer(ConstantsTestLecture.LECTURE_LECTURER_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(lecturerLectureBeforeUpdate, lectureAfterUpdate.get().getLecturer());
        assertEquals(ConstantsTestLecture.LECTURE_LECTURER_FOR_UPDATE, lectureAfterUpdate.get().getLecturer());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidLectureDate_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        LocalDate lectureDateLectureBeforeUpdate = lectureForUpdate.get().getLectureDate();
        assertNotEquals(ConstantsTestLecture.LECTURE_LECTURE_DATE_FOR_UPDATE, lectureForUpdate.get().getLectureDate());
        lectureForUpdate.get().setLectureDate(ConstantsTestLecture.LECTURE_LECTURE_DATE_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(lectureDateLectureBeforeUpdate, lectureAfterUpdate.get().getLectureDate());
        assertEquals(ConstantsTestLecture.LECTURE_LECTURE_DATE_FOR_UPDATE, lectureAfterUpdate.get().getLectureDate());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void save_WhenUpdateExistLectureWithValidTimeSlot_ThenShouldUpdate() {
        long lectureForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
        Optional<Lecture> lectureForUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureForUpdate.isPresent());
        TimeSlot timeSlotDateLectureBeforeUpdate = lectureForUpdate.get().getTimeSlot();
        assertNotEquals(ConstantsTestLecture.LECTURE_TIME_SLOT_FOR_UPDATE, lectureForUpdate.get().getTimeSlot());
        lectureForUpdate.get().setTimeSlot(ConstantsTestLecture.LECTURE_TIME_SLOT_FOR_UPDATE);
        testRepository.save(lectureForUpdate.get());
        testEntityManager.flush();
        Optional<Lecture> lectureAfterUpdate = testRepository.findById(lectureForUpdateId);
        assertTrue(lectureAfterUpdate.isPresent());
        assertNotEquals(timeSlotDateLectureBeforeUpdate, lectureAfterUpdate.get().getTimeSlot());
        assertEquals(ConstantsTestLecture.LECTURE_TIME_SLOT_FOR_UPDATE, lectureAfterUpdate.get().getTimeSlot());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteWithNullAsLecture_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.delete(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void delete_WhenDeleteNotExistLecture_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Lecture lectureForDelete = ConstantsTestLecture.newValidLecture(ConstantsTestLecture.LECTURE_ID_NOT_EXIST);
        assertFalse(testRepository.findById(lectureForDelete.getId()).isPresent());
        testRepository.delete(lectureForDelete);
        testEntityManager.flush();
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void delete_WhenDeleteExistLecture_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long lectureForDeleteId = ConstantsTestLecture.LECTURE_ID_FOR_DELETE;
        Optional<Lecture> groupForDelete = testRepository.findById(lectureForDeleteId);
        assertTrue(groupForDelete.isPresent());
        testRepository.delete(groupForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.findById(lectureForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsLectures_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void count_WhenExistsLectures_ThenReturnCountLectures() {
        assertEquals(15L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLectureDateBetween_WhenNotExistsLecturesBetweenLectureDates_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_INVALID))
                .isEmpty();

        // LECTURE_FIND_BY_LECTURE_DATE_2_VALID > LECTURE_FIND_BY_LECTURE_DATE_1_VALID
        assertThat(testRepository
                .findAllByLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLectureDateBetween_WhenExistsLecturesBetweenLectureDates_ThenReturnListLectures() {
        Lecture lecture4 = ConstantsTestLecture.getTestLecture(4L);
        Lecture lecture5 = ConstantsTestLecture.getTestLecture(5L);
        Lecture lecture6 = ConstantsTestLecture.getTestLecture(6L);
        Lecture lecture7 = ConstantsTestLecture.getTestLecture(7L);
        Lecture lecture8 = ConstantsTestLecture.getTestLecture(8L);
        Lecture lecture9 = ConstantsTestLecture.getTestLecture(9L);
        Lecture lecture10 = ConstantsTestLecture.getTestLecture(10L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        lecture4.addGroup(group1);
        lecture5.addGroup(group5);
        lecture6.addGroup(group4);
        lecture7.addGroup(group5);
        lecture8.addGroup(group3);
        lecture9.addGroup(group4);
        lecture10.addGroup(group2);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture4);
        lecturesExpected.add(lecture6);
        lecturesExpected.add(lecture5);
        lecturesExpected.add(lecture7);
        lecturesExpected.add(lecture8);
        lecturesExpected.add(lecture10);
        lecturesExpected.add(lecture9);

        List<Lecture> lecturesReturned = testRepository.findAllByLectureDateBetween(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID);
        assertThat(lecturesReturned).isNotEmpty();
        lecturesReturned.sort(Comparator.comparing(Lecture::getId));
        lecturesExpected.sort(Comparator.comparing(Lecture::getId));
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDate_WhenCalledWithNullAsLecturer_ThenReturnEmptyListLectures() {
        assertThat(testRepository.findAllByLecturerAndLectureDate(
                null,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDate_WhenCalledWithNullAsLectureDate_ThenReturnEmptyListLectures() {
        assertThat(testRepository.findAllByLecturerAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDate_WhenNotExistsLecturesWithLectureInLectureDate_ThenReturnEmptyListLectures() {
        assertThat(testRepository.findAllByLecturerAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();

        assertThat(testRepository.findAllByLecturerAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDate_WhenExistsLecturesWithLectureInLectureDate_ThenReturnListLectures() {
        Lecture lecture4 = ConstantsTestLecture.getTestLecture(4L);
        Lecture lecture6 = ConstantsTestLecture.getTestLecture(6L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        lecture4.addGroup(group1);
        lecture6.addGroup(group4);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture4);
        lecturesExpected.add(lecture6);

        List<Lecture> lecturesReturned = testRepository.findAllByLecturerAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        assertThat(lecturesReturned).isNotEmpty();
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLecturer_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDateBetween_WhenNotExistsLecturesWithLectureInLectureDates_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        // LECTURE_FIND_BY_LECTURE_DATE_2_VALID > LECTURE_FIND_BY_LECTURE_DATE_1_VALID
        assertThat(testRepository
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByLecturerAndLectureDateBetween_WhenExistsLecturesWithLectureBetweenLectureDates_ThenReturnListLectures() {
        Lecture lecture4 = ConstantsTestLecture.getTestLecture(4L);
        Lecture lecture6 = ConstantsTestLecture.getTestLecture(6L);
        Lecture lecture7 = ConstantsTestLecture.getTestLecture(7L);
        Lecture lecture11 = ConstantsTestLecture.getTestLecture(11L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        lecture4.addGroup(group1);
        lecture6.addGroup(group4);
        lecture7.addGroup(group5);
        lecture11.addGroup(group4);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture4);
        lecturesExpected.add(lecture6);
        lecturesExpected.add(lecture7);
        lecturesExpected.add(lecture11);

        List<Lecture> lecturesReturned = testRepository.findAllByLecturerAndLectureDateBetween(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(lecturesReturned).isNotEmpty();
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDate_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .findAllByGroupsContainingAndLectureDate(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDate_WhenCalledWithNullAsLectureDate_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDate_WhenNotExistsLecturesWithGroupInLectureDate_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDate_WhenExistsLecturesWithGroupInLectureDate_ThenReturnListLectures() {
        Lecture lecture12 = ConstantsTestLecture.getTestLecture(12L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        lecture12.addGroup(group1);
        lecture12.addGroup(group3);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture12);

        List<Lecture> lecturesReturned = testRepository.findAllByGroupsContainingAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(lecturesReturned).isNotEmpty();
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDateBetween_WhenNotExistsLecturesWithGroupBetweenLectureDates_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        // LECTURE_FIND_BY_LECTURE_DATE_2_VALID > LECTURE_FIND_BY_LECTURE_DATE_1_VALID
        assertThat(testRepository
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByGroupsContainingAndLectureDateBetween_WhenExistsLecturesWithGroupBetweenLectureDates_ThenReturnListLectures() {
        Lecture lecture8 = ConstantsTestLecture.getTestLecture(8L);
        Lecture lecture12 = ConstantsTestLecture.getTestLecture(12L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        lecture8.addGroup(group3);
        lecture12.addGroup(group1);
        lecture12.addGroup(group3);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture8);
        lecturesExpected.add(lecture12);

        List<Lecture> lecturesReturned = testRepository.findAllByGroupsContainingAndLectureDateBetween(
                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(lecturesReturned).isNotEmpty();
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditorium_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsLectureDate_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestAuditorium.getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1),
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditoriumAndLectureDate_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        null))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenNotExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();

        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnListLectures() {
        Lecture lecture13 = ConstantsTestLecture.getTestLecture(13L);
        Lecture lecture14 = ConstantsTestLecture.getTestLecture(14L);
        Lecture lecture9 = ConstantsTestLecture.getTestLecture(9L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        lecture9.addGroup(group4);
        lecture13.addGroup(group4);
        lecture14.addGroup(group5);
        List<Lecture> lecturesExpected = new ArrayList<>();
        lecturesExpected.add(lecture13);
        lecturesExpected.add(lecture9);
        lecturesExpected.add(lecture14);

        List<Lecture> lecturesReturned = testRepository.findAllByAuditoriumAndLectureDateGreaterThanEqual(
                ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID);
        assertThat(lecturesReturned)
                .isNotEmpty()
                .hasSameElementsAs(lecturesExpected);
        lecturesReturned.sort(Comparator.comparing(Lecture::getId));
        lecturesExpected.sort(Comparator.comparing(Lecture::getId));
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithPageableAndNullAsAuditorium_ThenReturnEmptyListLectures() {
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        PAGE_REQUEST))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithPageableAndNullAsLectureDate_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestAuditorium.getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1),
                        null,
                        PAGE_REQUEST))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithPageableAndNullAsAuditoriumAndLectureDate_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        null,
                        PAGE_REQUEST))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithPageableAndNotExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnEmptyListLectures() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        PAGE_REQUEST))
                .isEmpty();

        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        PAGE_REQUEST))
                .isEmpty();

        assertThat(testRepository
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        PAGE_REQUEST))
                .isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithPageableAndExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnPagedListLectures() {
        Auditorium searchAuditorium = ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID;
        LocalDate searchLectureDate = ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID;
        List<Lecture> lecturesExpected = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getAuditorium().equals(searchAuditorium)
                        && (lecture.getLectureDate().isAfter(searchLectureDate)
                                || lecture.getLectureDate().isEqual(searchLectureDate)))
                .sorted(Comparator.comparing(Lecture::getLectureDate))
                .limit(PAGE_LIMIT)
                .collect(Collectors.toList());

        List<Lecture> lecturesReturned = testRepository.findAllByAuditoriumAndLectureDateGreaterThanEqual(
                searchAuditorium,
                searchLectureDate,
                PAGE_REQUEST);
        assertThat(lecturesReturned)
                .isNotEmpty()
                .hasSameElementsAs(lecturesExpected);
        lecturesReturned.sort(Comparator.comparing(Lecture::getId));
        lecturesExpected.sort(Comparator.comparing(Lecture::getId));
        assertThat(lecturesReturned)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(lecturesExpected);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        lecturesExpected.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : lecturesReturned) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditorium_ThenReturnFalse() {
        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isFalse();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsLectureDate_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestAuditorium.getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1),
                        null))
                .isFalse();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditoriumAndLectureDate_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        null,
                        null))
                .isFalse();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenNotExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isFalse();

        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isFalse();

        assertThat(testRepository
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isFalse();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenExistsLecturesWithAuditoriumAndLectureDateGreaterThanEqual_ThenReturnTrue() {
        assertTrue(testRepository.count() > 0L);
        assertThat(testRepository.existsByAuditoriumAndLectureDateGreaterThanEqual(
                ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID))
                .isTrue();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsGroup_ThenException() {
        assertTrue(testRepository.count() > 0);
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .existsByGroupsContainingAndLectureDateAndTimeSlot(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                                ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsLectureDate_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        null,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsTimeSlot_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsGroupAndLectureDateAndTimeSlot_ThenException() {
        assertTrue(testRepository.count() > 0);
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .existsByGroupsContainingAndLectureDateAndTimeSlot(
                                null,
                                null,
                                null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectures_ThenReturnFalse() {
        assertFalse(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        assertFalse(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_INVALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByGroupsContainingAndLectureDateAndTimeSlot_WhenCalledAndExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnTrue() {
        assertTrue(testRepository.count() > 0);
        assertTrue(testRepository
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsLecturesId_ThenException() {
        assertTrue(testRepository.count() > 0);
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                                ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsGroup_ThenException() {
        assertTrue(testRepository.count() > 0);
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                                Arrays.asList(
                                        ConstantsTestLecture.LECTURE_ID_VALID_1,
                                        ConstantsTestLecture.LECTURE_ID_VALID_2),
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                                ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsLectureDate_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_1,
                                ConstantsTestLecture.LECTURE_ID_VALID_2),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        null,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNullAsTimeSlot_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_1,
                                ConstantsTestLecture.LECTURE_ID_VALID_2),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithNull_ThenException() {
        assertTrue(testRepository.count() > 0);
        assertThrows(DataAccessException.class,
                () -> testRepository
                        .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                                null,
                                null,
                                null,
                                null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectures_ThenReturnFalse() {
        assertFalse(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_1,
                                ConstantsTestLecture.LECTURE_ID_VALID_2),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnFalse() {
        assertTrue(testRepository.count() > 0);
        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_INVALID));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithExistsLectureAndExistLectureIdInLecturesId_ThenReturnFalse() {
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        Lecture existLecture = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        existLecture.addGroup(group5);

        Optional<Lecture> lectureReturned = testRepository.findById(existLecture.getId());
        assertTrue(lectureReturned.isPresent());
        assertThat(lectureReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(existLecture);
        assertEquals(lectureReturned.get().getAuditorium(), existLecture.getAuditorium());
        assertEquals(lectureReturned.get().getCourse(), existLecture.getCourse());
        assertThat(lectureReturned.get().getGroups())
                .isNotEmpty()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(existLecture.getGroups());
        assertEquals(lectureReturned.get().getLecturer(), existLecture.getLecturer());

        assertTrue(existLecture.getGroups().contains(group5));
        assertFalse(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(
                                existLecture.getId(),
                                ConstantsTestLecture.LECTURE_ID_VALID_2,
                                ConstantsTestLecture.LECTURE_ID_FOR_DELETE,
                                ConstantsTestLecture.LECTURE_ID_NOT_EXIST),
                        group5,
                        existLecture.getLectureDate(),
                        existLecture.getTimeSlot()));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = {
            "classpath:testdata_for_Auditorium_in_auditoriums.sql",
            "classpath:testdata_for_Employee_in_persons.sql",
            "classpath:testdata_for_Employee_in_persons_user_roles.sql",
            "classpath:testdata_for_Employee_in_employees.sql",
            "classpath:testdata_for_Course_in_courses.sql",
            "classpath:testdata_for_Group_in_groups.sql",
            "classpath:testdata_for_Group_in_groups_courses.sql",
            "classpath:testdata_for_Lecture_in_lectures.sql",
            "classpath:testdata_for_Lecture_in_lectures_groups.sql" })
    void existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot_WhenCalledWithExistsLectureAndExistLectureIdNotInLecturesId_ThenReturnTrue() {
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        Lecture existLecture = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        existLecture.addGroup(group5);

        Optional<Lecture> lectureReturned = testRepository.findById(existLecture.getId());
        assertTrue(lectureReturned.isPresent());
        assertThat(lectureReturned.get())
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(existLecture);
        assertEquals(lectureReturned.get().getAuditorium(), existLecture.getAuditorium());
        assertEquals(lectureReturned.get().getCourse(), existLecture.getCourse());
        assertThat(lectureReturned.get().getGroups())
                .isNotEmpty()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(existLecture.getGroups());
        assertEquals(lectureReturned.get().getLecturer(), existLecture.getLecturer());

        assertTrue(existLecture.getGroups().contains(group5));
        assertTrue(testRepository
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_2,
                                ConstantsTestLecture.LECTURE_ID_FOR_DELETE,
                                ConstantsTestLecture.LECTURE_ID_NOT_EXIST),
                        group5,
                        existLecture.getLectureDate(),
                        existLecture.getTimeSlot()));
    }

}
