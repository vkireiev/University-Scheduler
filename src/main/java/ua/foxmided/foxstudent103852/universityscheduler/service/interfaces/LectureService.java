package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;

@Validated
public interface LectureService extends CrudService<Lecture, Long> {

    long count();

    List<Lecture> findAllByLectureDateBetween(@NotNull LocalDate lectureDateStart, @NotNull LocalDate lectureDateEnd);

    List<Lecture> findAllByLecturerAndLectureDate(Employee lecturer, LocalDate lectureDate);

    List<Lecture> findAllByLecturerAndLectureDateBetween(Employee lecturer,
            LocalDate lectureDateStart, LocalDate lectureDateEnd);

    List<Lecture> findAllByGroupsContainingAndLectureDate(Group group, LocalDate lectureDate);

    List<Lecture> findAllByGroupsContainingAndLectureDateBetween(Group group,
            LocalDate lectureDateStart, LocalDate lectureDateEnd);

    List<Lecture> findAllByAuditoriumAndLectureDateGreaterThanEqual(@NotNull Auditorium auditorium,
            @NotNull LocalDate date);

    List<Lecture> findPageByAuditoriumAndLectureDateGreaterThanEqual(@NotNull Auditorium auditorium,
            @NotNull LocalDate date, @NotNull Pageable page);

    boolean existsByAuditoriumAndLectureDateGreaterThanEqual(@NotNull Auditorium auditorium,
            @NotNull LocalDate date);

    boolean existsByGroupAndLectureDateAndTimeSlot(@NotNull Group group, @NotNull LocalDate lectureDate,
            @NotNull TimeSlot timeSlot);

    boolean existsByIdNotInAndGroupAndLectureDateAndTimeSlot(@NotEmpty Collection<Long> lecturesId,
            @NotNull Group group, @NotNull LocalDate lectureDate, @NotNull TimeSlot timeSlot);

    boolean updateGroups(@NotNull Long id, @NotNull Set<Group> groups);

}
