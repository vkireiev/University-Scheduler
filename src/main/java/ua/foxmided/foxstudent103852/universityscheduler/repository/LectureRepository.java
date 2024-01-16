package ua.foxmided.foxstudent103852.universityscheduler.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByLectureDateBetween(LocalDate date1, LocalDate date2);

    List<Lecture> findAllByLecturerAndLectureDate(Employee lecturer, LocalDate date);

    List<Lecture> findAllByLecturerAndLectureDateBetween(Employee lecturer, LocalDate date1, LocalDate date2);

    List<Lecture> findAllByGroupsContainingAndLectureDate(Group group, LocalDate date);

    List<Lecture> findAllByGroupsContainingAndLectureDateBetween(Group group, LocalDate date1, LocalDate date2);

    List<Lecture> findAllByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date);

    List<Lecture> findAllByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date,
            Pageable page);

    boolean existsByAuditoriumAndLectureDateGreaterThanEqual(Auditorium auditorium, LocalDate date);

    boolean existsByGroupsContainingAndLectureDateAndTimeSlot(Group group, LocalDate lectureDate, TimeSlot timeSlot);

    boolean existsByIdNotInAndGroupsContainingAndLectureDateAndTimeSlot(Collection<Long> lecturesId, Group group,
            LocalDate lectureDate, TimeSlot timeSlot);

}
