package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;

import jakarta.validation.ConstraintViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.repository.PersonRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @MockBean
    PersonRepository testRepositoryMock;

    @Autowired
    PersonServiceImpl testService;

    private final long entityForTestId = ConstantsTestStudent.STUDENT_ID_VALID_1;
    private Person entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestStudent.getTestStudent(entityForTestId);
        Group group = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        ((Student) entityForTest).setGroup(group);
    }

    @Test
    void findAll_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.findAll()).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.findAll(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAll_WhenCalledAndNotExistsPersons_ThenReturnEmptyList() {
        List<Person> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock.findAll())
                .thenReturn(resultMockito);

        List<Person> resultService = testService.findAll();
        assertThat(resultService).isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAll_WhenCalledAndExistsPersons_ThenReturnPersonsList() {
        List<Person> resultMockito = new ArrayList<>();
        resultMockito.addAll(ConstantsTestStudent.getAllTestStudents());
        resultMockito.addAll(ConstantsTestEmployee.getAllTestEmployees());

        Mockito.when(testRepositoryMock.findAll())
                .thenReturn(resultMockito);

        List<Person> resultService = testService.findAll();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void count_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.count()).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.count(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsPersons_ThenReturnZero() {
        Mockito.when(testRepositoryMock.count())
                .thenReturn(0L);

        assertEquals(0L, testService.count());

        Mockito.verify(testRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void count_WhenCalledAndExistsPersons_ThenReturnPersonsCount() {
        long resultMockito = ConstantsTestStudent.getAllTestStudents().size();
        resultMockito += ConstantsTestEmployee.getAllTestEmployees().size();

        Mockito.when(testRepositoryMock.count())
                .thenReturn(resultMockito);

        assertEquals(resultMockito, testService.count());

        Mockito.verify(testRepositoryMock, Mockito.times(1)).count();
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
    void existsByUsername_WhenCalledAndNotExistPersonWithSuchUsername_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestStudent.STUDENT_USERNAME_VALID))
                .thenReturn(false);

        assertFalse(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndExistPersonWithSuchUsername_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestStudent.STUDENT_USERNAME_VALID))
                .thenReturn(true);

        assertTrue(testService.existsByUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

}
