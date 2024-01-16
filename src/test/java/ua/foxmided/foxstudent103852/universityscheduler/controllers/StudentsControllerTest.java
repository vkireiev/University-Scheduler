package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collection;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.StudentServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@AutoConfigureMockMvc
class StudentsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentServiceImpl studentServiceMock;

    @Test
    void studentsPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/students"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void studentsPage_WhenCalledWithForbiddenScenario_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/students")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void studentsPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(studentServiceMock.getAll())
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/students")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(studentServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(studentServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void studentsPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnStudentsView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        List<Student> resultMock = ConstantsTestStudent.getAllTestStudents();

        Mockito.when(studentServiceMock.getAll()).thenReturn(resultMock);
        Mockito.when(studentServiceMock.count()).thenReturn((long) resultMock.size());

        MvcResult resultController = mvc.perform(get("/students")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Students");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("students");
        List<Student> resultReturned = (List<Student>) resultController.getModelAndView().getModel().get("students");
        assertThat(resultReturned)
                .hasSameElementsAs(resultMock);
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("entitiesCount", resultMock.size());

        Mockito.verify(studentServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(studentServiceMock);
    }

    @Test
    @WithMockUser(authorities = { "EMPLOYEE" })
    void studentsPage_WhenCalledAsEmployeeWithoutAnyRoles_ShouldReturnAccessDeniedView() throws Exception {
        mvc.perform(get("/students"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(studentServiceMock);
    }

    private static Stream<Arguments> provideAuthoritiesForForbiddenScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("VIEWER"))));
    }

    private static Stream<Arguments> provideAuthoritiesForAccessibleScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"), new SimpleGrantedAuthority("ADMIN"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("VIEWER"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"), new SimpleGrantedAuthority("ADMIN"))));
    }

}
