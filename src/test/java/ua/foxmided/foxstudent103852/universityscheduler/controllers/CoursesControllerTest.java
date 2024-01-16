package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.service.CourseServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CourseServiceImpl courseServiceMock;

    private final Long entityForViewId = ConstantsTestCourse.COURSE_ID_VALID_1;

    @Test
    void coursesPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/courses"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void coursesPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/courses")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void coursesPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(courseServiceMock.getAll())
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/courses")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void coursesPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnCoursesView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        List<Course> resultMock = ConstantsTestCourse.getAllTestCourses();

        Mockito.when(courseServiceMock.getAll()).thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/courses")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Courses");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("courses");
        assertThat(resultController.getModelAndView().getModel().get("courses"))
                .usingRecursiveComparison()
                .isEqualTo(resultMock);
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("entitiesCount", resultMock.size());

        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void courseViewPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/courses/view/{id}", entityForViewId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void courseViewPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/courses/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void courseViewPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/courses/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void courseViewPage_WhenCalledWithAccessibleScenarioAndNotExistCourseWithSuchId_ShouldReturnViewWithErrorMessage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.empty());

        mvc.perform(get("/courses/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Course not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void courseViewPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnCourseView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Course entityForViewMock = ConstantsTestCourse.getTestCourse(entityForViewId);

        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(entityForViewMock));

        MvcResult resultController = mvc.perform(get("/courses/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/course_view"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Course view");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("course_view");
        assertThat(resultController.getModelAndView().getModel().get("course_view"))
                .usingRecursiveComparison()
                .isEqualTo(entityForViewMock);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
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

}
