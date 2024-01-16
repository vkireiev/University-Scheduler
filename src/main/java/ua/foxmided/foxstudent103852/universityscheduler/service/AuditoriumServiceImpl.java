package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.repository.AuditoriumRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;

@Service
@RequiredArgsConstructor
public class AuditoriumServiceImpl implements AuditoriumService {

    @Value("${err.msg.service.jdbc.auditorium.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.getByNumber.fail}")
    private String getByNumberFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.auditorium.delete.fail.integrity.violation}")
    private String deleteFailIntegrityViolationMessage;

    @Value("${err.msg.service.jdbc.auditorium.count.fail}")
    private String countFailMessage;

    private final AuditoriumRepository auditoriumRepository;
    private final LectureService lectureService;

    @Override
    public Auditorium add(Auditorium auditorium) {
        try {
            return auditoriumRepository.save(auditorium);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAddDataIntegrityViolationException(
                    "Auditorium with such number/availability already exists", e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, auditorium), e);
        }
    }

    @Override
    public Optional<Auditorium> get(Long id) {
        try {
            return auditoriumRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Auditorium> getAll() {
        try {
            return auditoriumRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Auditorium update(Auditorium auditorium) {
        LocalDate currentDate = LocalDate.now();
        try {
            if (!auditoriumRepository.existsById(auditorium.getId())) {
                throw new EntityNotFoundException("Auditorium not found");
            }
            if (!auditorium.isAvailable()
                    && lectureService.existsByAuditoriumAndLectureDateGreaterThanEqual(auditorium, currentDate)) {
                throw new EntityUpdateDataIntegrityViolationException(
                        "Before making Auditorium unavailable, reassign active Lectures to another Auditorium(s)");
            }
            return auditoriumRepository.save(auditorium);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUpdateDataIntegrityViolationException(
                    "Auditorium with such number/availability already exists", e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, auditorium), e);
        }
    }

    @Override
    public boolean delete(Auditorium auditorium) {
        try {
            auditoriumRepository.delete(auditorium);
            return !auditoriumRepository.existsById(auditorium.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, auditorium), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            auditoriumRepository.deleteById(id);
            return !auditoriumRepository.existsById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDataIntegrityViolationException(deleteFailIntegrityViolationMessage, e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, id), e);
        }
    }

    @Override
    public long count() {
        try {
            return auditoriumRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public List<Auditorium> findAllByAvailable(boolean available) {
        try {
            return auditoriumRepository.findAllByAvailable(available);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

}
