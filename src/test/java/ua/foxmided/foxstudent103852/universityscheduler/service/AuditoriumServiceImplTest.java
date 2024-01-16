package ua.foxmided.foxstudent103852.universityscheduler.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.repository.AuditoriumRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuditoriumServiceImplTest {

    @MockBean
    AuditoriumRepository testRepositoryMock;

    @MockBean
    LectureService lectureServiceMock;

    @Autowired
    AuditoriumServiceImpl testService;

    private final long entityForTestId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
    private Auditorium entityForTest;

    @BeforeEach
    void beforeEach() {
        entityForTest = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
    }

    @Test
    void add_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void add_WhenCalledWithInvalidAuditorium_ThenException() {
        entityForTest.setId(null);
        entityForTest.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.add(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void add_WhenCalledWithValidAuditorium_ThenAddAndReturnAddedEntity() {
        entityForTest.setId(null);
        Auditorium resultMockito = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenReturn(resultMockito);

        Auditorium resultService = testService.add(entityForTest);
        assertNotNull(resultService.getId());
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void add_WhenCalledWithDuplicatedValidAuditorium_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(DataIntegrityViolationException.class);

        EntityAddDataIntegrityViolationException exception = assertThrows(
                EntityAddDataIntegrityViolationException.class,
                () -> testService.add(entityForTest),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Auditorium with such number/availability already exists");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setId(null);

        Mockito.when(testRepositoryMock.save(entityForTest)).thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.add(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithInvalidAuditorium_ThenException() {
        entityForTest.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1);

        assertThrows(ValidationException.class, () -> testService.update(entityForTest),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithNotExistAuditorium_ThenException() {
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testService.update(entityForTest),
                ConstantsTest.ENTITY_NOT_FOUND_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithNullAsAuditoriumId_ThenException() {
        entityForTest.setId(null);

        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithExistAuditoriumAndHappyPath_ThenUpdateAndReturnUpdatedEntity() {
        Auditorium resultMockito = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);

        Mockito.when(testRepositoryMock.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(entityForTest))
                .thenReturn(resultMockito);

        Auditorium resultService = testService.update(entityForTest);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1)).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithDuplicatedValidAuditorium_ThenException() {
        entityForTest.setAvailable(true);
        Auditorium entityForUpdate = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
        entityForUpdate.setAvailable(false);
        Mockito.when(testRepositoryMock.existsById(Mockito.any(Long.class)))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Auditorium.class)))
                .thenThrow(DataIntegrityViolationException.class);

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.update(entityForUpdate),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Auditorium with such number/availability already exists");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1)).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        entityForTest.setAvailable(true);
        Auditorium entityForUpdate = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
        entityForUpdate.setAvailable(false);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Auditorium.class)))
                .thenThrow(BadSqlGrammarException.class);

        assertThrows(DataProcessingException.class, () -> testService.update(entityForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1)).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithExistAuditoriumAndSetAvailableToTrue_ThenUpdateAndReturnUpdatedEntity() {
        entityForTest.setAvailable(false);
        Auditorium entityForUpdate = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
        entityForUpdate.setAvailable(true);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Auditorium.class)))
                .thenReturn(entityForUpdate);

        Auditorium resultService = testService.update(entityForUpdate);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(entityForUpdate);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.never()).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithExistAuditoriumWithoutAssignedLecturesAndSetAvailableToFalse_ThenUpdateAndReturnUpdatedEntity() {
        Auditorium entityForUpdate = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
        entityForUpdate.setAvailable(false);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(false);
        Mockito.when(testRepositoryMock.save(Mockito.any(Auditorium.class)))
                .thenReturn(entityForUpdate);

        Auditorium resultService = testService.update(entityForUpdate);
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(entityForUpdate);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).save(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1)).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void update_WhenCalledWithExistAuditoriumWithAssignedLecturesAndSetAvailableToFalse_ThenException() {
        Auditorium entityForUpdate = ConstantsTestAuditorium.getTestAuditorium(entityForTestId);
        entityForUpdate.setAvailable(false);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(true);
        Mockito.when(lectureServiceMock.existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class)))
                .thenReturn(true);
        Mockito.when(testRepositoryMock.save(Mockito.any(Auditorium.class)))
                .thenReturn(entityForUpdate);

        EntityUpdateDataIntegrityViolationException exception = assertThrows(
                EntityUpdateDataIntegrityViolationException.class,
                () -> testService.update(entityForUpdate),
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase(
                        "Before making Auditorium unavailable, reassign active Lectures to another Auditorium(s)");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1)).existsByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class), Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledWithNullAsAuditorium_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.delete(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledWithNotExistAuditorium_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Auditorium.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledWithExistAuditoriumAndHappyPath_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.delete(entityForTest));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Auditorium.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledWithNullAsAuditoriumId_ThenException() {
        entityForTest.setId(null);

        Mockito.doNothing().when(testRepositoryMock).delete(entityForTest);
        doThrow(IllegalArgumentException.class).when(testRepositoryMock).existsById(null);

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Auditorium.class));
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(null);
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).delete(Mockito.any(Auditorium.class));

        assertThrows(DataProcessingException.class, () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void delete_WhenCalledWithExistAuditoriumAndAssignedLectures_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).delete(entityForTest);

        EntityDataIntegrityViolationException exception = assertThrows(
                EntityDataIntegrityViolationException.class,
                () -> testService.delete(entityForTest),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Before deleting Auditorium, reassign Lectures to another Auditorium(s)");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).delete(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void deleteById_WhenCalledWithNullAsAuditoriumId_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> testService.deleteById(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verifyNoInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void deleteById_WhenCalledWithNotExistAuditorium_ThenNothingToDeletedAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).deleteById(entityForTestId);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.deleteById(entityForTestId));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void deleteById_WhenCalledWithExistAuditoriumAndHappyPath_ThenDeleteAndReturnTrue() {
        Mockito.doNothing().when(testRepositoryMock).deleteById(entityForTestId);
        Mockito.when(testRepositoryMock.existsById(entityForTest.getId()))
                .thenReturn(false);

        assertTrue(testService.deleteById(entityForTestId));

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(testRepositoryMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void deleteById_WhenCalledAndThrowsDataAccessException_ThenException() {
        doThrow(BadSqlGrammarException.class).when(testRepositoryMock).deleteById(Mockito.anyLong());

        assertThrows(DataProcessingException.class, () -> testService.deleteById(entityForTestId),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void deleteById_WhenCalledWithExistAuditoriumAndAssignedLectures_ThenException() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(testRepositoryMock).deleteById(entityForTestId);

        EntityDataIntegrityViolationException exception = assertThrows(
                EntityDataIntegrityViolationException.class,
                () -> testService.deleteById(entityForTestId),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        assertThat(exception.getMessage())
                .containsIgnoringCase("Before deleting Auditorium, reassign Lectures to another Auditorium(s)");

        Mockito.verify(testRepositoryMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void findAllByAvailable_WhenCalledAndNotExistsAuditoriums_ThenReturnEmptyListAuditoriums() {
        List<Auditorium> resultMockito = new ArrayList<>();

        Mockito.when(testRepositoryMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(resultMockito);

        assertThat(testService.findAllByAvailable(true)).isEmpty();

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

    @ParameterizedTest
    @CsvSource({
            "false",
            "true"
    })
    void findAllByAvailable_WhenCalledAndExistsAuditoriumsWithSuchAvailable_ThenReturnListAuditoriums(
            boolean parameterizedAvailable) {
        List<Auditorium> resultMockito = ConstantsTestAuditorium.getAllTestAuditoriums()
                .stream()
                .filter(auditorium -> auditorium.isAvailable() == parameterizedAvailable)
                .toList();

        Mockito.when(testRepositoryMock.findAllByAvailable(parameterizedAvailable))
                .thenReturn(resultMockito);

        List<Auditorium> resultService = testService.findAllByAvailable(parameterizedAvailable);
        assertThat(resultService).isNotEmpty();
        assertThat(resultService)
                .usingRecursiveComparison()
                .isEqualTo(resultMockito);

        Mockito.verify(testRepositoryMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(testRepositoryMock);
    }

}
