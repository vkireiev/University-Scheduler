package ua.foxmided.foxstudent103852.universityscheduler.util;

import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

public final class Constants {
    public static final String EMPTY_STRING = "";
    public static final long ZERO_LONG = 0L;

    public static final String LOG_PATTERN_MESSAGE_2 = "{}{}";
    public static final String LOG_PATTERN_MESSAGE_3 = "{}{}{}";
    public static final String LOG_PATTERN_MESSAGE_4 = "{}{}{}{}";
    public static final String LOG_PATTERN_MESSAGE_5 = "{}{}{}{}{}";
    public static final String LOG_PATTERN_2_MESSAGE = "{}, {}";
    public static final String LOG_PATTERN_2_EXCEPTION = "{} <- {}";
    public static final String LOG_PATTERN_3_EXCEPTION = "{} <- {} <- {}";

    public static final String ERR_MSG_AUDITORIUM_ADD = "Failed to add Auditorium data to database: %s";
    public static final String ERR_MSG_AUDITORIUM_UPDATE = "Failed to update Auditorium data in database: %s";
    public static final String ERR_MSG_AUDITORIUM_DELETE = "Failed to delete Auditorium data from database: %s";

    public static final String ERR_MSG_GROUP_ADD = "Failed to add Group data to database: %s";
    public static final String ERR_MSG_GROUP_UPDATE = "Failed to update Group data in database: %s";
    public static final String ERR_MSG_GROUP_DELETE = "Failed to delete Group data from database: %s";

    public static final String ERR_MSG_COURSE_ADD = "Failed to add Course data to database: %s";
    public static final String ERR_MSG_COURSE_UPDATE = "Failed to update Course data in database: %s";
    public static final String ERR_MSG_COURSE_DELETE = "Failed to delete Course data from database: %s";

    public static final String ERR_MSG_EMPLOYEE_ADD = "Failed to add %s data to database: %s";
    public static final String ERR_MSG_EMPLOYEE_UPDATE = "Failed to update %s data in database: %s";
    public static final String ERR_MSG_EMPLOYEE_DELETE = "Failed to delete %s data from database: %s";

    public static final String ERR_MSG_STUDENT_ADD = "Failed to add Student data to database: %s";
    public static final String ERR_MSG_STUDENT_UPDATE = "Failed to update Student data in database: %s";
    public static final String ERR_MSG_STUDENT_DELETE = "Failed to delete Student data from database: %s";

    public static final String ERR_MSG_LECTURE_ADD = "Failed to add Lecture data to database: %s";
    public static final String ERR_MSG_LECTURE_UPDATE = "Failed to update Lecture data in database: %s";
    public static final String ERR_MSG_LECTURE_DELETE = "Failed to delete Lecture data from database: %s";

    public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong... Try again later";
    public static final String DEFAULT_VALIDATE_ERROR_MESSAGE = "Something went wrong... Errors occurred during  validation";

    public static final String EMPLOYEE_ROLE = Employee.class.getSimpleName().toUpperCase();
    public static final String STUDENT_ROLE = Student.class.getSimpleName().toUpperCase();
    public static final String ADMIN_AUTHZ = UserRole.ADMIN.name().toUpperCase();
    public static final String EDITOR_AUTHZ = UserRole.EDITOR.name().toUpperCase();
    public static final String VIEWER_AUTHZ = UserRole.VIEWER.name().toUpperCase();

    public static final String APPLICATION_NAME = "University-Scheduler";
    public static final String MODULE_NAME = "Module Name";
    public static final String MODULE_HREF = "/";

    public static final String ROOT_MODULE_NAME = "Home";
    public static final String ROOT_MODULE_HREF = "/";

    public static final String AUDITORIUMS_MODULE_NAME = "Auditoriums";
    public static final String AUDITORIUMS_MODULE_TITLE = "Auditoriums";
    public static final String AUDITORIUMS_MODULE_HREF = "/auditoriums";

    public static final String COURSES_MODULE_NAME = "Courses";
    public static final String COURSES_MODULE_TITLE = "Auditoriums";
    public static final String COURSES_MODULE_HREF = "/courses";

    public static final String EMPLOYEES_MODULE_NAME = "Employees";
    public static final String EMPLOYEES_MODULE_TITLE = "Employees";
    public static final String EMPLOYEES_MODULE_HREF = "/employees";

    public static final String LECTURERS_MODULE_NAME = "Lecturers";
    public static final String LECTURERS_MODULE_TITLE = "Lecturers";
    public static final String LECTURERS_MODULE_HREF = "/lecturers";

    public static final String GROUPS_MODULE_NAME = "Groups";
    public static final String GROUPS_MODULE_TITLE = "Groups";
    public static final String GROUPS_MODULE_HREF = "/groups";

    public static final String LECTURES_MODULE_NAME = "My Schedule";
    public static final String LECTURES_MODULE_TITLE = "My Schedule";
    public static final String LECTURES_MODULE_HREF = "/lectures";

    public static final String STUDENTS_MODULE_NAME = "Students";
    public static final String STUDENTS_MODULE_TITLE = "Students";
    public static final String STUDENTS_MODULE_HREF = "/students";

    private Constants() {
    }

}
