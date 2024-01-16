package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.repository.LectureRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {
    private static final int PAGE_LIMIT = 3;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, PAGE_LIMIT, Sort.by("lectureDate").ascending());

    @MockBean
    LectureRepository testRepositoryMock;

    @Autowired
    LectureServiceImpl testService;

    private final long entityForTestId = ConstantsTestLecture.LECTURE_ID_VALID_1;
    private Lecture entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestLecture.getTestLecture(entityForTestId);
    }

    @Test
    void add_WhenCalledWithNullAsLecture_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithInvalidLecture_ThenException() {
        entityForTest.setId(null);
        entityForTest.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.add(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidLecture_ThenAddAndReturnAddedEntity() {
        entityForTest.setId(null);
        Lecture resultMockito = ConstantsTestLecture.getTestLecture(entityForTestId);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenReturn(resultMockito);

        Lecture resultService = testService.add(entityForTest);
        assertNotNull(resultService.getId());
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledAndTriggeringСonstraints_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenThrow(new DataIntegrityViolationException(Constants.DEFAULT_ERROR_MESSAGE));

        EntityDataIntegrityViolationException exception = assertThrows(EntityDataIntegrityViolationException.class,
                () -> testService.add(entityForTest),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Failed to add Lecture due to restrictions");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsLecture_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithInvalidLecture_ThenException() {
        entityForTest.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.update(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotExistLecture_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testService.update(entityForTest),
                ConstantsTest.ENTITY_NOT_FOUND_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsLectureId_ThenException() {
        entityForTest.setId(null);

        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistLectureAndHappyPath_ThenUpdateAndReturnUpdatedEntity() {
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        entityForTest.addGroup(group5);
        entityForTest.addGroup(group1);
        entityForTest.addGroup(group3);
        Lecture resultMockito = ConstantsTestLecture.getTestLecture(entityForTestId);
        resultMockito.addGroup(group5);
        resultMockito.addGroup(group1);
        resultMockito.addGroup(group3);

        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        Lecture resultService = testService.update(entityForTest);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(entityForTest.getGroups().size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledAndTriggeringСonstraints_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Lecture.class)))
                .thenThrow(new DataIntegrityViolationException(Constants.DEFAULT_ERROR_MESSAGE));

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.update(entityForTest),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .contains("Failed to update Lecture due to restrictions");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistLectureAndExistViolationByGroup_ThenException() {
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        entityForTest.addGroup(group5);
        entityForTest.addGroup(group1);
        entityForTest.addGroup(group3);
        Lecture resultMockito = ConstantsTestLecture.getTestLecture(entityForTestId);
        resultMockito.addGroup(group5);
        resultMockito.addGroup(group1);
        resultMockito.addGroup(group3);

        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.eq(group3),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.update(entityForTest),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .contains("The Group already has a Lecture scheduled at the specified time");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(entityForTest.getGroups().size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Lecture.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsLecture_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.delete(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNotExistLecture_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Lecture.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistLecture_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Lecture.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsLectureId_ThenException() {
        entityForTest.setId(null);

        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Lecture.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).delete(Mockito.any(Lecture.class));

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDate_WhenCalledWithNullAsLecturer_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findAllByLectureDateBetween(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findAllByLectureDateBetween(
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLectureDateBetween_WhenNotExistsLecturesBetweenLectureDates_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_INVALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLectureDateBetween_WhenExistsLecturesWithLectureBetweenLectureDates_ThenReturnListLectures() {
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
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture4);
        resultMockito.add(lecture6);
        resultMockito.add(lecture7);
        resultMockito.add(lecture11);

        Mockito.when(testRepositoryMock
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findAllByLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDate_WhenCalledWithNullAsLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDate_WhenCalledAndNotExistLecturesWithLecturerAndLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        /**/
        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDate_WhenExistsLecturesWithLectureInLectureDate_ThenReturnListLectures() {
        Lecture lecture4 = ConstantsTestLecture.getTestLecture(4L);
        Lecture lecture6 = ConstantsTestLecture.getTestLecture(6L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        lecture4.addGroup(group1);
        lecture6.addGroup(group4);
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture4);
        resultMockito.add(lecture6);

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService.findAllByLecturerAndLectureDate(
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLecturer_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByLecturerAndLectureDateBetween_WhenNotExistsLecturesWithLectureInLectureDates_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        /**/
        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        /**/
        // LECTURE_FIND_BY_LECTURE_DATE_2_VALID > LECTURE_FIND_BY_LECTURE_DATE_1_VALID
        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
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
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture4);
        resultMockito.add(lecture6);
        resultMockito.add(lecture7);
        resultMockito.add(lecture11);

        Mockito.when(testRepositoryMock
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURER_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDate_WhenCalledWithNullAsGroup_ThenException() {
        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenThrow(InvalidDataAccessApiUsageException.class);

        assertThrows(DataProcessingException.class, () -> testService
                .findAllByGroupsContainingAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDate(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDate_WhenCalledWithNullAsLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDate_WhenNotExistsLecturesWithGroupInLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        /**/
        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDate_WhenExistsLecturesWithGroupInLectureDate_ThenReturnListLectures() {
        Lecture lecture12 = ConstantsTestLecture.getTestLecture(12L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        lecture12.addGroup(group1);
        lecture12.addGroup(group3);
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture12);

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDate(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsGroup_ThenException() {
        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenThrow(InvalidDataAccessApiUsageException.class);

        assertThrows(DataProcessingException.class, () -> testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsLectureDateStart_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDateBetween_WhenCalledWithNullAsLectureDateEnd_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDateBetween_WhenNotExistsLecturesWithGroupBetweenLectureDates_ThenReturnListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        /**/
        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        /**/
        // LECTURE_FIND_BY_LECTURE_DATE_2_VALID > LECTURE_FIND_BY_LECTURE_DATE_1_VALID
        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByGroupsContainingAndLectureDateBetween_WhenExistsLecturesWithGroupBetweenLectureDates_ThenReturnListLectures() {
        Lecture lecture8 = ConstantsTestLecture.getTestLecture(8L);
        Lecture lecture12 = ConstantsTestLecture.getTestLecture(12L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        lecture8.addGroup(group3);
        lecture12.addGroup(group1);
        lecture12.addGroup(group3);
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture8);
        resultMockito.add(lecture12);

        Mockito.when(testRepositoryMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsLectureDate_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                                ConstantsTestAuditorium
                                        .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1),
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditoriumAndLectureDate_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenNotExistsLecturesWithAuditoriumAndLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(resultMockito);

        assertThat(testService
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByAuditoriumAndLectureDateGreaterThanEqual_WhenExistsLecturesWithAuditoriumAndLectureDate_ThenReturnListLectures() {
        Lecture lecture13 = ConstantsTestLecture.getTestLecture(13L);
        Lecture lecture14 = ConstantsTestLecture.getTestLecture(14L);
        Lecture lecture9 = ConstantsTestLecture.getTestLecture(9L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        lecture9.addGroup(group4);
        lecture13.addGroup(group4);
        lecture14.addGroup(group5);
        List<Lecture> resultMockito = new ArrayList<>();
        resultMockito.add(lecture13);
        resultMockito.add(lecture9);
        resultMockito.add(lecture14);

        Mockito.when(testRepositoryMock
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID);
        resultService.sort(Comparator.comparing(Lecture::getId));
        resultMockito.sort(Comparator.comparing(Lecture::getId));
        assertThat(resultService)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(resultMockito);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        resultMockito.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : resultService) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                                PAGE_REQUEST),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsLectureDate_ThenException() {
        Auditorium searchAuditorium = ConstantsTestAuditorium
                .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);

        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                                searchAuditorium,
                                null,
                                PAGE_REQUEST),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsPage_ThenException() {
        Auditorium searchAuditorium = ConstantsTestAuditorium
                .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);

        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                                searchAuditorium,
                                null,
                                PAGE_REQUEST),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditoriumAndLectureDateAndPage_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                null,
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenNotExistsLecturesWithAuditoriumAndLectureDate_ThenReturnEmptyListLectures() {
        List<Lecture> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class)))
                .thenReturn(resultMockito);

        assertThat(testService
                .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID,
                        PAGE_REQUEST))
                .isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findPageByAuditoriumAndLectureDateGreaterThanEqual_WhenExistsLecturesWithAuditoriumAndLectureDate_ThenReturnPagedListLectures() {
        Auditorium searchAuditorium = ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID;
        LocalDate searchLectureDate = ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID;
        List<Lecture> resultMockito = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getAuditorium().equals(searchAuditorium)
                        && (lecture.getLectureDate().isAfter(searchLectureDate)
                                || lecture.getLectureDate().isEqual(searchLectureDate)))
                .sorted(Comparator.comparing(Lecture::getLectureDate))
                .limit(PAGE_LIMIT)
                .collect(Collectors.toList());

        Mockito.when(testRepositoryMock
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class)))
                .thenReturn(resultMockito);

        List<Lecture> resultService = testService
                .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID,
                        PAGE_REQUEST);
        resultService.sort(Comparator.comparing(Lecture::getId));
        resultMockito.sort(Comparator.comparing(Lecture::getId));
        assertThat(resultService)
                .usingRecursiveComparison()
                .ignoringFields("auditorium", "course", "groups", "lecturer")
                .isEqualTo(resultMockito);
        Map<Long, Auditorium> lecturesExpectedAuditorium = new HashMap<>();
        Map<Long, Course> lecturesExpectedCourse = new HashMap<>();
        Map<Long, Set<Group>> lecturesExpectedGroups = new HashMap<>();
        Map<Long, Employee> lecturesExpectedLecturer = new HashMap<>();
        resultMockito.forEach(lecture -> {
            lecturesExpectedAuditorium.put(lecture.getId(), lecture.getAuditorium());
            lecturesExpectedCourse.put(lecture.getId(), lecture.getCourse());
            lecturesExpectedGroups.put(lecture.getId(), lecture.getGroups());
            lecturesExpectedLecturer.put(lecture.getId(), lecture.getLecturer());
        });
        for (Lecture lectureReturned : resultService) {
            assertEquals(lecturesExpectedAuditorium.get(lectureReturned.getId()), lectureReturned.getAuditorium());
            assertEquals(lecturesExpectedCourse.get(lectureReturned.getId()), lectureReturned.getCourse());
            assertThat(lectureReturned.getGroups())
                    .usingRecursiveComparison()
                    .usingOverriddenEquals()
                    .isEqualTo(lecturesExpectedGroups.get(lectureReturned.getId()));
            assertEquals(lecturesExpectedLecturer.get(lectureReturned.getId()), lectureReturned.getLecturer());
        }

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findAllByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .existsByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsLectureDate_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .existsByAuditoriumAndLectureDateGreaterThanEqual(
                                ConstantsTestAuditorium
                                        .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1),
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenCalledWithNullAsAuditoriumAndLectureDate_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService
                        .existsByAuditoriumAndLectureDateGreaterThanEqual(
                                null,
                                null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenNotExistsLecturesWithAuditoriumAndLectureDate_ThenReturnFalse() {
        Mockito.when(testRepositoryMock
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .thenReturn(false);

        assertThat(testService
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_2_VALID))
                .isFalse();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByAuditoriumAndLectureDateGreaterThanEqual_WhenExistsLecturesWithGroupBetweenLectureDates_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID))
                .thenReturn(true);

        assertThat(testService.existsByAuditoriumAndLectureDateGreaterThanEqual(
                ConstantsTestLecture.LECTURE_FIND_BY_AUDITORIUM_VALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_3_VALID))
                .isTrue();

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @MethodSource("provideNullArgumentsForExistsByGroupAndLectureDateAndTimeSlot")
    void existsByGroupAndLectureDateAndTimeSlot_WhenCalledWithNullArguments_ThenException(
            Group parameterizedGroup, LocalDate parameterizedLectureDate, TimeSlot parameterizedTimeSlot) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByGroupAndLectureDateAndTimeSlot(
                        parameterizedGroup,
                        parameterizedLectureDate,
                        parameterizedTimeSlot),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByGroupAndLectureDateAndTimeSlot_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsByGroupAndLectureDateAndTimeSlot(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentsForExistsByGroupAndLectureDateAndTimeSlot")
    void existsByGroupAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnFalse(
            Group parameterizedGroup, LocalDate parameterizedLectureDate, TimeSlot parameterizedTimeSlot) {
        Mockito.when(testRepositoryMock
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);

        assertFalse(testService.existsByGroupAndLectureDateAndTimeSlot(
                parameterizedGroup,
                parameterizedLectureDate,
                parameterizedTimeSlot));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByGroupAndLectureDateAndTimeSlot_WhenCalledAndExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(true);

        assertTrue(testService.existsByGroupAndLectureDateAndTimeSlot(
                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentsForExistsByIdNotInAndGroupAndLectureDateAndTimeSlot")
    void existsByIdNotInAndGroupAndLectureDateAndTimeSlot_WhenCalledWithNullArguments_ThenException(
            Collection<Long> parameterizedLecturesId, Group parameterizedGroup,
            LocalDate parameterizedLectureDate, TimeSlot parameterizedTimeSlot) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByIdNotInAndGroupAndLectureDateAndTimeSlot(
                        parameterizedLecturesId,
                        parameterizedGroup,
                        parameterizedLectureDate,
                        parameterizedTimeSlot),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByIdNotInAndGroupAndLectureDateAndTimeSlot_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsByIdNotInAndGroupAndLectureDateAndTimeSlot(
                        Arrays.asList(entityForTest.getId()),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        entityForTest.getLectureDate(),
                        entityForTest.getTimeSlot()),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByIdNotInAndGroupAndLectureDateAndTimeSlot_WhenCalledAndNotExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnFalse() {
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);

        assertFalse(testService.existsByIdNotInAndGroupAndLectureDateAndTimeSlot(
                Arrays.asList(entityForTest.getId()),
                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                entityForTest.getLectureDate(),
                entityForTest.getTimeSlot()));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByIdNotInAndGroupAndLectureDateAndTimeSlot_WhenCalledAndExistsLectureWithSuchGroupAndLectureDateAndTimeSlot_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(true);

        assertTrue(testService.existsByIdNotInAndGroupAndLectureDateAndTimeSlot(
                Arrays.asList(ConstantsTestLecture.LECTURE_ID_NOT_EXIST),
                ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithNullAsLectureId_ThenException() {
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE));

        assertThrows(ConstraintViolationException.class,
                () -> testService.updateGroups(null, groupsForUpdate),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithNullAsGroups_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateGroups(ConstantsTestLecture.LECTURE_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledAndNotExistLectureWithSuchId_ThenException() {
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> testService.updateGroups(ConstantsTestLecture.LECTURE_ID_VALID_1, groupsForUpdate),
                ConstantsTest.ENTITY_NOT_FOUND_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithEmptyGroups_ThenException() {
        Lecture lectureForUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);

        Mockito.when(testRepositoryMock.findById(lectureForUpdateMock.getId()))
                .thenReturn(Optional.of(lectureForUpdateMock));

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.updateGroups(ConstantsTestLecture.LECTURE_ID_VALID_1, new HashSet<>()),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Before deleting Group from the Lecture, add another Group to the Lecture");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithExistLectureAndAddGroupAlreadyScheduledInLocalDateAndTimeSlot_ThenException() {
        Lecture lectureForUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock.findById(lectureForUpdateMock.getId()))
                .thenReturn(Optional.of(lectureForUpdateMock));
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(true);

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.updateGroups(lectureForUpdateMock.getId(), groupsForUpdate),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("The Group already has a Lecture scheduled at the specified time");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(groupsForUpdate.size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithExistLectureAndAddNotExistsGroup_ThenException() {
        Lecture lectureForUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_NOT_EXIST));

        Mockito.when(testRepositoryMock.findById(lectureForUpdateMock.getId()))
                .thenReturn(Optional.of(lectureForUpdateMock));
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Lecture.class)))
                .thenThrow(JpaObjectRetrievalFailureException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateGroups(lectureForUpdateMock.getId(), groupsForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(groupsForUpdate.size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithExistLectureAndValidGroupsAndNotUpdateGroups_ThenReturnFalse() {
        Lecture lectureForUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE));
        Lecture lectureAfterUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureAfterUpdateMock.setGroups(new HashSet<>(lectureForUpdateMock.getGroups()));

        Mockito.when(testRepositoryMock.findById(lectureForUpdateMock.getId()))
                .thenReturn(Optional.of(lectureForUpdateMock));
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Lecture.class)))
                .thenReturn(lectureAfterUpdateMock);

        assertFalse(testService.updateGroups(lectureForUpdateMock.getId(), groupsForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(groupsForUpdate.size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroups_WhenCalledWithExistLectureAndValidGroupsAndUpdateGroups_ThenReturnTrue() {
        Lecture lectureForUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Set<Group> groupsForUpdate = Set.of(
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE));
        Lecture lectureAfterUpdateMock = ConstantsTestLecture.getTestLecture(ConstantsTestLecture.LECTURE_ID_VALID_1);
        lectureAfterUpdateMock.setGroups(new HashSet<>(groupsForUpdate));

        Mockito.when(testRepositoryMock.findById(lectureForUpdateMock.getId()))
                .thenReturn(Optional.of(lectureForUpdateMock));
        Mockito.when(testRepositoryMock
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Lecture.class)))
                .thenReturn(lectureAfterUpdateMock);

        assertTrue(testService.updateGroups(lectureForUpdateMock.getId(), groupsForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.atLeastOnce())
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.atMost(groupsForUpdate.size()))
                .existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                        Mockito.anyCollection(),
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(TimeSlot.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    private static Stream<Arguments> provideNullArgumentsForExistsByGroupAndLectureDateAndTimeSlot() {
        return Stream.of(
                Arguments.of(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        null,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null));
    }

    private static Stream<Arguments> provideInvalidArgumentsForExistsByGroupAndLectureDateAndTimeSlot() {
        return Stream.of(
                Arguments.of(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_INVALID,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_INVALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_2,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_INVALID));
    }

    private static Stream<Arguments> provideInvalidArgumentsForExistsByIdNotInAndGroupAndLectureDateAndTimeSlot() {
        return Stream.of(
                Arguments.of(null, null, null, null),
                Arguments.of(Arrays.asList(), null, null, null),
                Arguments.of(
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        Arrays.asList(),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_1,
                                ConstantsTestLecture.LECTURE_ID_VALID_2),
                        null,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(
                        Arrays.asList(
                                ConstantsTestLecture.LECTURE_ID_VALID_1,
                                ConstantsTestLecture.LECTURE_ID_VALID_2),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        null,
                        ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_2),
                Arguments.of(Arrays.asList(
                        ConstantsTestLecture.LECTURE_ID_VALID_1,
                        ConstantsTestLecture.LECTURE_ID_VALID_2),
                        ConstantsTestLecture.LECTURE_FIND_BY_GROUP_VALID_1,
                        ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID,
                        null));
    }

}
