package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

public final class ConstantsTestEmployee {
    public static final Long EMPLOYEE_ID_VALID_1 = 3L;
    public static final Long EMPLOYEE_ID_VALID_2 = 5L;
    public static final Long EMPLOYEE_ID_FOR_DELETE = 6L;
    public static final Long EMPLOYEE_ID_INVALID = 0L;
    public static final Long EMPLOYEE_ID_INVALID_1 = 2L;
    public static final Long EMPLOYEE_ID_NOT_EXIST = 111L;

    public static final String EMPLOYEE_USERNAME_VALID = "employee";
    public static final String EMPLOYEE_USERNAME_FOR_UPDATE = "userupdate";
    public static final String EMPLOYEE_USERNAME_EMPTY = "";
    public static final String EMPLOYEE_USERNAME_INVALID_1 = "u";
    public static final String EMPLOYEE_USERNAME_INVALID_2 = "user-invalid1,user-invalid2,user-invalid3,"
            + "user-invalid4,user-invalid5";

    public static final String EMPLOYEE_PASSWORD_VALID = "passwordStrong";
    public static final String EMPLOYEE_PASSWORD_FOR_UPDATE = "passwordUpdate";
    public static final String EMPLOYEE_PASSWORD_EMPTY = "";
    public static final String EMPLOYEE_PASSWORD_INVALID_1 = "qwerty";
    public static final String EMPLOYEE_PASSWORD_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";

    public static final String EMPLOYEE_EMAIL_VALID = "employee@mail.com";
    public static final String EMPLOYEE_EMAIL_FOR_UPDATE = "employee.update@mail.com";
    public static final String EMPLOYEE_EMAIL_EMPTY = "";
    public static final String EMPLOYEE_EMAIL_INVALID_1 = "mail";
    public static final String EMPLOYEE_EMAIL_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";

    public static final String EMPLOYEE_FIRSTNAME_VALID = "Oliver";
    public static final String EMPLOYEE_FIRSTNAME_FOR_UPDATE = "Jackson";
    public static final String EMPLOYEE_FIRSTNAME_EMPTY = "";
    public static final String EMPLOYEE_FIRSTNAME_INVALID_1 = "O";
    public static final String EMPLOYEE_FIRSTNAME_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    public static final String EMPLOYEE_LASTNAME_VALID = "Brown";
    public static final String EMPLOYEE_LASTNAME_FOR_UPDATE = "Williams";
    public static final String EMPLOYEE_LASTNAME_EMPTY = "";
    public static final String EMPLOYEE_LASTNAME_INVALID_1 = "W";
    public static final String EMPLOYEE_LASTNAME_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    public static final LocalDate EMPLOYEE_BIRTHDAY_VALID = LocalDate.of(1988, 12, 31);
    public static final LocalDate EMPLOYEE_BIRTHDAY_FOR_UPDATE = LocalDate.of(1989, 1, 1);

    public static final String EMPLOYEE_PHONE_NUMBER_VALID = "380123456789";
    public static final String EMPLOYEE_PHONE_NUMBER_FOR_UPDATE = "380987654321";
    public static final String EMPLOYEE_PHONE_NUMBER_EMPTY = "";
    public static final String EMPLOYEE_PHONE_NUMBER_INVALID_1 = "W";
    public static final String EMPLOYEE_PHONE_NUMBER_INVALID_2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final Set<UserRole> EMPLOYEE_USER_ROLES_VALID = new HashSet<>(Arrays.asList(UserRole.VIEWER));
    public static final Set<UserRole> EMPLOYEE_USER_ROLES_FOR_UPDATE = new HashSet<>(
            Arrays.asList(
                    UserRole.VIEWER,
                    UserRole.EDITOR));
    public static final Set<UserRole> EMPLOYEE_USER_ROLES_EMPTY = new HashSet<>();

    public static final LocalDateTime EMPLOYEE_REGISTER_DATE_VALID = LocalDateTime.of(2023, 8, 1, 9, 1, 1);
    public static final LocalDateTime EMPLOYEE_REGISTER_DATE_FOR_UPDATE = LocalDateTime.of(2023, 8, 3, 12, 12, 12);

    public static final boolean EMPLOYEE_LOCKED_VALID = true;

    public static final EmployeeType EMPLOYEE_EMPLOYEE_TYPE_VALID = EmployeeType.EMPLOYEE;
    public static final EmployeeType EMPLOYEE_EMPLOYEE_TYPE_FOR_UPDATE = EmployeeType.LECTURER;
    public static final EmployeeType LECTURER_EMPLOYEE_TYPE_VALID = EmployeeType.LECTURER;
    public static final EmployeeType LECTURER_EMPLOYEE_TYPE_FOR_UPDATE = EmployeeType.EMPLOYEE;

    public static final Set<Course> LECTURER_COURSES_EMPTY = new HashSet<>();

    private ConstantsTestEmployee() {
    }

    public static Employee getTestEmployee(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, String> usernameMap = new HashMap<>();
        Map<Long, String> passwordMap = new HashMap<>();
        Map<Long, String> emailMap = new HashMap<>();
        Map<Long, String> firstNameMap = new HashMap<>();
        Map<Long, LocalDate> birthdayMap = new HashMap<>();
        Map<Long, String> lastNameMap = new HashMap<>();
        Map<Long, String> phoneNumberMap = new HashMap<>();
        Map<Long, Set<UserRole>> userRolesMap = new HashMap<>();
        Map<Long, LocalDateTime> registerDateMap = new HashMap<>();
        Map<Long, Boolean> lockedMap = new HashMap<>();
        Map<Long, EmployeeType> employeeTypeMap = new HashMap<>();
        // id = 1
        idMap.put(1L, 1L);
        usernameMap.put(1L, "FloresOliverLecturer");
        passwordMap.put(1L, "64ZQBM21BRUT0E2M61ZL1RHH9ZEZPERI");
        emailMap.put(1L, "Flores.Oliver@gmain.com");
        firstNameMap.put(1L, "Oliver");
        lastNameMap.put(1L, "Flores");
        birthdayMap.put(1L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(1L, "phone-4893914754");
        userRolesMap.put(1L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN)));
        registerDateMap.put(1L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(1L, false);
        employeeTypeMap.put(1L, EmployeeType.LECTURER);
        // id = 2
        idMap.put(2L, 2L);
        usernameMap.put(2L, "SmithJacksonLecturer");
        passwordMap.put(2L, "625YNNKWLKHHUQT968ZS5778M6BI51V2");
        emailMap.put(2L, "Smith.Jackson@gmain.com");
        firstNameMap.put(2L, "Jackson");
        lastNameMap.put(2L, "Smith");
        birthdayMap.put(2L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(2L, "phone-7485684585");
        userRolesMap.put(2L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR)));
        registerDateMap.put(2L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(2L, false);
        employeeTypeMap.put(2L, EmployeeType.LECTURER);
        // id = 3
        idMap.put(3L, 3L);
        usernameMap.put(3L, "MartinEmilyLecturer");
        passwordMap.put(3L, "N33I9UXL5EQQZH8BQYK5K1FB37LLRL57");
        emailMap.put(3L, "Martin.Emily@gmain.com");
        firstNameMap.put(3L, "Emily");
        lastNameMap.put(3L, "Martin");
        birthdayMap.put(3L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(3L, "phone-7992439516");
        userRolesMap.put(3L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(3L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(3L, false);
        employeeTypeMap.put(3L, EmployeeType.LECTURER);
        // id = 4
        idMap.put(4L, 4L);
        usernameMap.put(4L, "DavisSebastianLecturer");
        passwordMap.put(4L, "CRFT6IWG03MCQVWXHD9ARRYXQLWINC1E");
        emailMap.put(4L, "Davis.Sebastian@gmain.com");
        firstNameMap.put(4L, "Sebastian");
        lastNameMap.put(4L, "Davis");
        birthdayMap.put(4L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(4L, "phone-7674550918");
        userRolesMap.put(4L, new HashSet<>());
        registerDateMap.put(4L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(4L, false);
        employeeTypeMap.put(4L, EmployeeType.LECTURER);
        // id = 5
        idMap.put(5L, 5L);
        usernameMap.put(5L, "WilliamsSophiaLecturer");
        passwordMap.put(5L, "8538FJ0LD4EY10JAB87M0L0FPOXLWS6X");
        emailMap.put(5L, "Williams.Sophia@gmain.com");
        firstNameMap.put(5L, "Sophia");
        lastNameMap.put(5L, "Williams");
        birthdayMap.put(5L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(5L, "phone-5896767441");
        userRolesMap.put(5L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR)));
        registerDateMap.put(5L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(5L, true);
        employeeTypeMap.put(5L, EmployeeType.LECTURER);
        // id = 6
        idMap.put(6L, 6L);
        usernameMap.put(6L, "FloresJosephLecturer");
        passwordMap.put(6L, "B8PHCBYMQM2J83HSRQQ5F25LUF2KUS9D");
        emailMap.put(6L, "Flores.Joseph@gmain.com");
        firstNameMap.put(6L, "Joseph");
        lastNameMap.put(6L, "Flores");
        birthdayMap.put(6L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(6L, "phone-4575160456");
        userRolesMap.put(6L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(6L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(6L, true);
        employeeTypeMap.put(6L, EmployeeType.LECTURER);
        // id = 26
        idMap.put(26L, 26L);
        usernameMap.put(26L, "MillerNoahEmployee");
        passwordMap.put(26L, "G58PQA7R7Q9ZJG5HBJB89TFRAECGAS8H");
        emailMap.put(26L, "Miller.Noah@gmain.com");
        firstNameMap.put(26L, "Noah");
        lastNameMap.put(26L, "Miller");
        birthdayMap.put(26L, LocalDate.of(1989, 1, 11));
        phoneNumberMap.put(26L, "phone-0811590512");
        userRolesMap.put(26L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(26L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(26L, true);
        employeeTypeMap.put(26L, EmployeeType.EMPLOYEE);
        // id = 27
        idMap.put(27L, 27L);
        usernameMap.put(27L, "ThomasLeviEmployee");
        passwordMap.put(27L, "M6AE9F8BSB84U9IF6DH0A48DFPF06LMJ");
        emailMap.put(27L, "Thomas.Levi@gmain.com");
        firstNameMap.put(27L, "Levi");
        lastNameMap.put(27L, "Thomas");
        birthdayMap.put(27L, LocalDate.of(2000, 7, 16));
        phoneNumberMap.put(27L, "phone-7330666038");
        userRolesMap.put(27L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR)));
        registerDateMap.put(27L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(27L, false);
        employeeTypeMap.put(27L, EmployeeType.EMPLOYEE);
        // id = 28
        idMap.put(28L, 28L);
        usernameMap.put(28L, "PerezLoganEmployee");
        passwordMap.put(28L, "8JO9Y2IB4UJOYP2D8FADG9U7YT4KLU8V");
        emailMap.put(28L, "Perez.Logan@gmain.com");
        firstNameMap.put(28L, "Logan");
        lastNameMap.put(28L, "Perez");
        birthdayMap.put(28L, LocalDate.of(1981, 11, 03));
        phoneNumberMap.put(28L, "phone-5887429939");
        userRolesMap.put(28L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN)));
        registerDateMap.put(28L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(28L, false);
        employeeTypeMap.put(28L, EmployeeType.EMPLOYEE);

        Employee employee = new Employee();
        employee.setId(idMap.get(id));
        employee.setUsername(usernameMap.get(id));
        employee.setPassword(passwordMap.get(id));
        employee.setEmail(emailMap.get(id));
        employee.setFirstName(firstNameMap.get(id));
        employee.setLastName(lastNameMap.get(id));
        employee.setBirthday(birthdayMap.get(id));
        employee.setPhoneNumber(phoneNumberMap.get(id));
        employee.setUserRoles(userRolesMap.get(id));
        employee.setRegisterDate(registerDateMap.get(id));
        employee.setLocked(lockedMap.get(id));
        employee.setEmployeeType(employeeTypeMap.get(id));
        employee.setCourses(new HashSet<>());
        return employee;
    }

    public static List<Employee> getAllTestEmployees() {
        Employee employee1 = ConstantsTestEmployee.getTestEmployee(1L);
        Course course1 = ConstantsTestCourse.getTestCourse(1L);
        Course course2 = ConstantsTestCourse.getTestCourse(2L);
        employee1.addCourse(course1);
        employee1.addCourse(course2);

        Employee employee2 = ConstantsTestEmployee.getTestEmployee(2L);
        Course course3 = ConstantsTestCourse.getTestCourse(3L);
        employee2.addCourse(course3);

        Employee employee3 = ConstantsTestEmployee.getTestEmployee(3L);
        Course course5 = ConstantsTestCourse.getTestCourse(5L);
        Course course6 = ConstantsTestCourse.getTestCourse(6L);
        employee3.addCourse(course5);
        employee3.addCourse(course6);

        Employee employee4 = ConstantsTestEmployee.getTestEmployee(4L);
        Course course7 = ConstantsTestCourse.getTestCourse(7L);
        Course course9 = ConstantsTestCourse.getTestCourse(9L);
        Course course11 = ConstantsTestCourse.getTestCourse(11L);
        Course course12 = ConstantsTestCourse.getTestCourse(12L);
        employee4.addCourse(course7);
        employee4.addCourse(course9);
        employee4.addCourse(course11);
        employee4.addCourse(course12);

        Employee employee5 = ConstantsTestEmployee.getTestEmployee(5L);
        Course course8 = ConstantsTestCourse.getTestCourse(8L);
        employee5.addCourse(course8);

        Employee employee6 = ConstantsTestEmployee.getTestEmployee(6L);

        Employee employee26 = ConstantsTestEmployee.getTestEmployee(26L);
        Employee employee27 = ConstantsTestEmployee.getTestEmployee(27L);
        Employee employee28 = ConstantsTestEmployee.getTestEmployee(28L);

        return new ArrayList<Employee>(Arrays.asList(employee1, employee2, employee3,
                employee4, employee5, employee6, employee26, employee27, employee28));
    }

    public static List<Employee> getAllTestEmployeesWithEmployeeTypeAsLecturer() {
        Employee employee1 = ConstantsTestEmployee.getTestEmployee(1L);
        Course course1 = ConstantsTestCourse.getTestCourse(1L);
        Course course2 = ConstantsTestCourse.getTestCourse(2L);
        employee1.addCourse(course1);
        employee1.addCourse(course2);

        Employee employee2 = ConstantsTestEmployee.getTestEmployee(2L);
        Course course3 = ConstantsTestCourse.getTestCourse(3L);
        employee2.addCourse(course3);

        Employee employee3 = ConstantsTestEmployee.getTestEmployee(3L);
        Course course5 = ConstantsTestCourse.getTestCourse(5L);
        Course course6 = ConstantsTestCourse.getTestCourse(6L);
        employee3.addCourse(course5);
        employee3.addCourse(course6);

        Employee employee4 = ConstantsTestEmployee.getTestEmployee(4L);
        Course course7 = ConstantsTestCourse.getTestCourse(7L);
        Course course9 = ConstantsTestCourse.getTestCourse(9L);
        Course course11 = ConstantsTestCourse.getTestCourse(11L);
        Course course12 = ConstantsTestCourse.getTestCourse(12L);
        employee4.addCourse(course7);
        employee4.addCourse(course9);
        employee4.addCourse(course11);
        employee4.addCourse(course12);

        Employee employee5 = ConstantsTestEmployee.getTestEmployee(5L);
        Course course8 = ConstantsTestCourse.getTestCourse(8L);
        employee5.addCourse(course8);

        Employee employee6 = ConstantsTestEmployee.getTestEmployee(6L);

        return new ArrayList<Employee>(Arrays.asList(employee1, employee2, employee3,
                employee4, employee5, employee6));
    }

    public static List<Employee> getAllTestEmployeesWithEmployeeTypeAsEmployee() {
        Employee employee26 = ConstantsTestEmployee.getTestEmployee(26L);
        Employee employee27 = ConstantsTestEmployee.getTestEmployee(27L);
        Employee employee28 = ConstantsTestEmployee.getTestEmployee(28L);

        return new ArrayList<Employee>(Arrays.asList(employee26, employee27, employee28));
    }

    public static Employee newValidEmployee(Long id) {
        Employee employee = newValidEmployee();
        employee.setId(id);
        return employee;
    }

    public static Employee newValidEmployee() {
        Employee employee = new Employee();
        employee.setUsername(ConstantsTestEmployee.EMPLOYEE_USERNAME_VALID);
        employee.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_VALID);
        employee.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_VALID);
        employee.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_VALID);
        employee.setLastName(ConstantsTestEmployee.EMPLOYEE_LASTNAME_VALID);
        employee.setBirthday(ConstantsTestEmployee.EMPLOYEE_BIRTHDAY_VALID);
        employee.setPhoneNumber(ConstantsTestEmployee.EMPLOYEE_PHONE_NUMBER_VALID);
        employee.setUserRoles(ConstantsTestEmployee.EMPLOYEE_USER_ROLES_VALID);
        employee.setRegisterDate(ConstantsTestEmployee.EMPLOYEE_REGISTER_DATE_VALID);
        employee.setLocked(ConstantsTestEmployee.EMPLOYEE_LOCKED_VALID);
        employee.setEmployeeType(ConstantsTestEmployee.EMPLOYEE_EMPLOYEE_TYPE_VALID);
        employee.setCourses(ConstantsTestEmployee.LECTURER_COURSES_EMPTY);
        return employee;
    }

}
