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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

public final class ConstantsTestStudent {
    public static final Long STUDENT_ID_VALID_1 = 53L;
    public static final Long STUDENT_ID_VALID_2 = 56L;
    public static final Long STUDENT_ID_FOR_DELETE = 58L;
    public static final Long STUDENT_ID_INVALID = 0L;
    public static final Long STUDENT_ID_NOT_EXIST = 111L;

    public static final String STUDENT_USERNAME_VALID = "student01";
    public static final String STUDENT_USERNAME_FOR_UPDATE = "studentUpdate";
    public static final String STUDENT_USERNAME_EMPTY = "";
    public static final String STUDENT_USERNAME_INVALID_1 = "s";
    public static final String STUDENT_USERNAME_INVALID_2 = "stud-invalid1,stud-invalid2,stud-invalid3,"
            + "stud-invalid4,stud-invalid5";

    public static final String STUDENT_PASSWORD_VALID = "passwordStrong";
    public static final String STUDENT_PASSWORD_FOR_UPDATE = "passwordUpdate";
    public static final String STUDENT_PASSWORD_EMPTY = "";
    public static final String STUDENT_PASSWORD_INVALID_1 = "qwerty";
    public static final String STUDENT_PASSWORD_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";

    public static final String STUDENT_EMAIL_VALID = "student@mail.com";
    public static final String STUDENT_EMAIL_FOR_UPDATE = "student.update@mail.com";
    public static final String STUDENT_EMAIL_EMPTY = "";
    public static final String STUDENT_EMAIL_INVALID_1 = "mail";
    public static final String STUDENT_EMAIL_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";

    public static final String STUDENT_FIRSTNAME_VALID = "Owen";
    public static final String STUDENT_FIRSTNAME_FOR_UPDATE = "Upd-James";
    public static final String STUDENT_FIRSTNAME_EMPTY = "";
    public static final String STUDENT_FIRSTNAME_INVALID_1 = "O";
    public static final String STUDENT_FIRSTNAME_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    public static final String STUDENT_LASTNAME_VALID = "Garcia";
    public static final String STUDENT_LASTNAME_FOR_UPDATE = "Upd-Thompson";
    public static final String STUDENT_LASTNAME_EMPTY = "";
    public static final String STUDENT_LASTNAME_INVALID_1 = "W";
    public static final String STUDENT_LASTNAME_INVALID_2 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    public static final LocalDate STUDENT_BIRTHDAY_VALID = LocalDate.of(1988, 12, 31);
    public static final LocalDate STUDENT_BIRTHDAY_FOR_UPDATE = LocalDate.of(1989, 1, 1);

    public static final String STUDENT_PHONE_NUMBER_VALID = "380123456789";
    public static final String STUDENT_PHONE_NUMBER_FOR_UPDATE = "Upd-380987654321";
    public static final String STUDENT_PHONE_NUMBER_EMPTY = "";
    public static final String STUDENT_PHONE_NUMBER_INVALID_1 = "W";
    public static final String STUDENT_PHONE_NUMBER_INVALID_2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final Set<UserRole> STUDENT_USER_ROLES_VALID = new HashSet<>(Arrays.asList(UserRole.VIEWER));
    public static final Set<UserRole> STUDENT_USER_ROLES_FOR_UPDATE = new HashSet<>(
            Arrays.asList(
                    UserRole.VIEWER,
                    UserRole.EDITOR));
    public static final Set<UserRole> STUDENT_USER_ROLES_EMPTY = new HashSet<>();

    public static final LocalDateTime STUDENT_REGISTER_DATE_VALID = LocalDateTime.of(2023, 8, 1, 9, 1, 1);
    public static final LocalDateTime STUDENT_REGISTER_DATE_FOR_UPDATE = LocalDateTime.of(2023, 8, 3, 12, 12, 12);

    public static final boolean STUDENT_LOCKED_VALID = true;

    public static final Group STUDENT_GROUP_VALID = ConstantsTestGroup
            .getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);

    private ConstantsTestStudent() {
    }

    public static Student getTestStudent(@NotNull Long id) {
        Map<Long, Long> idMap = new HashMap<>();
        Map<Long, String> usernameMap = new HashMap<>();
        Map<Long, String> passwordMap = new HashMap<>();
        Map<Long, String> emailMap = new HashMap<>();
        Map<Long, String> firstNameMap = new HashMap<>();
        Map<Long, String> lastNameMap = new HashMap<>();
        Map<Long, LocalDate> birthdayMap = new HashMap<>();
        Map<Long, String> phoneNumberMap = new HashMap<>();
        Map<Long, Set<UserRole>> userRolesMap = new HashMap<>();
        Map<Long, LocalDateTime> registerDateMap = new HashMap<>();
        Map<Long, Boolean> lockedMap = new HashMap<>();
        // id = 51
        idMap.put(51L, 51L);
        usernameMap.put(51L, "MartinezDanielStudent");
        passwordMap.put(51L, "S7HF02ZBYIB3QA7DY549L6O17ND3TY77");
        emailMap.put(51L, "Martinez.Daniel@gmain.com");
        firstNameMap.put(51L, "Daniel");
        lastNameMap.put(51L, "Martinez");
        birthdayMap.put(51L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(51L, "phone-4724186297");
        userRolesMap.put(51L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN)));
        registerDateMap.put(51L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(51L, false);
        // id = 52
        idMap.put(52L, 52L);
        usernameMap.put(52L, "GarciaNoahStudent");
        passwordMap.put(52L, "J1UEQ58SHF7OHK64PYWDFCUZ1SUVMHRN");
        emailMap.put(52L, "Garcia.Noah@gmain.com");
        firstNameMap.put(52L, "Noah");
        lastNameMap.put(52L, "Garcia");
        birthdayMap.put(52L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(52L, "phone-1659893076");
        userRolesMap.put(52L, new HashSet<>(Arrays.asList(UserRole.VIEWER, UserRole.EDITOR)));
        registerDateMap.put(52L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(52L, false);
        // id = 53
        idMap.put(53L, 53L);
        usernameMap.put(53L, "ThompsonMiaStudent");
        passwordMap.put(53L, "EFZBMHI0J9UJQQUKKJIZWTVAR86RRF7O");
        emailMap.put(53L, "Thompson.Mia@gmain.com");
        firstNameMap.put(53L, "Mia");
        lastNameMap.put(53L, "Thompson");
        birthdayMap.put(53L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(53L, "phone-9692719092");
        userRolesMap.put(53L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(53L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(53L, false);
        // id = 54
        idMap.put(54L, 54L);
        usernameMap.put(54L, "HallSophiaStudent");
        passwordMap.put(54L, "9DOK8YFRQZJMDE1O6E08GLCU157F1TKP");
        emailMap.put(54L, "Hall.Sophia@gmain.com");
        firstNameMap.put(54L, "Sophia");
        lastNameMap.put(54L, "Hall");
        birthdayMap.put(54L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(54L, "phone-5319360589");
        userRolesMap.put(54L, new HashSet<>());
        registerDateMap.put(54L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(54L, false);
        // id = 55
        idMap.put(55L, 55L);
        usernameMap.put(55L, "LeeJosephStudent");
        passwordMap.put(55L, "3NVMQYPTVW2E6JJB8C6WS1XSEC7BW8TQ");
        emailMap.put(55L, "Lee.Joseph@gmain.com");
        firstNameMap.put(55L, "Joseph");
        lastNameMap.put(55L, "Lee");
        birthdayMap.put(55L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(55L, "phone-2141261178");
        userRolesMap.put(55L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(55L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(55L, true);
        // id = 56
        idMap.put(56L, 56L);
        usernameMap.put(56L, "HernandezEmilyStudent");
        passwordMap.put(56L, "R9ZU3IUMS9EIQ1IOO709X8DD7KK1WIAN");
        emailMap.put(56L, "Hernandez.Emily@gmain.com");
        firstNameMap.put(56L, "Emily");
        lastNameMap.put(56L, "Hernandez");
        birthdayMap.put(56L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(56L, "phone-6219916736");
        userRolesMap.put(56L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(56L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(56L, false);
        // id = 57
        idMap.put(57L, 57L);
        usernameMap.put(57L, "LeeJamesStudent");
        passwordMap.put(57L, "78AP3JVNBBIJWB8E9X2ATJIFQGL4YOR1");
        emailMap.put(57L, "Lee.James@gmain.com");
        firstNameMap.put(57L, "James");
        lastNameMap.put(57L, "Lee");
        birthdayMap.put(57L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(57L, "phone-7378352102");
        userRolesMap.put(57L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(57L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(57L, false);
        // id = 58
        idMap.put(58L, 58L);
        usernameMap.put(58L, "HallOwenStudent");
        passwordMap.put(58L, "GKC8IARVDEIQ8ZEZOME75P9OKQ9Z1QAS");
        emailMap.put(58L, "Hall.Owen@gmain.com");
        firstNameMap.put(58L, "Owen");
        lastNameMap.put(58L, "Hall");
        birthdayMap.put(58L, LocalDate.of(2023, 7, 18));
        phoneNumberMap.put(58L, "phone-4954225404");
        userRolesMap.put(58L, new HashSet<>(Arrays.asList(UserRole.VIEWER)));
        registerDateMap.put(58L, LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0));
        lockedMap.put(58L, false);

        Student student = new Student();
        student.setId(idMap.get(id));
        student.setUsername(usernameMap.get(id));
        student.setPassword(passwordMap.get(id));
        student.setEmail(emailMap.get(id));
        student.setFirstName(firstNameMap.get(id));
        student.setLastName(lastNameMap.get(id));
        student.setBirthday(birthdayMap.get(id));
        student.setPhoneNumber(phoneNumberMap.get(id));
        student.setUserRoles(userRolesMap.get(id));
        student.setRegisterDate(registerDateMap.get(id));
        student.setLocked(lockedMap.get(id));
        return student;
    }

    public static List<Student> getAllTestStudents() {
        Student student51 = ConstantsTestStudent.getTestStudent(51L);
        Group group3 = ConstantsTestGroup.getTestGroup(3L);
        group3.addStudent(student51);

        Student student52 = ConstantsTestStudent.getTestStudent(52L);
        Group group2 = ConstantsTestGroup.getTestGroup(2L);
        group2.addStudent(student52);

        Student student53 = ConstantsTestStudent.getTestStudent(53L);
        Student student54 = ConstantsTestStudent.getTestStudent(54L);
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        group1.addStudent(student53);
        group1.addStudent(student54);

        Student student55 = ConstantsTestStudent.getTestStudent(55L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);
        group4.addStudent(student55);

        Group group7 = ConstantsTestGroup.getTestGroup(7L);
        Student student56 = ConstantsTestStudent.getTestStudent(56L);
        group7.addStudent(student56);

        Student student57 = ConstantsTestStudent.getTestStudent(57L);
        Student student58 = ConstantsTestStudent.getTestStudent(58L);
        Group group5 = ConstantsTestGroup.getTestGroup(5L);
        group5.addStudent(student57);
        group5.addStudent(student58);

        return new ArrayList<Student>(Arrays.asList(student51, student52, student53,
                student54, student55, student56, student57, student58));
    }

    public static Student newValidStudent(@NotNull Long id) {
        Student student = newValidStudent();
        student.setId(id);
        return student;
    }

    public static Student newValidStudent() {
        Student student = new Student();
        student.setUsername(ConstantsTestStudent.STUDENT_USERNAME_VALID);
        student.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_VALID);
        student.setEmail(ConstantsTestStudent.STUDENT_EMAIL_VALID);
        student.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_VALID);
        student.setLastName(ConstantsTestStudent.STUDENT_LASTNAME_VALID);
        student.setBirthday(ConstantsTestStudent.STUDENT_BIRTHDAY_VALID);
        student.setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_VALID);
        student.setUserRoles(ConstantsTestStudent.STUDENT_USER_ROLES_VALID);
        student.setRegisterDate(ConstantsTestStudent.STUDENT_REGISTER_DATE_VALID);
        student.setLocked(ConstantsTestStudent.STUDENT_LOCKED_VALID);
        student.setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        return student;
    }

}
