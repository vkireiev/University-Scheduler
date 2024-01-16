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
import java.util.HashSet;
import java.util.List;
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
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.repository.EmployeeRepository;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @MockBean
    EmployeeRepository testRepositoryMock;

    @Autowired
    EmployeeServiceImpl testService;

    private final long entityForTestId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
    private Employee entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestEmployee.getTestEmployee(entityForTestId);
    }

    @Test
    void add_WhenCalledWithNullAsEmployee_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithInvalidEmployee_ThenException() {
        entityForTest.setId(null);
        entityForTest.setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.add(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidEmployee_ThenAddAndReturnAddedEntity() {
        entityForTest.setId(null);
        Employee resultMockito = ConstantsTestEmployee.getTestEmployee(entityForTestId);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenReturn(resultMockito);

        Employee resultService = testService.add(entityForTest);
        assertNotNull(resultService.getId());
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledWithDuplicatedValidEmployee_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsEmployee_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithInvalidEmployee_ThenException() {
        entityForTest.setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.update(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotExistEmployee_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsEmployeeId_ThenException() {
        entityForTest.setId(null);

        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistEmployee_ThenUpdateAndReturnUpdatedEntity() {
        Employee resultMockito = ConstantsTestEmployee.getTestEmployee(entityForTestId);

        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        Employee resultService = testService.update(entityForTest);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledWithDuplicatedValidEmployee_ThenException() {
        Mockito.when(testRepositoryMock.existsById(Mockito.any(Long.class)))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Employee.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Employee.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsEmployee_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.delete(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNotExistEmployee_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Employee.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistEmployee_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Employee.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsEmployeeId_ThenException() {
        entityForTest.setId(null);

        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Employee.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).delete(Mockito.any(Employee.class));

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistEmployeeAndAssignedCourse_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).delete(entityForTest);

        assertThrows(EntityDataIntegrityViolationException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.deleteById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithNotExistEmployee_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).deleteById(Mockito.anyLong());
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong()))
                .thenReturn(false);

        assertTrue(testService.deleteById(entityForTestId));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void deleteById_WhenCalledWithExistEmployeeAndHappyPath_ThenDeleteAndReturnTrue() {
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
    void deleteById_WhenCalledWithExistEmployeeAndAssignedStudentOrCourse_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).deleteById(Mockito.anyLong());

        assertThrows(EntityDataIntegrityViolationException.class, () -> testService.deleteById(entityForTestId),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByEmployeeType_WhenCalledWithNullAsEmployeeType_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.findAllByEmployeeType(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void findAllByEmployeeType_WhenCalledAndNotExistsEmployeesWithEmployeeType_ThenReturnEmptyListEmployees() {
        List<Employee> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(resultMockito);

        assertThat(testService.findAllByEmployeeType(EmployeeType.EMPLOYEE)).isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void findAllByEmployeeType_WhenCalledAndExistsEmployeesWithEmployeeType_ThenReturnListEmployeesWithEmployeeType() {
        List<Employee> resultMockito = new ArrayList<>(Arrays.asList(
                ConstantsTestEmployee.getTestEmployee(26L),
                ConstantsTestEmployee.getTestEmployee(27L),
                ConstantsTestEmployee.getTestEmployee(28L)));

        Mockito.when(testRepositoryMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(resultMockito);

        List<Employee> resultService = testService.findAllByEmployeeType(EmployeeType.EMPLOYEE);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void countByEmployeeType_WhenCalledWithNullAsEmployeeType_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.countByEmployeeType(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void countByEmployeeType_WhenCalledAndNotExistsEmployeesWithEmployeeType_ThenReturnZero() {
        long resultMockito = 0L;

        Mockito.when(testRepositoryMock.countByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(resultMockito);

        assertEquals(resultMockito, testService.countByEmployeeType(EmployeeType.EMPLOYEE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).countByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void countByEmployeeType_WhenCalledAndExistsEmployeesWithEmployeeType_ThenReturnCountEmployeesWithEmployeeType() {
        long resultMockito = 3L;

        Mockito.when(testRepositoryMock.countByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(resultMockito);

        assertEquals(resultMockito, testService.countByEmployeeType(EmployeeType.EMPLOYEE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).countByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsById(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsById(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(false);

        assertFalse(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsById_WhenCalledAndExistEmployeeWithSuchId_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong())).thenReturn(true);

        assertTrue(testService.existsById(Mockito.anyLong()));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.existsByUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void existsByUsername_WhenCalledWithInvalidUsername_ThenException(String parameterizedUsername) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.existsByUsername(parameterizedUsername),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndNotExistEmployeeWithSuchUsername_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID))
                .thenReturn(false);

        assertFalse(testService.existsByUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void existsByUsername_WhenCalledAndExistEmployeeWithSuchUsername_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.existsByUsernameIgnoringCase(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID))
                .thenReturn(true);

        assertTrue(testService.existsByUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsByUsernameIgnoringCase(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateEmail(null, ConstantsTestEmployee.EMPLOYEE_EMAIL_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updateEmail_WhenCalledWithInvalidEmail_ThenException(String parameterizedEmail) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateEmail(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, parameterizedEmail),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateEmail(
                        ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                        ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateEmail(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE)))
                .thenReturn(0);

        assertFalse(testService.updateEmail(
                ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateEmail_WhenCalledAndExistEmployeeWithSuchIdAndValidEmail_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updateEmail(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE)))
                .thenReturn(1);

        assertTrue(testService.updateEmail(
                ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updatePassword(null, ConstantsTestEmployee.EMPLOYEE_PASSWORD_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updatePassword_WhenCalledWithInvalidPassword_ThenException(String parameterizedPassword) {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updatePassword(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, parameterizedPassword),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updatePassword(
                        ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                        ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updatePassword(
                Mockito.anyLong(),
                Mockito.eq(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE)))
                .thenReturn(0);

        assertFalse(testService.updatePassword(
                ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updatePassword_WhenCalledAndExistEmployeeWithSuchIdAndValidPassword_ThenReturnTrue() {
        Mockito.when(testRepositoryMock.updatePassword(
                Mockito.anyLong(),
                Mockito.anyString()))
                .thenReturn(1);

        assertTrue(testService.updatePassword(
                ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(null, entityForTest),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledWithNullAsEmployeeProfile_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateProfile(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber()),
                        Mockito.any(EmployeeType.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateProfile(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString(),
                        Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledAndNotExistEmployeeWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber()),
                        Mockito.any(EmployeeType.class)))
                .thenReturn(0);

        assertFalse(testService.updateProfile(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString(),
                        Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateProfile_WhenCalledAndExistEmployeeWithSuchIdAndValidEmployeeProfile_ThenReturnTrue() {
        Mockito.when(testRepositoryMock
                .updateProfile(
                        Mockito.anyLong(),
                        Mockito.eq(entityForTest.getFirstName()),
                        Mockito.eq(entityForTest.getLastName()),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(entityForTest.getPhoneNumber()),
                        Mockito.any(EmployeeType.class)))
                .thenReturn(1);

        assertTrue(testService.updateProfile(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(LocalDate.class),
                        Mockito.anyString(),
                        Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateLocked_WhenCalledWithNullAsEmployeeId_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> testService.updateLocked(null,
                        ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateLocked_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(testRepositoryMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateLocked(
                        ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                        ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateLocked_WhenCalledAndExistEmployeeWithSuchId_ThenReturnFalse() {
        Mockito.when(testRepositoryMock.updateLocked(
                Mockito.anyLong(),
                Mockito.anyBoolean()))
                .thenReturn(0);

        assertFalse(testService.updateLocked(
                ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "false, 0",
            "true, 1"
    })
    void updateLocked_WhenCalledAndExistEmployeeWithSuchIdAndUpdatedOrNotUpdatedLocked_ThenReturnTrueOrFalse(
            boolean methodServiceResult, int methodMockitoResult) {
        Mockito.when(testRepositoryMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(methodMockitoResult);

        assertEquals(methodServiceResult,
                testService.updateLocked(
                        ConstantsTestEmployee.EMPLOYEE_ID_VALID_1,
                        ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledWithNullAsEmployeeId_ThenException() {
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
                () -> testService.updateUserRoles(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, methodParameter),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndNotExistEmployeeWithSuchId_ThenException() {
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);

        Mockito.when(testRepositoryMock
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(DataProcessingException.class,
                () -> testService.updateUserRoles(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, userRolesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndThrowsDataAccessException_ThenException() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateUserRoles(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, userRolesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndExistEmployeeWithSuchIdAndUpdatedUserRoles_ThenReturnTrue() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);
        Employee employeeResultMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        employeeResultMock.setUserRoles(new HashSet<>(userRolesForUpdate));
        assertThat(employeeGetMock.getUserRoles())
                .isNotEqualTo(employeeResultMock.getUserRoles());

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenReturn(employeeResultMock);

        assertTrue(testService.updateUserRoles(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, userRolesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateUserRoles_WhenCalledAndExistEmployeeWithSuchIdAndNotUpdatedUserRoles_ThenReturnFalse() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<UserRole> userRolesForUpdate = Set.of(UserRole.VIEWER, UserRole.ADMIN);
        Employee employeeResultMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        assertThat(employeeGetMock.getUserRoles())
                .isEqualTo(employeeGetMock.getUserRoles());
        assertThat(userRolesForUpdate)
                .isNotEqualTo(employeeResultMock.getUserRoles());

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenReturn(employeeResultMock);

        assertFalse(testService.updateUserRoles(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, userRolesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledWithNullAsEmployeeId_ThenException() {
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
                () -> testService.updateCourses(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndNotExistEmployeeWithSuchId_ThenException() {
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(DataProcessingException.class,
                () -> testService.updateCourses(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, coursesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndThrowsDataAccessException_ThenException() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataProcessingException.class,
                () -> testService.updateCourses(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, coursesForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndExistEmployeeWithSuchIdAndUpdatedCourses_ThenReturnTrue() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));
        Employee employeeResultMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        employeeResultMock.setCourses(new HashSet<>(coursesForUpdate));
        assertThat(employeeGetMock.getCourses())
                .isNotEqualTo(employeeResultMock.getCourses());

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenReturn(employeeResultMock);

        assertTrue(testService.updateCourses(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, coursesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @Test
    void updateCourses_WhenCalledAndExistEmployeeWithSuchIdAndNotUpdatedCourses_ThenReturnFalse() {
        Employee employeeGetMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        Set<Course> coursesForUpdate = Set.of(
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1),
                ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE));
        Employee employeeResultMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        assertThat(employeeGetMock.getCourses())
                .isEqualTo(employeeGetMock.getCourses());
        assertThat(coursesForUpdate)
                .isNotEqualTo(employeeResultMock.getCourses());

        Mockito.when(testRepositoryMock
                .findById(employeeGetMock.getId()))
                .thenReturn(Optional.of(employeeGetMock));
        Mockito.when(testRepositoryMock
                .save(Mockito.any(Employee.class)))
                .thenReturn(employeeResultMock);

        assertFalse(testService.updateCourses(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1, coursesForUpdate));

        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1))
                .save(Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

}
