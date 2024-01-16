package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.service.AuditoriumServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.service.LectureServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;

@SpringBootTest
@AutoConfigureMockMvc
class AuditoriumsControllerTest {
    private static final int PAGE_LIMIT = 3;

    @Autowired
    MockMvc mvc;

    @MockBean
    AuditoriumServiceImpl auditoriumServiceMock;

    @MockBean
    LectureServiceImpl lectureServiceMock;

    private final Long entityForViewId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

    @Test
    void auditoriumsPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/auditoriums"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void auditoriumsPage_WhenCalledWithForbiddenScenario_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/auditoriums")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumsPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(auditoriumServiceMock.getAll())
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/auditoriums")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumsPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnAuditoriumsView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        List<Auditorium> resultMock = ConstantsTestAuditorium.getAllTestAuditoriums();

        Mockito.when(auditoriumServiceMock.getAll()).thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/auditoriums")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditoriums"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Auditoriums");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("auditoriums");
        assertThat(resultController.getModelAndView().getModel().get("auditoriums"))
                .usingRecursiveComparison()
                .isEqualTo(resultMock);
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("entitiesCount", resultMock.size());

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    @WithMockUser(authorities = { "EMPLOYEE" })
    void auditoriumsPage_WhenCalledAsEmployeeWithoutAnyRoles_ShouldReturnAccessDeniedView() throws Exception {
        mvc.perform(get("/auditoriums"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @Test
    void auditoriumViewPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/auditoriums/view/{id}", entityForViewId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void auditoriumViewPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/auditoriums/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumViewPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(auditoriumServiceMock.get(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/auditoriums/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumViewPage_WhenCalledWithAccessibleScenarioAndNotExistAuditoriumWithSuchId_ShouldReturnViewWithErrorMessage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(auditoriumServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get("/auditoriums/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Auditorium not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumViewPage_WhenCalledWithExistsAuditoriumWithoutUpcomingLectures_ShouldReturnAuditoriumViewWithoutUpcomingLectures(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Auditorium entityForViewMock = ConstantsTestAuditorium.getTestAuditorium(entityForViewId);

        Mockito.when(auditoriumServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(entityForViewMock));
        Mockito.when(lectureServiceMock.findPageByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class),
                Mockito.any(LocalDate.class),
                Mockito.any(Pageable.class)))
                .thenReturn(List.of());

        MvcResult resultController = mvc.perform(get("/auditoriums/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_view"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Auditorium view")
                .containsKey("auditorium_view")
                .containsKey("upcomingLectures");
        assertThat(resultController.getModelAndView().getModel().get("auditorium_view"))
                .usingRecursiveComparison()
                .isEqualTo(entityForViewMock);
        assertThat(((List<Lecture>) resultController.getModelAndView().getModel().get("upcomingLectures")))
                .isEmpty();

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void auditoriumViewPage_WhenCalledWithExistsAuditoriumAndUpcomingLectures_ShouldReturnAuditoriumViewWithUpcomingLectures(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Auditorium entityForViewMock = ConstantsTestAuditorium.getTestAuditorium(entityForViewId);

        LocalDate searchLectureDate = ConstantsTestLecture.LECTURE_FIND_BY_LECTURE_DATE_1_VALID;
        List<Lecture> lecturesMockito = ConstantsTestLecture.getAllTestLectures().stream()
                .filter(lecture -> lecture.getAuditorium().equals(entityForViewMock)
                        && (lecture.getLectureDate().isAfter(searchLectureDate)
                                || lecture.getLectureDate().isEqual(searchLectureDate)))
                .sorted(Comparator.comparing(Lecture::getLectureDate))
                .limit(PAGE_LIMIT)
                .collect(Collectors.toList());
        assertThat(lecturesMockito).isNotEmpty();

        Mockito.when(auditoriumServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(entityForViewMock));
        Mockito.when(lectureServiceMock.findPageByAuditoriumAndLectureDateGreaterThanEqual(
                Mockito.any(Auditorium.class),
                Mockito.any(LocalDate.class),
                Mockito.any(Pageable.class)))
                .thenReturn(lecturesMockito);

        MvcResult resultController = mvc.perform(get("/auditoriums/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_view"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Auditorium view")
                .containsKey("auditorium_view")
                .containsKey("upcomingLectures");
        assertThat(resultController.getModelAndView().getModel().get("auditorium_view"))
                .usingRecursiveComparison()
                .isEqualTo(entityForViewMock);
        List<Lecture> lecturesController = (List<Lecture>) resultController.getModelAndView().getModel()
                .get("upcomingLectures");
        lecturesMockito.sort(Comparator.comparing(Lecture::getId));
        lecturesController.sort(Comparator.comparing(Lecture::getId));
        assertThat(lecturesController)
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(lecturesMockito);

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(lectureServiceMock, Mockito.times(1))
                .findPageByAuditoriumAndLectureDateGreaterThanEqual(
                        Mockito.any(Auditorium.class),
                        Mockito.any(LocalDate.class),
                        Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
    }

    private static Stream<Arguments> provideAuthoritiesForAccessDeniedScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("STUDENT"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("EMPLOYEE"))));
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
