package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.repository.LectureRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    private static final String DEFAULT_DATA_INTEGRITY_VIOLATION_MESSAGE = "Failed to %s Lecture due to restrictions";
    private static final String DATA_INTEGRITY_VIOLATION_MESSAGE_COMMON_PART = "Failed to %s Lecture. ";
    private static final Map<String, String> DATA_INTEGRITY_VIOLATION_MESSAGE_EXTENDED_PARTS = Map.of(
            "lectures_auditorium_lecture_time_slot_lecture_date_unique",
            DATA_INTEGRITY_VIOLATION_MESSAGE_COMMON_PART
                    + "The Auditorium at the specified time is already occupied by another Lecture",
            "lectures_lecturer_lecture_date_lecture_time_slot_unique",
            DATA_INTEGRITY_VIOLATION_MESSAGE_COMMON_PART
                    + "The Lecturer already has a Lecture scheduled at the specified time",
            "lectures_group_lecture_date_lecture_time_slot_unique",
            DATA_INTEGRITY_VIOLATION_MESSAGE_COMMON_PART
                    + "The Group already has a Lecture scheduled at the specified time");

    @Value("${err.msg.service.jdbc.lecture.add.fail}")
    private String addFailMessage;

    @Value("${err.msg.service.jdbc.lecture.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.lecture.update.fail}")
    private String updateFailMessage;

    @Value("${err.msg.service.jdbc.lecture.delete.fail}")
    private String deleteFailMessage;

    @Value("${err.msg.service.jdbc.lecture.count.fail}")
    private String countFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.period.fail}")
    private String getAllByPeriodFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.lecturer.date.fail}")
    private String getAllByLecturerAndDateFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.lecturer.period.fail}")
    private String getAllByLecturerAndPeriodFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.group.date.fail}")
    private String getAllByGroupAndDateFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.group.period.fail}")
    private String getAllByGroupAndPeriodFailMessage;

    @Value("${err.msg.service.jdbc.lecture.getall.by.auditorium.date.fail}")
    private String getAllByAuditoriumAndLectureDateFailMessage;

    @Value("${err.msg.service.jdbc.lecture.count.by.auditorium.date.fail}")
    private String countByAuditoriumAndLectureDateFailMessage;

    private final LectureRepository lectureRepository;

    @Override
    public Lecture add(Lecture lecture) {
        try {
            checkGroupsInScheduling(
                    lecture.getGroups(),
                    lecture.getLectureDate(),
                    lecture.getTimeSlot());
            return lectureRepository.save(lecture);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAddDataIntegrityViolationException(
                    String.format(getExtendedExceptionMessage(e.getMessage()), "add"), e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(addFailMessage, lecture), e);
        }
    }

    @Override
    public Optional<Lecture> get(Long id) {
        try {
            return lectureRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Lecture> getAll() {
        try {
            return lectureRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public Lecture update(Lecture lecture) {
        try {
            if (!lectureRepository.existsById(lecture.getId())) {
                throw new EntityNotFoundException("Lecture not found");
            }
            checkGroupsInScheduling(
                    Arrays.asList(lecture.getId()),
                    lecture.getGroups(),
                    lecture.getLectureDate(),
                    lecture.getTimeSlot());
            return lectureRepository.save(lecture);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUpdateDataIntegrityViolationException(
                    String.format(getExtendedExceptionMessage(e.getMessage()), "update"), e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, lecture), e);
        }
    }

    @Override
    public boolean delete(Lecture lecture) {
        try {
            lectureRepository.delete(lecture);
            return !lectureRepository.existsById(lecture.getId());
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, lecture), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            lectureRepository.deleteById(id);
            return !lectureRepository.existsById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(deleteFailMessage, id), e);
        }
    }

    @Override
    public long count() {
        try {
            return lectureRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByLectureDateBetween(LocalDate lectureDateStart, LocalDate lectureDateEnd) {
        try {
            return lectureRepository.findAllByLectureDateBetween(lectureDateStart, lectureDateEnd);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByPeriodFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByLecturerAndLectureDate(Employee lecturer, LocalDate lectureDate) {
        try {
            return lectureRepository.findAllByLecturerAndLectureDate(lecturer, lectureDate);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByLecturerAndDateFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByLecturerAndLectureDateBetween(Employee lecturer,
            LocalDate lectureDateStart, LocalDate lectureDateEnd) {
        try {
            return lectureRepository.findAllByLecturerAndLectureDateBetween(lecturer, lectureDateStart, lectureDateEnd);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByLecturerAndPeriodFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByGroupsContainingAndLectureDate(Group group, LocalDate lectureDate) {
        try {
            return lectureRepository.findAllByGroupsContainingAndLectureDate(group, lectureDate);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByGroupAndDateFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByGroupsContainingAndLectureDateBetween(Group group,
            LocalDate lectureDateStart, LocalDate lectureDateEnd) {
        try {
            return lectureRepository.findAllByGroupsContainingAndLectureDateBetween(group,
                    lectureDateStart, lectureDateEnd);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByGroupAndPeriodFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findAllByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date) {
        try {
            return lectureRepository.findAllByAuditoriumAndLectureDateGreaterThanEqual(auditorium, date);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByAuditoriumAndLectureDateFailMessage, e);
        }
    }

    @Override
    public List<Lecture> findPageByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date,
            Pageable page) {
        try {
            return lectureRepository.findAllByAuditoriumAndLectureDateGreaterThanEqual(auditorium, date, page);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllByAuditoriumAndLectureDateFailMessage, e);
        }
    }

    @Override
    public boolean existsByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date) {
        try {
            return lectureRepository.existsByAuditoriumAndLectureDateGreaterThanEqual(auditorium, date);
        } catch (DataAccessException e) {
            throw new DataProcessingException(countByAuditoriumAndLectureDateFailMessage, e);
        }
    }

    @Override
    public boolean existsByGroupAndLectureDateAndTimeSlot(Group group, LocalDate lectureDate, TimeSlot timeSlot) {
        try {
            return lectureRepository.existsByGroupsContainingAndLectureDateAndTimeSlot(
                    group,
                    lectureDate,
                    timeSlot);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public boolean existsByIdNotInAndGroupAndLectureDateAndTimeSlot(Collection<Long> lecturesId,
            Group group, LocalDate lectureDate, TimeSlot timeSlot) {
        try {
            return lectureRepository.existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(
                    lecturesId,
                    group,
                    lectureDate,
                    timeSlot);
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public boolean updateGroups(Long id, Set<Group> groups) {
        try {
            Optional<Lecture> entityForUpdate = lectureRepository.findById(id);
            if (entityForUpdate.isEmpty()) {
                throw new EntityNotFoundException("Lecture not found");
            }
            if (groups.isEmpty()) {
                throw new EntityUpdateDataIntegrityViolationException(
                        String.format(DATA_INTEGRITY_VIOLATION_MESSAGE_COMMON_PART, "update")
                                + "Before deleting Group from the Lecture, add another Group to the Lecture");
            }
            Set<Group> groupsDifferences = new HashSet<>(groups);
            groupsDifferences.removeAll(entityForUpdate.get().getGroups());
            checkGroupsInScheduling(
                    Arrays.asList(id),
                    groupsDifferences,
                    entityForUpdate.get().getLectureDate(),
                    entityForUpdate.get().getTimeSlot());
            entityForUpdate.get().setGroups(new HashSet<>(groups));
            Lecture updatedEntity = lectureRepository.save(entityForUpdate.get());
            return groups.containsAll(updatedEntity.getGroups())
                    && updatedEntity.getGroups().containsAll(groups);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUpdateDataIntegrityViolationException(
                    String.format(getExtendedExceptionMessage(e.getMessage()), "update"), e);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    private void checkGroupsInScheduling(Set<Group> groups, LocalDate lectureDate, TimeSlot timeSlot) {
        for (Group group : groups) {
            if (existsByGroupAndLectureDateAndTimeSlot(group, lectureDate, timeSlot)) {
                throw new DataIntegrityViolationException("lectures_group_lecture_date_lecture_time_slot_unique");
            }
        }
    }

    private void checkGroupsInScheduling(List<Long> lecturesId,
            Set<Group> groups, LocalDate lectureDate, TimeSlot timeSlot) {
        for (Group group : groups) {
            if (existsByIdNotInAndGroupAndLectureDateAndTimeSlot(
                    lecturesId,
                    group,
                    lectureDate,
                    timeSlot)) {
                throw new DataIntegrityViolationException("lectures_group_lecture_date_lecture_time_slot_unique");
            }
        }
    }

    private String getExtendedExceptionMessage(String exceptionMessage) {
        for (Entry<String, String> extendedPart : DATA_INTEGRITY_VIOLATION_MESSAGE_EXTENDED_PARTS.entrySet()) {
            if (exceptionMessage.contains(extendedPart.getKey())) {
                return extendedPart.getValue();
            }
        }
        return DEFAULT_DATA_INTEGRITY_VIOLATION_MESSAGE;
    }

}
