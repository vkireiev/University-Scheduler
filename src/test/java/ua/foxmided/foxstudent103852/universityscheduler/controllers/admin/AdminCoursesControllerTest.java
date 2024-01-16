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
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class AdminCoursesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CourseService courseServiceMock;

    @Autowired
    CustomEntityValidator<Course> customEntityValidator;

    @Test
    void newEntityPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/admin/courses/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/admin/courses/new")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void newEntityPage_WhenCalledWithAdminRole_ShouldReturnNewCourseView() throws Exception {
        MvcResult resultController = mvc.perform(get("/admin/courses/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/courses/course_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Course")
                .containsKey("new_course");
        assertThat(resultController.getModelAndView().getModel().get("new_course"))
                .isInstanceOf(Course.class);

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void newEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(post("/admin/courses/new")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(post("/admin/courses/new")
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestCourse.COURSE_NAME_INVALID_1,
            ConstantsTestCourse.COURSE_NAME_INVALID_2
    })
    void newEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnNewCourseViewWithMessage(
            String parameterizedName) throws Exception {
        Course newCourse = ConstantsTestCourse.newValidCourse();
        newCourse.setName(parameterizedName);

        MvcResult resultController = mvc.perform(post("/admin/courses/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromCourseForPostForm(newCourse))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/courses/course_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Course")
                .containsKey("new_course")
                .containsKey("err_new_course");
        checkForMessagePresenceInBindingResult(resultController, "new_course", "name");
        checkForMessagePresenceInModelMap(resultController, "err_new_course",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_course"))
                .isInstanceOf(Course.class);

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Course newCourse = ConstantsTestCourse.newValidCourse();

        Mockito.when(courseServiceMock.add(Mockito.any(Course.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post("/admin/courses/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromCourseForPostForm(newCourse))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).add(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndHappyPath_ShouldAddCourseAndReturnCoursesView() throws Exception {
        Course newCourse = ConstantsTestCourse.newValidCourse();

        MvcResult resultController = mvc.perform(post("/admin/courses/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromCourseForPostForm(newCourse))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attributeExists("gMessages"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gMessages", "Course added successfully");

        Mockito.verify(courseServiceMock, Mockito.times(1)).add(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        Long entityForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;

        mvc.perform(get("/admin/courses/delete/{id}", entityForDeleteId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void deleteEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Long entityForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;

        mvc.perform(get("/admin/courses/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Long entityForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;

        Mockito.when(courseServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/courses/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndExistCourseAndAssignedLecturerOrGroup_ShouldReturnRefererViewWithMessage()
            throws Exception {
        Long entityForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;

        Mockito.when(courseServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new EntityDataIntegrityViolationException(
                        "Before deleting Course, remove all Lecturers/Groups from the Course"));

        MvcResult resultController = mvc.perform(get("/admin/courses/delete/{id}", entityForDeleteId)
                .header("Referer", "/courses/edit/" + entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForDeleteId))
                .andExpect(flash().attributeExists("gErrors"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gErrors",
                "Before deleting Course, remove all Lecturers/Groups from the Course");

        Mockito.verify(courseServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "gErrors, false, Failed to delete Course",
            "gMessages, true, Course deleted successfully"
    })
    void deleteEntity_WhenCalledWithAdminRoleAndCourseNotDeletedOrDeleted_ShouldReturnCoursesViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Long entityForDeleteId = ConstantsTestCourse.COURSE_ID_FOR_DELETE;
        Course entityForDeleteMock = ConstantsTestCourse.getTestCourse(entityForDeleteId);
        assertTrue(entityForDeleteMock.getLecturers().isEmpty());
        assertTrue(entityForDeleteMock.getGroups().isEmpty());

        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(entityForDeleteMock));
        Mockito.when(courseServiceMock.deleteById(Mockito.anyLong()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/courses/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"))
                .andExpect(flash().attributeExists(parameterizedAttribute))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(courseServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromCourseForPostForm(@NotNull Course course) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("name", course.getName());
        result.add("description", course.getDescription());
        return result;
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
