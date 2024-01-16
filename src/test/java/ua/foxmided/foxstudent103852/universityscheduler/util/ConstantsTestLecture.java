package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;

public final class ConstantsTestLecture {
    public static final Long LECTURE_ID_VALID_1 = 5L;
    public static final Long LECTURE_ID_VALID_2 = 3L;
    public static final Long LECTURE_ID_FOR_DELETE = 4L;
    public static final Long LECTURE_ID_INVALID = 0L;
    public static final Long LECTURE_ID_NOT_EXIST = 111L;

    public static final Auditorium LECTURE_AUDITORIUM_VALID = ConstantsTestAuditorium
            .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);
    public static final Auditorium LECTURE_AUDITORIUM_FOR_UPDATE = ConstantsTestAuditorium
            .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_FOR_UPDATE);

    public static final String LECTURE_SUBJECT_VALID = "Subject 'Test'";
    public static final String LECTURE_SUBJECT_FOR_UPDATE = "Subject 'Update'";
    public static final String LECTURE_SUBJECT_EMPTY = "";
    public static final String LECTURE_SUBJECT_INVALID_1 = "S";
    public static final String LECTURE_SUBJECT_INVALID_2 = "Subject#invalid01 Subject#invalid02 Subject#invalid03 "
            + "Subject#invalid04 Subject#invalid05 Subject#invalid06 Subject#invalid07 Subject#invalid08 "
            + "Subject#invalid09 Subject#invalid10 Subject#invalid10 Subject#invalid11 Subject#invalid12 "
            + "Subject#invalid13 Subject#invalid14 Subject#invalid15 Subject#invalid16 Subject#invalid17 ";

    public static final Course LECTURE_COURSE_VALID = ConstantsTestCourse
            .getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
    public static final Course LECTURE_COURSE_FOR_UPDATE = ConstantsTestCourse
            .getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_2);

    public static final Set<Group> LECTURE_GROUPS_VALID = new HashSet<>(Arrays.asList(
            ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1),
            ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2)));
    public static final Group LECTURE_GROUP_FOR_ADD = ConstantsTestGroup
            .getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE);

    public static final Employee LECTURE_LECTURER_VALID = ConstantsTestEmployee.getTestEmployee(2L);
    public static final Employee LECTURE_LECTURER_FOR_UPDATE = ConstantsTestEmployee
            .getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_FOR_DELETE);

    public static final LocalDate LECTURE_LECTURE_DATE_VALID = LocalDate.of(2023, 5, 3);
    public static final LocalDate LECTURE_LECTURE_DATE_FOR_UPDATE = LocalDate.of(2023, 2, 2);

    public static final TimeSlot LECTURE_TIME_SLOT_VALID_1 = TimeSlot.LECTURE6;
    public static final TimeSlot LECTURE_TIME_SLOT_VALID_2 = TimeSlot.LECTURE2;
    public static final TimeSlot LECTURE_TIME_SLOT_INVALID = TimeSlot.LECTURE0;
    public static final TimeSlot LECTURE_TIME_SLOT_FOR_UPDATE = TimeSlot.LECTURE0;

    public static final Employee LECTURE_FIND_BY_LECTURER_VALID = ConstantsTestEmployee.getTestEmployee(4L);
    public static final Employee LECTURE_FIND_BY_LECTURER_INVALID = ConstantsTestEmployee.getTestEmployee(1L);

    public static final Group LECTURE_FIND_BY_GROUP_VALID_1 = ConstantsTestGroup.getTestGroup(3L);
    public static final Group LECTURE_FIND_BY_GROUP_VALID_2 = ConstantsTestGroup.getTestGroup(5L);
    public static final Group LECTURE_FIND_BY_GROUP_INVALID = ConstantsTestGroup.getTestGroup(6L);

    public static final LocalDate LECTURE_FIND_BY_LECTURE_DATE_1_VALID = LocalDate.of(2023, 5, 2);
    public static final LocalDate LECTURE_FIND_BY_LECTURE_DATE_2_VALID = LocalDate.of(2023, 5, 4);
    public static final LocalDate LECTURE_FIND_BY_LECTURE_DATE_3_VALID = LocalDate.of(2023, 5, 3);
    public static final LocalDate LECTURE_FIND_BY_LECTURE_DATE_1_INVALID = LocalDate.of(2023, 5, 6);
    public static final LocalDate LECTURE_FIND_BY_LECTURE_DATE_2_INVALID = LocalDate.of(2023, 5, 8);

    public static final Auditorium LECTURE_FIND_BY_AUDITORIUM_VALID = ConstantsTestAuditorium
            .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1);
    public static final Auditorium LECTURE_FIND_BY_AUDITORIUM_INVALID = ConstantsTestAuditorium
            .getTestAuditorium(ConstantsTestAuditorium.AUDITORIUM_ID_NOT_EXIST);

    private ConstantsTestLecture() {
    }

    public static Lecture getTestLecture(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, Auditorium> auditoriumMap = new HashMap<>();
        Map<Long, String> subjectMap = new HashMap<>();
        Map<Long, Course> courseMap = new HashMap<>();
        Map<Long, LocalDate> lectureDateMap = new HashMap<>();
        Map<Long, TimeSlot> timeSlotMap = new HashMap<>();
        Map<Long, Employee> lecturerMap = new HashMap<>();
        // id = 1
        idMap.put(1L, 1L);
        auditoriumMap.put(1L, ConstantsTestAuditorium.getTestAuditorium(1L));
        subjectMap.put(1L, "Lecture #1");
        courseMap.put(1L, ConstantsTestCourse.getTestCourse(3L));
        lectureDateMap.put(1L, LocalDate.of(2023, 5, 1));
        timeSlotMap.put(1L, TimeSlot.LECTURE1);
        lecturerMap.put(1L, ConstantsTestEmployee.getTestEmployee(2L));
        // id = 2
        idMap.put(2L, 2L);
        auditoriumMap.put(2L, ConstantsTestAuditorium.getTestAuditorium(2L));
        subjectMap.put(2L, "Lecture #2");
        courseMap.put(2L, ConstantsTestCourse.getTestCourse(9L));
        lectureDateMap.put(2L, LocalDate.of(2023, 5, 1));
        timeSlotMap.put(2L, TimeSlot.LECTURE2);
        lecturerMap.put(2L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 3
        idMap.put(3L, 3L);
        auditoriumMap.put(3L, ConstantsTestAuditorium.getTestAuditorium(3L));
        subjectMap.put(3L, "Lecture #3");
        courseMap.put(3L, ConstantsTestCourse.getTestCourse(8L));
        lectureDateMap.put(3L, LocalDate.of(2023, 5, 1));
        timeSlotMap.put(3L, TimeSlot.LECTURE2);
        lecturerMap.put(3L, ConstantsTestEmployee.getTestEmployee(5L));
        // id = 4
        idMap.put(4L, 4L);
        auditoriumMap.put(4L, ConstantsTestAuditorium.getTestAuditorium(3L));
        subjectMap.put(4L, "Lecture #4");
        courseMap.put(4L, ConstantsTestCourse.getTestCourse(11L));
        lectureDateMap.put(4L, LocalDate.of(2023, 5, 2));
        timeSlotMap.put(4L, TimeSlot.LECTURE1);
        lecturerMap.put(4L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 5
        idMap.put(5L, 5L);
        auditoriumMap.put(5L, ConstantsTestAuditorium.getTestAuditorium(4L));
        subjectMap.put(5L, "Lecture #5");
        courseMap.put(5L, ConstantsTestCourse.getTestCourse(3L));
        lectureDateMap.put(5L, LocalDate.of(2023, 5, 2));
        timeSlotMap.put(5L, TimeSlot.LECTURE2);
        lecturerMap.put(5L, ConstantsTestEmployee.getTestEmployee(2L));
        // id = 6
        idMap.put(6L, 6L);
        auditoriumMap.put(6L, ConstantsTestAuditorium.getTestAuditorium(1L));
        subjectMap.put(6L, "Lecture #6");
        courseMap.put(6L, ConstantsTestCourse.getTestCourse(7L));
        lectureDateMap.put(6L, LocalDate.of(2023, 5, 2));
        timeSlotMap.put(6L, TimeSlot.LECTURE2);
        lecturerMap.put(6L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 7
        idMap.put(7L, 7L);
        auditoriumMap.put(7L, ConstantsTestAuditorium.getTestAuditorium(1L));
        subjectMap.put(7L, "Lecture #7");
        courseMap.put(7L, ConstantsTestCourse.getTestCourse(7L));
        lectureDateMap.put(7L, LocalDate.of(2023, 5, 3));
        timeSlotMap.put(7L, TimeSlot.LECTURE1);
        lecturerMap.put(7L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 8
        idMap.put(8L, 8L);
        auditoriumMap.put(8L, ConstantsTestAuditorium.getTestAuditorium(2L));
        subjectMap.put(8L, "Lecture #8");
        courseMap.put(8L, ConstantsTestCourse.getTestCourse(3L));
        lectureDateMap.put(8L, LocalDate.of(2023, 5, 3));
        timeSlotMap.put(8L, TimeSlot.LECTURE1);
        lecturerMap.put(8L, ConstantsTestEmployee.getTestEmployee(2L));
        // id = 9
        idMap.put(9L, 9L);
        auditoriumMap.put(9L, ConstantsTestAuditorium.getTestAuditorium(3L));
        subjectMap.put(9L, "Lecture #9");
        courseMap.put(9L, ConstantsTestCourse.getTestCourse(8L));
        lectureDateMap.put(9L, LocalDate.of(2023, 5, 3));
        timeSlotMap.put(9L, TimeSlot.LECTURE2);
        lecturerMap.put(9L, ConstantsTestEmployee.getTestEmployee(5L));
        // id = 10
        idMap.put(10L, 10L);
        auditoriumMap.put(10L, ConstantsTestAuditorium.getTestAuditorium(4L));
        subjectMap.put(10L, "Lecture #10");
        courseMap.put(10L, ConstantsTestCourse.getTestCourse(3L));
        lectureDateMap.put(10L, LocalDate.of(2023, 5, 3));
        timeSlotMap.put(10L, TimeSlot.LECTURE2);
        lecturerMap.put(10L, ConstantsTestEmployee.getTestEmployee(2L));
        // id = 11
        idMap.put(11L, 11L);
        auditoriumMap.put(11L, ConstantsTestAuditorium.getTestAuditorium(1L));
        subjectMap.put(11L, "Lecture #11");
        courseMap.put(11L, ConstantsTestCourse.getTestCourse(9L));
        lectureDateMap.put(11L, LocalDate.of(2023, 5, 4));
        timeSlotMap.put(11L, TimeSlot.LECTURE2);
        lecturerMap.put(11L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 12
        idMap.put(12L, 12L);
        auditoriumMap.put(12L, ConstantsTestAuditorium.getTestAuditorium(2L));
        subjectMap.put(12L, "Lecture #12");
        courseMap.put(12L, ConstantsTestCourse.getTestCourse(8L));
        lectureDateMap.put(12L, LocalDate.of(2023, 5, 4));
        timeSlotMap.put(12L, TimeSlot.LECTURE2);
        lecturerMap.put(12L, ConstantsTestEmployee.getTestEmployee(5L));
        // id = 13
        idMap.put(13L, 13L);
        auditoriumMap.put(13L, ConstantsTestAuditorium.getTestAuditorium(3L));
        subjectMap.put(13L, "Lecture #13");
        courseMap.put(13L, ConstantsTestCourse.getTestCourse(5L));
        lectureDateMap.put(13L, LocalDate.of(2023, 5, 4));
        timeSlotMap.put(13L, TimeSlot.LECTURE3);
        lecturerMap.put(13L, ConstantsTestEmployee.getTestEmployee(3L));
        // id = 14
        idMap.put(14L, 14L);
        auditoriumMap.put(14L, ConstantsTestAuditorium.getTestAuditorium(3L));
        subjectMap.put(14L, "Lecture #14");
        courseMap.put(14L, ConstantsTestCourse.getTestCourse(7L));
        lectureDateMap.put(14L, LocalDate.of(2023, 5, 5));
        timeSlotMap.put(14L, TimeSlot.LECTURE1);
        lecturerMap.put(14L, ConstantsTestEmployee.getTestEmployee(4L));
        // id = 15
        idMap.put(15L, 15L);
        auditoriumMap.put(15L, ConstantsTestAuditorium.getTestAuditorium(4L));
        subjectMap.put(15L, "Lecture #15");
        courseMap.put(15L, ConstantsTestCourse.getTestCourse(3L));
        lectureDateMap.put(15L, LocalDate.of(2023, 5, 5));
        timeSlotMap.put(15L, TimeSlot.LECTURE2);
        lecturerMap.put(15L, ConstantsTestEmployee.getTestEmployee(2L));

        Lecture lecture = new Lecture();
        lecture.setId(idMap.get(id));
        lecture.setAuditorium(auditoriumMap.get(id));
        lecture.setSubject(subjectMap.get(id));
        lecture.setCourse(courseMap.get(id));
        lecture.setGroups(new HashSet<>());
        lecture.setLecturer(lecturerMap.get(id));
        lecture.setLectureDate(lectureDateMap.get(id));
        lecture.setTimeSlot(timeSlotMap.get(id));
        return lecture;
    }

    public static List<Lecture> getAllTestLectures() {
        Lecture lecture1 = ConstantsTestLecture.getTestLecture(1L);
        Lecture lecture2 = ConstantsTestLecture.getTestLecture(2L);
        Lecture lecture3 = ConstantsTestLecture.getTestLecture(3L);
        Lecture lecture4 = ConstantsTestLecture.getTestLecture(4L);
        Lecture lecture5 = ConstantsTestLecture.getTestLecture(5L);
        Lecture lecture6 = ConstantsTestLecture.getTestLecture(6L);
        Lecture lecture7 = ConstantsTestLecture.getTestLecture(7L);
        Lecture lecture8 = ConstantsTestLecture.getTestLecture(8L);
        Lecture lecture9 = ConstantsTestLecture.getTestLecture(9L);
        Lecture lecture10 = ConstantsTestLecture.getTestLecture(10L);
        Lecture lecture11 = ConstantsTestLecture.getTestLecture(11L);
        Lecture lecture12 = ConstantsTestLecture.getTestLecture(12L);
        Lecture lecture13 = ConstantsTestLecture.getTestLecture(13L);
        Lecture lecture14 = ConstantsTestLecture.getTestLecture(14L);
        Lecture lecture15 = ConstantsTestLecture.getTestLecture(15L);

        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        lecture1.addGroup(group2);
        lecture2.addGroup(group4);
        lecture3.addGroup(group1);
        lecture3.addGroup(group3);
        lecture4.addGroup(group1);
        lecture5.addGroup(group5);
        lecture6.addGroup(group4);
        lecture7.addGroup(group5);
        lecture8.addGroup(group3);
        lecture9.addGroup(group4);
        lecture10.addGroup(group2);
        lecture11.addGroup(group4);
        lecture12.addGroup(group1);
        lecture12.addGroup(group3);
        lecture13.addGroup(group4);
        lecture14.addGroup(group5);
        lecture15.addGroup(group2);

        return new ArrayList<Lecture>(Arrays.asList(lecture1, lecture2, lecture3,
                lecture4, lecture5, lecture6, lecture7, lecture8, lecture9, lecture10,
                lecture11, lecture12, lecture13, lecture14, lecture15));
    }

    public static Lecture newValidLecture(Long id) {
        Lecture lecture = newValidLecture();
        lecture.setId(id);
        return lecture;
    }

    public static Lecture newValidLecture() {
        Lecture lecture = new Lecture();
        lecture.setAuditorium(ConstantsTestLecture.LECTURE_AUDITORIUM_VALID);
        lecture.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_VALID);
        lecture.setCourse(ConstantsTestLecture.LECTURE_COURSE_VALID);
        lecture.setGroups(ConstantsTestLecture.LECTURE_GROUPS_VALID);
        lecture.setLecturer(ConstantsTestLecture.LECTURE_LECTURER_VALID);
        lecture.setLectureDate(ConstantsTestLecture.LECTURE_LECTURE_DATE_VALID);
        lecture.setTimeSlot(ConstantsTestLecture.LECTURE_TIME_SLOT_VALID_1);
        return lecture;
    }

}
