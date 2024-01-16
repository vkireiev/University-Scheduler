package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.repository.GroupRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @MockBean
    GroupRepository testRepositoryMock;

    @Autowired
    GroupServiceImpl testService;

    private final long entityForTestId = ConstantsTestGroup.GROUP_ID_VALID_1;
    private Group entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestGroup.getTestGroup(entityForTestId);
    }

    @Test
    void add_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithInvalidGroup_ThenException() {
        entityForTest.setId(null);
        entityForTest.setName(ConstantsTestGroup.GROUP_NAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.add(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidGroup_ThenAddAndReturnAddedEntity() {
        entityForTest.setId(null);
        Group resultMockito = ConstantsTestGroup.getTestGroup(entityForTestId);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenReturn(resultMockito);

        Group resultService = testService.add(entityForTest);
        assertNotNull(resultService.getId());
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithInvalidGroup_ThenException() {
        entityForTest.setName(ConstantsTestGroup.GROUP_NAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.update(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotExistGroup_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsGroupId_ThenException() {
        entityForTest.setId(null);

        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistGroup_ThenUpdateAndReturnUpdatedEntity() {
        Group resultMockito = ConstantsTestGroup.getTestGroup(entityForTestId);

        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        Group resultService = testService.update(entityForTest);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Group.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.delete(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNotExistGroup_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Group.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistGroupAndHappyPath_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Group.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsGroupId_ThenException() {
        entityForTest.setId(null);

        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Group.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).delete(Mockito.any(Group.class));

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistGroupAndAssignedStudentOrCourse_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).delete(entityForTest);

        assertThrows(EntityDataIntegrityViolationException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithNullAsGroupId_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.deleteById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithNotExistGroup_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).deleteById(Mockito.anyLong());
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong()))
                .thenReturn(false);

        assertTrue(testService.deleteById(entityForTestId));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithExistGroupAndHappyPath_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).deleteById(Mockito.anyLong());
        Mockito.when(testRepositoryMock.existsById(entityForTestId))
                .thenReturn(false);

        assertTrue(testService.deleteById(entityForTestId));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).deleteById(Mockito.anyLong());

        assertThrows(DataProcessingException.class, () -> testService.deleteById(entityForTestId),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithExistGroupAndAssignedStudentOrCourse_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).deleteById(Mockito.anyLong());

        assertThrows(EntityDataIntegrityViolationException.class, () -> testService.deleteById(entityForTestId),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledWithNullAsGroupId_ThenException() {
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));

        assertThrows(ConstraintViolationException.class,
                () -> testService.updateCourses(null, coursesForUpdate),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledWithNotValidCourses_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateCourses(ConstantsTestGroup.GROUP_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndNotExistGroupWithSuchId_ThenException() {
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(DataProcessingException.class,
                () -> testService.updateCourses(ConstantsTestGroup.GROUP_ID_VALID_1, coursesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndThrowsDataAccessException_ThenException() {
        Group groupGetMock = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock
                .findById(groupGetMock.getId()))
                .thenReturn(Optional.of(groupGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Group.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateCourses(ConstantsTestGroup.GROUP_ID_VALID_1, coursesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndExistGroupWithSuchIdAndUpdatedCourses_ThenReturnTrue() {
        Group groupGetMock = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));
        Group groupResultMock = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        groupResultMock.setCourses(new HashSet<>(coursesForUpdate));
        assertThat(groupGetMock.getCourses())
                .isNotEqualTo(groupResultMock.getCourses());

        Mockito.when(testRepositoryMock
                .findById(groupGetMock.getId()))
                .thenReturn(Optional.of(groupGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Group.class)))
                .thenReturn(groupResultMock);

        assertTrue(testService.updateCourses(ConstantsTestGroup.GROUP_ID_VALID_1, coursesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndExistGroupWithSuchIdAndNotUpdatedCourses_ThenReturnFalse() {
        Group groupGetMock = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));
        Group groupResultMock = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        assertThat(groupGetMock.getCourses())
                .isEqualTo(groupGetMock.getCourses());
        assertThat(coursesForUpdate)
                .isNotEqualTo(groupResultMock.getCourses());

        Mockito.when(testRepositoryMock
                .findById(groupGetMock.getId()))
                .thenReturn(Optional.of(groupGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Group.class)))
                .thenReturn(groupResultMock);

        assertFalse(testService.updateCourses(ConstantsTestGroup.GROUP_ID_VALID_1, coursesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

}
