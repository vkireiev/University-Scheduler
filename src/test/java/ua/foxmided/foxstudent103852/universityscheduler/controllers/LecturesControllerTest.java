package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.security.SecurityPersonDetails;
import ua.foxmided.foxstudent103852.universityscheduler.service.LectureServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@AutoConfigureMockMvc
class LecturesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LectureServiceImpl lectureServiceMock;

    private final Long entityForViewId = ConstantsTestLecture.LECTURE_ID_VALID_1;

    @Test
    void lecturesPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/lectures"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void lecturesPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/lectures")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void lecturesPage_WhenCalledAndLoggedAsStudentWithViewRole_ShouldReturnStudentLectures()
            throws Exception {
        Person loggedUserMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        loggedUserMock.setUserRoles(Set.of(UserRole.VIEWER));
        ((Student) loggedUserMock).setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        assertNotNull(((Student) loggedUserMock).getGroup());

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getGroups().contains(((Student) loggedUserMock).getGroup()))
                .collect(Collectors.toList());
        Mockito.when(lectureServiceMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .param("show_all", "1")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", false);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void lecturesPage_WhenCalledAndLoggedAsEmployeeWithViewRole_ShouldReturnEmployeeLectures()
            throws Exception {
        Person loggedUserMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        loggedUserMock.setUserRoles(Set.of(UserRole.VIEWER));

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getLecturer().getId() == loggedUserMock.getId())
                .collect(Collectors.toList());
        Mockito.when(lectureServiceMock
                .findAllByLecturerAndLectureDateBetween(
                        Mockito.any(Employee.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .param("show_all", "1")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", false);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        Mockito.any(Employee.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideParameterizedArgumentsForLecturesDateBetweenScenario")
    void lecturesPage_WhenCalledAndLoggedWithEditOrAdminRole_ShouldReturnLecturesDateBetween(
            Person parameterizedPerson, Set<UserRole> parameterizedUserRoles) throws Exception {
        Person loggedUserMock = parameterizedPerson;
        loggedUserMock.setUserRoles(parameterizedUserRoles);

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures();
        Mockito.when(lectureServiceMock
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", true);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideParameterizedArgumentsForLecturesDateBetweenScenario")
    void lecturesPage_WhenCalledAndLoggedWithEditOrAdminRoleWithShowAll_ShouldReturnLecturesDateBetween(
            Person parameterizedPerson, Set<UserRole> parameterizedUserRoles) throws Exception {
        Person loggedUserMock = parameterizedPerson;
        loggedUserMock.setUserRoles(parameterizedUserRoles);

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures();
        Mockito.when(lectureServiceMock
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .param("show_all", "1")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", true);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByLectureDateBetween(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void lecturesPage_WhenCalledAndLoggedAsStudentWithEditOrAdminRoleAndWithoutShowAll_ShouldReturnStudentLectures()
            throws Exception {
        Person loggedUserMock = ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1);
        loggedUserMock.setUserRoles(Set.of(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN));
        ((Student) loggedUserMock).setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        assertNotNull(((Student) loggedUserMock).getGroup());

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getGroups().contains(((Student) loggedUserMock).getGroup()))
                .collect(Collectors.toList());
        Mockito.when(lectureServiceMock
                .findAllByGroupsContainingAndLectureDateBetween(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .param("show_all", "0")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", false);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByGroupsContainingAndLectureDateBetween(
                        Mockito.any(Group.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void lecturesPage_WhenCalledAndLoggedAsEmployeeWithEditOrAdminRoleAndWithoutShowAll_ShouldReturnEmployeeLectures()
            throws Exception {
        Person loggedUserMock = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1);
        loggedUserMock.setUserRoles(Set.of(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN));

        List<Lecture> resultMock = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getLecturer().getId() == loggedUserMock.getId())
                .collect(Collectors.toList());
        Mockito.when(lectureServiceMock
                .findAllByLecturerAndLectureDateBetween(
                        Mockito.any(Employee.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)))
                .thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/lectures")
                .param("show_all", "0")
                .with(user(new SecurityPersonDetails(loggedUserMock))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lectures"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonLecturesViewAttributes(resultController);
        compareLectures(resultMock,
                (Map<LocalDate, Map<TimeSlot, Set<Lecture>>>) resultController.getModelAndView().getModel()
                        .get("scheduledLectures"));
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("showAll", false);

        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findAllByLecturerAndLectureDateBetween(
                        Mockito.any(Employee.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @Test
    void lectureViewPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/lectures/view/{id}", entityForViewId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void lectureViewPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/lectures/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void lectureViewPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(lectureServiceMock.get(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/lectures/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void lectureViewPage_WhenCalledWithAccessibleScenarioAndNotExistLectureWithSuchId_ShouldReturnViewWithErrorMessage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(lectureServiceMock.get(Mockito.anyLong())).thenReturn(Optional.empty());

        mvc.perform(get("/lectures/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Lecture not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void lectureViewPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnLectureView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Lecture entityForViewMock = ConstantsTestLecture.getTestLecture(entityForViewId);

        Mockito.when(lectureServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(entityForViewMock));

        MvcResult resultController = mvc.perform(get("/lectures/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lecture_view"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Lecture view");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("lecture_view");
        assertThat(resultController.getModelAndView().getModel().get("lecture_view"))
                .usingRecursiveComparison()
                .isEqualTo(entityForViewMock);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    private void checkCommonLecturesViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "My Schedule")
                .containsKey("date1")
                .containsKey("date2")
                .containsKey("showAll")
                .containsKey("calendar")
                .containsKey("scheduledLectures")
                .containsKey("daysOfWeek")
                .containsKey("timeSlots");
    }

    private void compareLectures(List<Lecture> lectures,
            Map<LocalDate, Map<TimeSlot, Set<Lecture>>> scheduledLectures) {
        List<Lecture> lecturesFromSchedule = new ArrayList<>();
        scheduledLectures.forEach((day, dayLectures) -> {
            dayLectures.values().forEach(lecture -> lecturesFromSchedule.addAll(lecture));
        });
        lectures.sort(Comparator.comparing(Lecture::getId));
        lecturesFromSchedule.sort(Comparator.comparing(Lecture::getId));
        assertThat(lectures)
                .usingRecursiveComparison()
                .isEqualTo(lecturesFromSchedule);
    }

    private static Stream<Arguments> provideAuthoritiesForAccessDeniedScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"))));
    }

    private static Stream<Arguments> provideAuthoritiesForAccessibleScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("VIEWER"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("ADMIN"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("VIEWER"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("ADMIN"))));
    }

    private static Stream<Arguments> provideParameterizedArgumentsForLecturesDateBetweenScenario() {
        return Stream.of(
                Arguments.of(
                        ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1),
                        Set.of(UserRole.VIEWER, UserRole.EDITOR)),
                Arguments.of(
                        ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_1),
                        Set.of(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN)),
                Arguments.of(
                        ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1),
                        Set.of(UserRole.VIEWER, UserRole.EDITOR)),
                Arguments.of(
                        ConstantsTestStudent.getTestStudent(ConstantsTestStudent.STUDENT_ID_VALID_1),
                        Set.of(UserRole.VIEWER, UserRole.EDITOR, UserRole.ADMIN)));
    }

}