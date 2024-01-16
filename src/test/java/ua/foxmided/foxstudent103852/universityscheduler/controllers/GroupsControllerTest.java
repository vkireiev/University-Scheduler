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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.GroupServiceImpl;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;

@SpringBootTest
@AutoConfigureMockMvc
class GroupsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GroupServiceImpl groupServiceMock;

    private final Long entityForViewId = ConstantsTestGroup.GROUP_ID_VALID_1;

    @Test
    void groupsPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/groups"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void groupsPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/groups")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void groupsPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(groupServiceMock.getAll())
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/groups")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void groupsPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnGroupsView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        List<Group> resultMock = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(groupServiceMock.getAll()).thenReturn(resultMock);

        MvcResult resultController = mvc.perform(get("/groups")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/groups"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Groups");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("groups");
        assertThat(resultController.getModelAndView().getModel().get("groups"))
                .usingRecursiveComparison()
                .isEqualTo(resultMock);
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("entitiesCount", resultMock.size());

        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void groupViewPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/groups/view/{id}", entityForViewId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessDeniedScenario")
    void groupViewPage_WhenCalledWithoutAnyAccessRoles_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/groups/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void groupViewPage_WhenCalledWithAccessibleScenarioAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/groups/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void groupViewPage_WhenCalledWithAccessibleScenarioAndNotExistGroupWithSuchId_ShouldReturnViewWithErrorMessage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(groupServiceMock.get(Mockito.anyLong())).thenReturn(Optional.empty());

        mvc.perform(get("/groups/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Group not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void groupViewPage_WhenCalledWithAccessibleScenarioAndHappyPath_ShouldReturnGroupView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Group entityForViewMock = ConstantsTestGroup.getTestGroup(entityForViewId);

        Mockito.when(groupServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(entityForViewMock));

        MvcResult resultController = mvc.perform(get("/groups/view/{id}", entityForViewId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group_view"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler");
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("moduleName", "Group view");
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("group_view");
        assertThat(resultController.getModelAndView().getModel().get("group_view"))
                .usingRecursiveComparison()
                .isEqualTo(entityForViewMock);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
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
