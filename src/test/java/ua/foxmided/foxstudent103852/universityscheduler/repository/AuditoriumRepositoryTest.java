package ua.foxmided.foxstudent103852.universityscheduler.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.validation.ValidationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuditoriumRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AuditoriumRepository testRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddAuditoriumWithNullAsNumber_ThenException() {
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium1.setNumber(null);
        assertNull(this.testEntityManager.getId(newAuditorium1));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddAuditoriumWithNullAsCapacity_ThenException() {
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium1.setCapacity(null);
        assertNull(this.testEntityManager.getId(newAuditorium1));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium1));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddAuditoriumWithInvalidNumber_ThenException() {
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium1.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_EMPTY);
        assertNull(this.testEntityManager.getId(newAuditorium1));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium1));

        Auditorium newAuditorium2 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium2.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1);
        assertNull(this.testEntityManager.getId(newAuditorium2));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium2));

        Auditorium newAuditorium3 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium3.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_2);
        assertNull(this.testEntityManager.getId(newAuditorium3));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddAuditoriumWithInvalidCapacity_ThenException() {
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium1.setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_1);
        assertNull(this.testEntityManager.getId(newAuditorium1));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium1),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium1));

        Auditorium newAuditorium2 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium2.setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_2);
        assertNull(this.testEntityManager.getId(newAuditorium2));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium2),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium2));

        Auditorium newAuditorium3 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium3.setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_3);
        assertNull(this.testEntityManager.getId(newAuditorium3));
        assertThrows(ValidationException.class, () -> testRepository.save(newAuditorium3),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newAuditorium3));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddValidAuditorium_ThenShouldAddAndReturnAddedEntity() {
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        assertNull(this.testEntityManager.getId(newAuditorium1));
        assertTrue(testRepository.save(newAuditorium1) instanceof Auditorium);
        assertNotNull(this.testEntityManager.getId(newAuditorium1));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenAddDuplicatedValidAuditorium_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long existAuditoriumId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
        Optional<Auditorium> existAuditorium = testRepository.findById(existAuditoriumId);
        assertTrue(existAuditorium.isPresent());
        Auditorium newAuditorium1 = ConstantsTestAuditorium.newValidAuditorium();
        newAuditorium1.setNumber(existAuditorium.get().getNumber());
        newAuditorium1.setAvailable(existAuditorium.get().isAvailable());
        /*
         * unique constraint "auditoriums_number_available_unique"
         */
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(newAuditorium1);
                    testEntityManager.flush();
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
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void findById_WhenNotExistAuditorium_ThenReturnOptionalNull() {
        assertFalse(testRepository.findById(ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void findById_WhenExistAuditorium_ThenReturnOptionalAuditorium() {
        Optional<Auditorium> auditoriumExpected = Optional
                .of(ConstantsTestAuditorium.newValidAuditorium((ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1)));

        Optional<Auditorium> auditoriumReturned = testRepository
                .findById(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);
        assertTrue(auditoriumReturned.isPresent());
        assertThat(auditoriumReturned.get())
                .usingRecursiveComparison()
                .isEqualTo(auditoriumExpected.get());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsAuditoriums_ThenReturnEmptyListAuditoriums() {
        assertThat(testRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void findAll_WhenExistsAuditoriums_ThenReturnListAuditoriums() {
        List<Auditorium> auditoriumsExpected = ConstantsTestAuditorium.getAllTestAuditoriums();

        List<Auditorium> auditoriumsReturned = testRepository.findAll();
        assertThat(auditoriumsReturned).isNotEmpty();
        assertThat(auditoriumsReturned)
                .usingRecursiveComparison()
                .isEqualTo(auditoriumsExpected);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithInvalidNumber_ThenException() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
        Optional<Auditorium> auditoriumForUpdate1 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate1.isPresent());
        auditoriumForUpdate1.get().setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Auditorium> auditoriumForUpdate2 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate2.isPresent());
        auditoriumForUpdate2.get().setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Auditorium> auditoriumForUpdate3 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate3.isPresent());
        auditoriumForUpdate3.get().setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_EMPTY);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithNullAsCapacity_ThenException() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
        Optional<Auditorium> auditoriumForUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate.isPresent());
        auditoriumForUpdate.get().setCapacity(null);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithInvalidCapacity_ThenException() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
        Optional<Auditorium> auditoriumForUpdate1 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate1.isPresent());
        auditoriumForUpdate1.get().setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_1);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate1.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Auditorium> auditoriumForUpdate2 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate2.isPresent());
        auditoriumForUpdate2.get().setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate2.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);

        Optional<Auditorium> auditoriumForUpdate3 = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate3.isPresent());
        auditoriumForUpdate3.get().setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_INVALID_2);
        assertThrows(ValidationException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate3.get());
                    testEntityManager.flush();
                },
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumOnAnotherExistAuditorium_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        long existAuditoriumId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Optional<Auditorium> existAuditorium = transactionTemplate.execute(status -> {
            Optional<Auditorium> auditorium = testRepository.findById(existAuditoriumId);
            assertTrue(auditorium.isPresent());
            return auditorium;
        });
        Optional<Auditorium> auditoriumBeforeAttempt = transactionTemplate.execute(status -> {
            Optional<Auditorium> auditorium = testRepository.findById(auditoriumForUpdateId);
            assertTrue(auditorium.isPresent());
            return auditorium;
        });
        Optional<Auditorium> auditoriumForUpdate = transactionTemplate.execute(status -> {
            Optional<Auditorium> auditorium = testRepository.findById(auditoriumForUpdateId);
            assertTrue(auditorium.isPresent());
            return auditorium;
        });
        assertNotEquals(existAuditorium.get(), auditoriumForUpdate.get());
        auditoriumForUpdate.get().setNumber(existAuditorium.get().getNumber());
        auditoriumForUpdate.get().setCapacity(existAuditorium.get().getCapacity());
        auditoriumForUpdate.get().setAvailable(existAuditorium.get().isAvailable());
        assertThrows(DataAccessException.class,
                () -> {
                    testRepository.save(auditoriumForUpdate.get());
                    this.testEntityManager.flush();
                }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        Optional<Auditorium> auditoriumAfterAttempt = transactionTemplate.execute(status -> {
            Optional<Auditorium> auditorium = testRepository.findById(auditoriumForUpdateId);
            assertTrue(auditorium.isPresent());
            return auditorium;
        });
        assertEquals(auditoriumBeforeAttempt.get(), auditoriumAfterAttempt.get());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithValidNumber_ThenShouldUpdate() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Optional<Auditorium> auditoriumForUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate.isPresent());
        String numberAuditoriumForUpdate = auditoriumForUpdate.get().getNumber();
        assertNotEquals(ConstantsTestAuditorium.AUDITORIUM_NUMBER_FOR_UPDATE, auditoriumForUpdate.get().getNumber());
        auditoriumForUpdate.get().setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_VALID);
        testRepository.save(auditoriumForUpdate.get());
        Optional<Auditorium> auditoriumAfterUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumAfterUpdate.isPresent());
        assertNotEquals(numberAuditoriumForUpdate, auditoriumAfterUpdate.get().getNumber());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithValidCapacity_ThenShouldUpdate() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Optional<Auditorium> auditoriumForUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate.isPresent());
        Short capacityAuditoriumForUpdate = auditoriumForUpdate.get().getCapacity();
        assertNotEquals(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_FOR_UPDATE,
                auditoriumForUpdate.get().getCapacity());
        auditoriumForUpdate.get().setCapacity(ConstantsTestAuditorium.AUDITORIUM_CAPACITY_FOR_UPDATE);
        testRepository.save(auditoriumForUpdate.get());
        Optional<Auditorium> auditoriumAfterUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumAfterUpdate.isPresent());
        assertNotEquals(capacityAuditoriumForUpdate, auditoriumAfterUpdate.get().getCapacity());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void save_WhenUpdateExistAuditoriumWithValidAvailable_ThenShouldUpdate() {
        long auditoriumForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Optional<Auditorium> auditoriumForUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumForUpdate.isPresent());
        boolean availableAuditoriumForUpdate = auditoriumForUpdate.get().isAvailable();
        auditoriumForUpdate.get().setAvailable(!auditoriumForUpdate.get().isAvailable());
        testRepository.save(auditoriumForUpdate.get());
        Optional<Auditorium> auditoriumAfterUpdate = testRepository.findById(auditoriumForUpdateId);
        assertTrue(auditoriumAfterUpdate.isPresent());
        assertNotEquals(availableAuditoriumForUpdate, auditoriumAfterUpdate.get().isAvailable());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteWithNullAsAuditorium_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.delete(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void delete_WhenDeleteNotExistAuditorium_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        Auditorium auditoriumForDelete = ConstantsTestAuditorium
                .newValidAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST);
        assertFalse(testRepository.findById(auditoriumForDelete.getId()).isPresent());
        testRepository.delete(auditoriumForDelete);
        testEntityManager.flush();
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
            "classpath:testdata_for_Lecture_in_lectures.sql" })
    void delete_WhenDeleteExistAuditoriumWithAssignedLectures_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long auditoriumForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;

        transactionTemplate = new TransactionTemplate(transactionManager);
        Optional<Auditorium> auditoriumForDelete = transactionTemplate.execute(status -> {
            Optional<Auditorium> auditorium = testRepository.findById(auditoriumForDeleteId);
            assertTrue(auditorium.isPresent());
            return auditorium;
        });

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.delete(auditoriumForDelete.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(auditoriumForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void delete_WhenDeleteExistAuditoriumWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long auditoriumForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        Optional<Auditorium> auditoriumForDelete = testRepository.findById(auditoriumForDeleteId);
        assertTrue(auditoriumForDelete.isPresent());
        testRepository.delete(auditoriumForDelete.get());
        testEntityManager.flush();
        assertFalse(testRepository.findById(auditoriumForDeleteId).isPresent());
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsAuditoriums_ThenReturnZero() {
        assertEquals(0L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void count_WhenExistsAuditoriums_ThenReturnCountAuditoriums() {
        assertEquals(5L, testRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void deleteById_WhenDeleteWithNullAsAuditoriumId_ThenException() {
        assertThrows(DataAccessException.class, () -> testRepository.deleteById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void deleteById_WhenDeleteNotExistAuditorium_ThenNothingWillBeDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long auditoriumForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST;
        assertFalse(testRepository.existsById(auditoriumForDeleteId));
        testRepository.deleteById(auditoriumForDeleteId);
        testEntityManager.flush();
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
            "classpath:testdata_for_Lecture_in_lectures.sql" })
    void deleteById_WhenDeleteExistAuditoriumWithAssignedLectures_ThenException() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long auditoriumForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        assertTrue(testRepository.existsById(auditoriumForDeleteId));

        assertThrows(DataIntegrityViolationException.class, () -> testRepository.deleteById(auditoriumForDeleteId),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);

        assertTrue(testRepository.existsById(auditoriumForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertEquals(countEntitiesBeforeAttempt, countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:testdata_for_Auditorium_in_auditoriums.sql" })
    void deleteById_WhenDeleteExistAuditoriumWithHappyPath_ThenDeleted() {
        long countEntitiesBeforeAttempt = testRepository.count();
        long auditoriumForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_2;
        assertTrue(testRepository.existsById(auditoriumForDeleteId));
        testRepository.deleteById(auditoriumForDeleteId);
        testEntityManager.flush();
        assertFalse(testRepository.existsById(auditoriumForDeleteId));
        long countEntitiesAfterAttempt = testRepository.count();
        assertTrue(countEntitiesBeforeAttempt > countEntitiesAfterAttempt);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAllByAvailable_WhenNotExistsAuditoriums_ThenReturnEmptyListAuditoriums() {
        assertFalse(testRepository.count() > 0);
        assertThat(testRepository.findAllByAvailable(false)).isEmpty();
        assertThat(testRepository.findAllByAvailable(true)).isEmpty();
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
            "classpath:testdata_for_Lecture_in_lectures.sql" })
    void findAllByAvailable_WhenExistsAuditoriumsWithSuchAvailable_ThenReturnListAuditoriums() {
        List<Auditorium> auditoriumsExpected = ConstantsTestAuditorium.getAllTestAuditoriums()
                .stream()
                .filter(Auditorium::isAvailable)
                .toList();
        assertThat(auditoriumsExpected).isNotEmpty();

        List<Auditorium> auditoriumsReturned = testRepository.findAllByAvailable(true);
        assertThat(auditoriumsReturned).isNotEmpty();
        assertThat(auditoriumsReturned)
                .usingRecursiveComparison()
                .isEqualTo(auditoriumsExpected);
    }

}
