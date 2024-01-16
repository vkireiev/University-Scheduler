package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;

public final class ConstantsTestCourse {
    public static final Long COURSE_ID_VALID_1 = 5L;
    public static final Long COURSE_ID_VALID_2 = 2L;
    public static final Long COURSE_ID_FOR_DELETE = 6L;
    public static final Long COURSE_ID_FOR_DELETE_INVALID_1 = 12L;
    public static final Long COURSE_ID_FOR_DELETE_INVALID_2 = 13L;
    public static final Long COURSE_ID_INVALID = 0L;
    public static final Long COURSE_ID_NOT_EXIST = 111L;

    public static final String COURSE_NAME_VALID = "Course #Test";
    public static final String COURSE_NAME_FOR_UPDATE = "Course #Update";
    public static final String COURSE_NAME_EMPTY = "";
    public static final String COURSE_NAME_INVALID_1 = "G";
    public static final String COURSE_NAME_INVALID_2 = "Course#invalid1_Course#invalid2_Course#invalid3_Course#invalid4"
            + "Course#invalid5_Course#invalid6_Course#invalid7_Course#invalid8_Course#invalid9_Course#invalid10";

    public static final String COURSE_DESCRIPTION_VALID = "Description #Test";
    public static final String COURSE_DESCRIPTION_FOR_UPDATE = "Description #Update";
    public static final String COURSE_DESCRIPTION_EMPTY = "";
    public static final String COURSE_DESCRIPTION_INVALID_1 = "D";
    public static final String COURSE_DESCRIPTION_INVALID_2 = "Description#invalid01 Description#invalid02 "
            + "Description#invalid03 Description#invalid04 Description#invalid05 Description#invalid06 "
            + "Description#invalid07 Description#invalid08 Description#invalid09 Description#invalid10 "
            + "Description#invalid11 Description#invalid12 Description#invalid13 Description#invalid14";

    public static final Set<Employee> COURSE_LECTURERS_VALID = new HashSet<>(Arrays.asList(
            ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1)));

    public static final Set<Group> COURSE_GROUPS_VALID = new HashSet<>(Arrays.asList(
            ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2)));

    private ConstantsTestCourse() {
    }

    public static Course getTestCourse(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, String> nameMap = new HashMap<>();
        Map<Long, String> descriptionMap = new HashMap<>();
        // id = 1
        idMap.put(1L, 1L);
        nameMap.put(1L, "Course #01");
        descriptionMap.put(1L, "Description Course #01");
        // id = 2
        idMap.put(2L, 2L);
        nameMap.put(2L, "Course #02");
        descriptionMap.put(2L, "Description Course #02");
        // id = 3
        idMap.put(3L, 3L);
        nameMap.put(3L, "Course #03");
        descriptionMap.put(3L, "Description Course #03");
        // id = 4
        idMap.put(4L, 4L);
        nameMap.put(4L, "Course #04");
        descriptionMap.put(4L, "Description Course #04");
        // id = 5
        idMap.put(5L, 5L);
        nameMap.put(5L, "Course #05");
        descriptionMap.put(5L, "Description Course #05");
        // id = 6
        idMap.put(6L, 6L);
        nameMap.put(6L, "Course #06");
        descriptionMap.put(6L, "Description Course #06");
        // id = 7
        idMap.put(7L, 7L);
        nameMap.put(7L, "Course #07");
        descriptionMap.put(7L, "Description Course #07");
        // id = 8
        idMap.put(8L, 8L);
        nameMap.put(8L, "Course #08");
        descriptionMap.put(8L, "Description Course #08");
        // id = 9
        idMap.put(9L, 9L);
        nameMap.put(9L, "Course #09");
        descriptionMap.put(9L, "Description Course #09");
        // id = 10
        idMap.put(10L, 10L);
        nameMap.put(10L, "Course #10");
        descriptionMap.put(10L, "Description Course #10");
        // id = 11
        idMap.put(11L, 11L);
        nameMap.put(11L, "Course #11");
        descriptionMap.put(11L, "Description Course #11");
        // id = 12
        idMap.put(12L, 12L);
        nameMap.put(12L, "Course #12");
        descriptionMap.put(12L, "Description Course #12");
        // id = 13
        idMap.put(13L, 13L);
        nameMap.put(13L, "Course #13");
        descriptionMap.put(13L, "Description Course #13");

        Course course = new Course();
        course.setId(idMap.get(id));
        course.setName(nameMap.get(id));
        course.setDescription(descriptionMap.get(id));
        course.setLecturers(new HashSet<>());
        course.setGroups(new HashSet<>());
        return course;
    }

    public static List<Course> getAllTestCourses() {
        Course course1 = ConstantsTestCourse.getTestCourse(1L);
        Course course2 = ConstantsTestCourse.getTestCourse(2L);
        Course course3 = ConstantsTestCourse.getTestCourse(3L);
        Course course4 = ConstantsTestCourse.getTestCourse(4L);
        Course course5 = ConstantsTestCourse.getTestCourse(5L);
        Course course6 = ConstantsTestCourse.getTestCourse(6L);
        Course course7 = ConstantsTestCourse.getTestCourse(7L);
        Course course8 = ConstantsTestCourse.getTestCourse(8L);
        Course course9 = ConstantsTestCourse.getTestCourse(9L);
        Course course10 = ConstantsTestCourse.getTestCourse(10L);
        Course course11 = ConstantsTestCourse.getTestCourse(11L);
        Course course12 = ConstantsTestCourse.getTestCourse(12L);
        Course course13 = ConstantsTestCourse.getTestCourse(13L);

        Employee employee1 = ConstantsTestEmployee.getTestEmployee(1L);
        Employee employee2 = ConstantsTestEmployee.getTestEmployee(2L);
        Employee employee3 = ConstantsTestEmployee.getTestEmployee(3L);
        Employee employee4 = ConstantsTestEmployee.getTestEmployee(4L);
        Employee employee5 = ConstantsTestEmployee.getTestEmployee(5L);
        employee1.addCourse(course1);
        employee1.addCourse(course2);
        employee2.addCourse(course3);
        employee3.addCourse(course5);
        employee3.addCourse(course6);
        employee4.addCourse(course7);
        employee5.addCourse(course8);
        employee4.addCourse(course9);
        employee4.addCourse(course11);
        employee4.addCourse(course12);

        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        Group group8 = ConstantsTestGroup.getTestGroup(8L);
        group1.addCourse(course1);
        group1.addCourse(course4);
        group1.addCourse(course8);
        group1.addCourse(course11);
        group1.addCourse(course3);
        group2.addCourse(course3);
        group2.addCourse(course2);
        group2.addCourse(course1);
        group2.addCourse(course13);
        group3.addCourse(course8);
        group3.addCourse(course2);
        group3.addCourse(course1);
        group3.addCourse(course4);
        group3.addCourse(course3);
        group4.addCourse(course9);
        group4.addCourse(course2);
        group4.addCourse(course7);
        group4.addCourse(course5);
        group4.addCourse(course1);
        group4.addCourse(course8);
        group5.addCourse(course1);
        group5.addCourse(course3);
        group5.addCourse(course7);
        group8.addCourse(course1);
        group8.addCourse(course3);
        group8.addCourse(course8);

        return new ArrayList<Course>(Arrays.asList(course1, course2, course3, course4,
                course5, course6, course7, course8, course9, course10, course11, course12, course13));
    }

    public static Course newValidCourse(Long id) {
        Course course = newValidCourse();
        course.setId(id);
        return course;
    }

    public static Course newValidCourse() {
        Course course = new Course();
        course.setName(ConstantsTestCourse.COURSE_NAME_VALID);
        course.setDescription(ConstantsTestCourse.COURSE_DESCRIPTION_VALID);
        course.setLecturers(ConstantsTestCourse.COURSE_LECTURERS_VALID);
        course.setGroups(ConstantsTestCourse.COURSE_GROUPS_VALID);
        return course;
    }

}
