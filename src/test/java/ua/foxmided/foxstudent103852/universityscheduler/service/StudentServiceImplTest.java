package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.repository.StudentRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @MockBean
    StudentRepository testRepositoryMock;

    @Autowired
    StudentServiceImpl testService;

    private final long entityForTestId = ConstantsTestStudent.STUDENT_ID_VALID_1;
    private Student entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        entityForTest.setGroup(group);
    }

    @Test
    void add_WhenCalledWithNullAsStudent_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithInvalidStudent_ThenException() {
        entityForTest.setId(null);
        entityForTest.setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.add(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidStudent_ThenAddAndReturnAddedEntity() {
        entityForTest.setId(null);
        Student resultMockito = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        resultMockito.setGroup(group);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenReturn(resultMockito);

        Student resultService = testService.add(entityForTest);
        assertNotNull(resultService.getId());
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithDuplicatedValidStudent_ThenException() {
        entityForTest.setId(null);
        Student resultMockito = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        resultMockito.setGroup(group);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsStudent_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithInvalidStudent_ThenException() {
        entityForTest.setLastName(ConstantsTestStudent.STUDENT_LASTNAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.update(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotExistStudent_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsStudentId_ThenException() {
        entityForTest.setId(null);

        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistStudent_ThenUpdateAndReturnUpdatedEntity() {
        Student resultMockito = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        resultMockito.setGroup(group);

        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        Student resultService = testService.update(entityForTest);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithDuplicatedValidStudent_ThenException() {
        Mockito.when(testRepositoryMock.existsById(Mockito.any(Long.class)))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Student.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Student.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsStudent_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.delete(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNotExistStudent_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistStudent_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsStudentId_ThenException() {
        entityForTest.setId(null);

        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).delete(Mockito.any(Student.class));

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(ConstantsTestStudent.STUDENT_ID_VALID_1))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsById(ConstantsTestStudent.STUDENT_ID_VALID_1),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(false);

        assertFalse(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndExistStudentWithSuchId_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(true);

        assertTrue(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestStudent.STUDENT_USERNAME_VALID))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledWithNullAsUsername_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByUsername(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledWithEmptyStringAsUsername_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByUsername(ConstantsTest.EMPTY_STRING),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndNotExistStudentWithSuchUsername_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestStudent.STUDENT_USERNAME_VALID))
                .thenReturn(false);

        assertFalse(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndExistStudentWithSuchUsername_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestStudent.STUDENT_USERNAME_VALID))
                .thenReturn(true);

        assertTrue(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateEmail(null, ConstantsTestStudent.STUDENT_EMAIL_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updateEmail_WhenCalledWithInvalidEmail_ThenException(String parameterizedEmail) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateEmail(ConstantsTestStudent.STUDENT_ID_VALID_1, parameterizedEmail),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateEmail(
                        ConstantsTestStudent.STUDENT_ID_INVALID,
                        ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateEmail(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE)))
                .thenReturn(0);

        assertFalse(testService.updateEmail(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndExistStudentWithSuchIdAndValidEmail_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updateEmail(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE)))
                .thenReturn(1);

        assertTrue(testService.updateEmail(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updatePassword(null, ConstantsTestStudent.STUDENT_PASSWORD_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updatePassword_WhenCalledWithInvalidPassword_ThenException(String parameterizedPassword) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updatePassword(ConstantsTestStudent.STUDENT_ID_VALID_1, parameterizedPassword),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updatePassword(
                        ConstantsTestStudent.STUDENT_ID_VALID_1,
                        ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updatePassword(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE)))
                .thenReturn(0);

        assertFalse(testService.updatePassword(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndExistStudentWithSuchIdAndValidPassword_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updatePassword(
                Mockito.anyLong(),
                Mockito.anyString()))
                .thenReturn(1);

        assertTrue(testService.updatePassword(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(null, entityForTest),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsStudentProfile_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(ConstantsTestStudent.STUDENT_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithInvalidStudentProfileAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber())))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateProfile(ConstantsTestStudent.STUDENT_ID_VALID_1, entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber())))
                .thenReturn(0);

        assertFalse(testService.updateProfile(ConstantsTestStudent.STUDENT_ID_VALID_1, entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledAndExistStudentWithSuchIdAndValidStudentProfile_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber())))
                .thenReturn(1);

        assertTrue(testService.updateProfile(ConstantsTestStudent.STUDENT_ID_VALID_1, entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroup_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateGroup(null,
                        ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1)),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateGroup_WhenCalledWithNullAsGroup_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateGroup(ConstantsTestStudent.STUDENT_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateGroup_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateGroup(Mockito.anyLong(), Mockito.any(Group.class))).thenReturn(0);

        assertFalse(testService.updateGroup(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1)));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroup_WhenCalledAndExistStudentAndNotExistGroup_ThenException() {
        Mockito.when(testRepositoryMock.updateGroup(Mockito.anyLong(), Mockito.any(Group.class)))
                .thenThrow(DataIntegrityViolationException.class);
        Group group = new Group();
        group.setId(ConstantsTestGroup.GROUP_ID_NOT_EXIST);

        assertThrows(DataProcessingException.class,
                () -> testService.updateGroup(ConstantsTestStudent.STUDENT_ID_VALID_1, group),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateGroup_WhenCalledAndExistStudentWithSuchIdAndValidGroup_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updateGroup(Mockito.anyLong(), Mockito.any(Group.class))).thenReturn(1);

        assertTrue(testService.updateGroup(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1)));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateLocked_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateLocked(null,
                        ConstantsTestStudent.STUDENT_LOCKED_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateLocked_WhenCalledAndNotExistStudentWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean())).thenReturn(0);

        assertFalse(testService.updateLocked(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_LOCKED_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "false, 0",
            "true, 1"
    })
    void updateLocked_WhenCalledAndExistStudentWithSuchIdAndUpdatedOrNotUpdatedLocked_ThenReturnTrueOrFalse(
            boolean methodServiceResult, int methodMockitoResult) {
        Mockito.when(testRepositoryMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(methodMockitoResult);

        assertEquals(methodServiceResult,
                testService.updateLocked(
                        ConstantsTestStudent.STUDENT_ID_VALID_1,
                        ConstantsTestStudent.STUDENT_LOCKED_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledWithNullAsStudentId_ThenException() {
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);

        assertThrows(ConstraintViolationException.class,
                () -> testService.updateUserRoles(null, userRolesForUpdate),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updateUserRoles_WhenCalledWithNotValidUserRoles_ThenException(
            Set<UserRole> methodParameter) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateUserRoles(ConstantsTestStudent.STUDENT_ID_VALID_1, methodParameter),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndNotExistStudentWithSuchId_ThenException() {
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);

        Mockito.when(testRepositoryMock
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(DataProcessingException.class,
                () -> testService.updateUserRoles(ConstantsTestStudent.STUDENT_ID_VALID_1, userRolesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student studentGetMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);

        Mockito.when(testRepositoryMock
                .findById(studentGetMock.getId()))
                .thenReturn(Optional.of(studentGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Student.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateUserRoles(ConstantsTestStudent.STUDENT_ID_VALID_1, userRolesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndExistStudentWithSuchIdAndUpdatedUserRoles_ThenReturnTrue() {
        Student studentGetMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);
        Student studentResultMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        studentResultMock.setUserRoles(new HashSet<>(userRolesForUpdate));
        assertThat(studentGetMock.getUserRoles())
                .isNotEqualTo(studentResultMock.getUserRoles());

        Mockito.when(testRepositoryMock
                .findById(studentGetMock.getId()))
                .thenReturn(Optional.of(studentGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Student.class)))
                .thenReturn(studentResultMock);

        assertTrue(testService.updateUserRoles(ConstantsTestStudent.STUDENT_ID_VALID_1, userRolesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndExistStudentWithSuchIdAndNotUpdatedUserRoles_ThenReturnFalse() {
        Student studentGetMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);
        Student studentResultMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        assertThat(studentGetMock.getUserRoles())
                .isEqualTo(studentGetMock.getUserRoles());
        assertThat(userRolesForUpdate)
                .isNotEqualTo(studentResultMock.getUserRoles());

        Mockito.when(testRepositoryMock
                .findById(studentGetMock.getId()))
                .thenReturn(Optional.of(studentGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Student.class)))
                .thenReturn(studentResultMock);

        assertFalse(testService.updateUserRoles(ConstantsTestStudent.STUDENT_ID_VALID_1, userRolesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

}
