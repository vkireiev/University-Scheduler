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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;

public final class ConstantsTestGroup {
    public static final Long GROUP_ID_VALID_1 = 4L;
    public static final Long GROUP_ID_VALID_2 = 3L;
    public static final Long GROUP_ID_FOR_DELETE = 6L;
    public static final Long GROUP_ID_FOR_DELETE_INVALID_1 = 7L;
    public static final Long GROUP_ID_FOR_DELETE_INVALID_2 = 8L;
    public static final Long GROUP_ID_INVALID_1 = 0L;
    public static final Long GROUP_ID_NOT_EXIST = 111L;

    public static final String GROUP_NAME_VALID = "Group #Test";
    public static final String GROUP_NAME_FOR_UPDATE = "Group #Update";
    public static final String GROUP_NAME_EMPTY = "";
    public static final String GROUP_NAME_INVALID_1 = "G";
    public static final String GROUP_NAME_INVALID_2 = "Group#invalid1_Group#invalid2_Group#invalid3_Group#invalid4"
            + "Group#invalid5_Group#invalid6_Group#invalid7_Group#invalid8_Group#invalid9";

    public static final Short GROUP_CAPACITY_VALID = 25;
    public static final Short GROUP_CAPACITY_INVALID_1 = 0;
    public static final Short GROUP_CAPACITY_INVALID_2 = 301;
    public static final Short GROUP_CAPACITY_INVALID_3 = -301;
    public static final Short GROUP_CAPACITY_FOR_UPDATE = 30;

    public static final Set<Student> GROUP_STUDENTS_VALID = new HashSet<>(Arrays.asList(
            ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1)));

    public static final Set<Course> GROUP_COURSES_VALID = new HashSet<>(Arrays.asList(
            ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1)));

    private ConstantsTestGroup() {
    }

    public static Group getTestGroup(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, String> nameMap = new HashMap<>();
        Map<Long, Short> capacityMap = new HashMap<>();
        // Invalid Group
        idMap.put(0L, 0L);
        nameMap.put(0L, "Invalid Group");
        capacityMap.put(0L, (short) 0);
        // id = 1
        idMap.put(1L, 1L);
        nameMap.put(1L, "Group #1");
        capacityMap.put(1L, (short) 10);
        // id = 2
        idMap.put(2L, 2L);
        nameMap.put(2L, "Group #2");
        capacityMap.put(2L, (short) 10);
        // id = 3
        idMap.put(3L, 3L);
        nameMap.put(3L, "Group #3");
        capacityMap.put(3L, (short) 10);
        // id = 4
        idMap.put(4L, 4L);
        nameMap.put(4L, "Group #4");
        capacityMap.put(4L, (short) 10);
        // id = 5
        idMap.put(5L, 5L);
        nameMap.put(5L, "Group #5");
        capacityMap.put(5L, (short) 10);
        // id = 6
        idMap.put(6L, 6L);
        nameMap.put(6L, "Group #6");
        capacityMap.put(6L, (short) 5);
        // id = 7
        idMap.put(7L, 7L);
        nameMap.put(7L, "Group #7");
        capacityMap.put(7L, (short) 17);
        // id = 8
        idMap.put(8L, 8L);
        nameMap.put(8L, "Group #8");
        capacityMap.put(8L, (short) 18);
        // id = 111L
        idMap.put(111L, 111L);
        nameMap.put(111L, "Group NotExists");
        capacityMap.put(111L, (short) 30);

        Group group = new Group();
        group.setId(idMap.get(id));
        group.setName(nameMap.get(id));
        group.setCapacity(capacityMap.get(id));
        group.setStudents(new HashSet<>());
        group.setCourses(new HashSet<>());
        return group;
    }

    public static List<Group> getAllTestGroups() {
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        Group group6 = ConstantsTestGroup.getTestGroup(6L);
        Group group7 = ConstantsTestGroup.getTestGroup(7L);
        Group group8 = ConstantsTestGroup.getTestGroup(8L);

        Course course1 = ConstantsTestCourse.getTestCourse(1L);
        Course course2 = ConstantsTestCourse.getTestCourse(2L);
        Course course3 = ConstantsTestCourse.getTestCourse(3L);
        Course course4 = ConstantsTestCourse.getTestCourse(4L);
        Course course5 = ConstantsTestCourse.getTestCourse(5L);
        Course course7 = ConstantsTestCourse.getTestCourse(7L);
        Course course8 = ConstantsTestCourse.getTestCourse(8L);
        Course course9 = ConstantsTestCourse.getTestCourse(9L);
        Course course11 = ConstantsTestCourse.getTestCourse(11L);
        Course course13 = ConstantsTestCourse.getTestCourse(13L);
        group1.setCourses(new HashSet<>(Arrays.asList(course1, course3, course4, course8, course11)));
        group2.setCourses(new HashSet<>(Arrays.asList(course2, course1, course3, course13)));
        group3.setCourses(new HashSet<>(Arrays.asList(course8, course2, course1, course4, course3)));
        group4.setCourses(new HashSet<>(Arrays.asList(course9, course2, course7, course5, course1, course8)));
        group5.setCourses(new HashSet<>(Arrays.asList(course7, course3, course1)));
        group8.setCourses(new HashSet<>(Arrays.asList(course8, course3, course1)));

        Student student51 = ConstantsTestStudent.getTestStudent(51L);
        Student student52 = ConstantsTestStudent.getTestStudent(52L);
        Student student53 = ConstantsTestStudent.getTestStudent(53L);
        Student student54 = ConstantsTestStudent.getTestStudent(54L);
        Student student55 = ConstantsTestStudent.getTestStudent(55L);
        Student student56 = ConstantsTestStudent.getTestStudent(56L);
        Student student57 = ConstantsTestStudent.getTestStudent(57L);
        Student student58 = ConstantsTestStudent.getTestStudent(58L);
        group1.addStudent(student53);
        group1.addStudent(student54);
        group2.addStudent(student52);
        group3.addStudent(student51);
        group4.addStudent(student55);
        group5.addStudent(student57);
        group5.addStudent(student58);
        group7.addStudent(student56);

        return new ArrayList<Group>(Arrays.asList(group1, group2, group3, group4, group5, group6, group7, group8));
    }

    public static Group newValidGroup(Long id) {
        Group group = newValidGroup();
        group.setId(id);
        return group;
    }

    public static Group newValidGroup() {
        Group group = new Group();
        group.setName(ConstantsTestGroup.GROUP_NAME_VALID);
        group.setCapacity(ConstantsTestGroup.GROUP_CAPACITY_VALID);
        group.setStudents(ConstantsTestGroup.GROUP_STUDENTS_VALID);
        group.setCourses(ConstantsTestGroup.GROUP_COURSES_VALID);
        return group;
    }

}
