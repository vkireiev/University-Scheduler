package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import jakarta.validation.ConstraintViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.repository.UserRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @MockBean
    UserRepository testRepositoryMock;

    @Autowired
    UserServiceImpl testService;

    private final long entityForTestId = ConstantsTestStudent.STUDENT_ID_VALID_1;
    private Person entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        ((Student) entityForTest).setGroup(group);
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
    void existsById_WhenCalledWithNullAsPersonId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndNotExistPersonWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(false);

        assertFalse(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndExistPersonWithSuchId_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(true);

        assertTrue(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(Mockito.anyString()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void existsByUsername_WhenCalledWithInvalidUsername_ThenException(
            String parameterizedUsername) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByUsername(parameterizedUsername),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndNotExistPersonWithSuchUsername_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(Mockito.anyString()))
                .thenReturn(false);

        assertFalse(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndExistPersonWithSuchUsername_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(Mockito.anyString()))
                .thenReturn(true);

        assertTrue(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledWithNullAsPersonId_ThenException() {
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
    void updateEmail_WhenCalledAndNotExistPersonWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(0);

        assertFalse(testService.updateEmail(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndExistPersonWithSuchIdAndValidEmail_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1);

        assertTrue(testService.updateEmail(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledWithNullAsPersonId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updatePassword(null, ConstantsTestStudent.STUDENT_PASSWORD_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updatePassword_WhenCalledWithNullAsPassword_ThenException(String parameterizedPassword) {
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
    void updatePassword_WhenCalledAndNotExistPersonWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(0);

        assertFalse(testService.updatePassword(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndExistPersonWithSuchIdAndValidPassword_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1);

        assertTrue(testService.updatePassword(
                ConstantsTestStudent.STUDENT_ID_VALID_1,
                ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsPersonId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(null, entityForTest),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsPersonProfile_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(ConstantsTestStudent.STUDENT_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithInvalidPersonProfileAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString()))
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
    void updateProfile_WhenCalledAndNotExistPersonWithSuchIdAndValidPersonProfileAndNotUpdated_ThenReturnFalse() {
        Mockito.when(testRepositoryMock
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString()))
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
    void updateProfile_WhenCalledAndExistPersonWithSuchIdAndValidPersonProfileAndUpdated_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString()))
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

}
