package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import org.springframework.validation.BindingResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class AdminLecturesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LectureService lectureServiceMock;

    @MockBean
    AuditoriumService auditoriumServiceMock;

    @MockBean
    CourseService courseServiceMock;

    @MockBean
    GroupService groupServiceMock;

    @MockBean
    EmployeeService employeeServiceMock;

    @Autowired
    CustomEntityValidator<Lecture> customEntityValidator;

    @Test
    void newEntityPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/admin/lectures/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/admin/lectures/new")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void newEntityPage_WhenCalledWithAdminRole_ShouldReturnNewLectureView() throws Exception {
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        MvcResult resultController = mvc.perform(get("/admin/lectures/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/lectures/lecture_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonNewLectureViewAttributes(resultController);
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("new_lecture");
        assertThat(resultController.getModelAndView().getModel().get("new_lecture"))
                .isInstanceOf(Lecture.class);

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void newEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(post("/admin/lectures/new")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(post("/admin/lectures/new")
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1,
            ConstantsTestLecture.LECTURE_SUBJECT_INVALID_2
    })
    void newEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnNewLectureViewWithMessage(
            String parameterizedSubject) throws Exception {
        Lecture newLecture = ConstantsTestLecture.newValidLecture();

        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        newLecture.setSubject(parameterizedSubject);
        MvcResult resultController = mvc.perform(post("/admin/lectures/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .flashAttr("new_lecture", newLecture)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/lectures/lecture_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonNewLectureViewAttributes(resultController);
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("new_lecture")
                .containsKey("err_new_lecture");
        checkForMessagePresenceInBindingResult(resultController, "new_lecture", "subject");
        checkForMessagePresenceInModelMap(resultController, "err_new_lecture",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_lecture"))
                .isInstanceOf(Lecture.class);

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Lecture newLecture = ConstantsTestLecture.newValidLecture();

        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(lectureServiceMock.add(Mockito.any(Lecture.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post("/admin/lectures/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .flashAttr("new_lecture", newLecture)
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).add(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataIntegrityViolationException_ShouldReturnNewLectureViewWithErrorMessage()
            throws Exception {
        Lecture newLecture = ConstantsTestLecture.newValidLecture();

        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(lectureServiceMock.add(Mockito.any(Lecture.class)))
                .thenThrow(new EntityAddDataIntegrityViolationException(Constants.DEFAULT_ERROR_MESSAGE));

        MvcResult resultController = mvc.perform(post("/admin/lectures/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .flashAttr("new_lecture", newLecture)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/lectures/lecture_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkCommonNewLectureViewAttributes(resultController);
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("new_lecture")
                .containsKey("gErrors");
        checkForMessagePresenceInModelMap(resultController, "gErrors", Constants.DEFAULT_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_lecture"))
                .isInstanceOf(Lecture.class);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).add(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndHappyPath_ShouldAddLectureAndReturnLecturesView() throws Exception {
        Lecture newLecture = ConstantsTestLecture.newValidLecture();

        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        MvcResult resultController = mvc.perform(post("/admin/lectures/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .flashAttr("new_lecture", newLecture)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attributeExists("gMessages"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gMessages", "Lecture added successfully");

        Mockito.verify(lectureServiceMock, Mockito.times(1)).add(Mockito.any(Lecture.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        Long entityForDeleteId = ConstantsTestLecture.LECTURE_ID_FOR_DELETE;

        mvc.perform(get("/admin/lectures/delete/{id}", entityForDeleteId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void deleteEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Long entityForDeleteId = ConstantsTestLecture.LECTURE_ID_FOR_DELETE;

        mvc.perform(get("/admin/lectures/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Long entityForDeleteId = ConstantsTestLecture.LECTURE_ID_FOR_DELETE;

        Mockito.when(lectureServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/lectures/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "gErrors, false, Failed to delete Lecture",
            "gMessages, true, Lecture deleted successfully"
    })
    void deleteEntity_WhenCalledWithAdminRoleAndLectureNotDeletedOrDeleted_ShouldReturnLecturesViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Long entityForDeleteId = ConstantsTestLecture.LECTURE_ID_FOR_DELETE;

        Mockito.when(lectureServiceMock.deleteById(Mockito.anyLong()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/lectures/delete/{id}", entityForDeleteId)
                .header("Referer", "/lectures")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(flash().attributeExists(parameterizedAttribute))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    private void checkCommonNewLectureViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Lecture")
                .containsEntry("availableAuditoriums", ConstantsTestAuditorium.getAllTestAuditoriums())
                .containsEntry("allCourses", ConstantsTestCourse.getAllTestCourses())
                .containsEntry("allGroups", ConstantsTestGroup.getAllTestGroups())
                .containsEntry("allLecturers", ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer())
                .containsKey("timeSlots");
    }

    private void checkForMessagePresenceInModelMap(MvcResult resultController, String attribute, String message) {
        assertThat(resultController.getModelAndView().getModelMap())
                .containsKey(attribute);
        assertThat((ArrayList<String>) resultController.getModelAndView().getModelMap().getAttribute(attribute))
                .containsSequence(message);
    }

    private void checkForMessagePresenceInFlashMap(MvcResult resultController, String attribute, String message) {
        assertTrue(resultController.getFlashMap().containsKey(attribute));
        assertThat((ArrayList<String>) resultController.getFlashMap().get(attribute))
                .containsSequence(message);
    }

    private void checkForMessagePresenceInBindingResult(MvcResult resultController, String attribute, String field) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("org.springframework.validation.BindingResult." + attribute);
        BindingResult modelBindingResult = (BindingResult) resultController.getModelAndView().getModel()
                .get("org.springframework.validation.BindingResult." + attribute);
        assertTrue(modelBindingResult.hasErrors());
        assertTrue(modelBindingResult.hasFieldErrors(field));
    }

    private static Stream<Arguments> provideAuthoritiesForForbiddenScenario() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR"))));
    }

}
