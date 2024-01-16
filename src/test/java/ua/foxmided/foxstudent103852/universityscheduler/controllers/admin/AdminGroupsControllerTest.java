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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class AdminGroupsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GroupService groupServiceMock;

    @Autowired
    CustomEntityValidator<Group> customEntityValidator;

    @Test
    void newEntityPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/admin/groups/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/admin/groups/new")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntityPage_WhenCalledWithAdminRole_ShouldReturnNewGroupView() throws Exception {
        MvcResult resultController = mvc.perform(get("/admin/groups/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/groups/group_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Group")
                .containsKey("new_group");
        assertThat(resultController.getModelAndView().getModel().get("new_group"))
                .isInstanceOf(Group.class);

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(post("/admin/groups/new")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(post("/admin/groups/new")
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestGroup.GROUP_NAME_INVALID_1,
            ConstantsTestGroup.GROUP_NAME_INVALID_2
    })
    void newEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnNewGroupViewWithMessage(
            String parameterizedName) throws Exception {
        Group newGroup = ConstantsTestGroup.newValidGroup();
        newGroup.setName(parameterizedName);

        MvcResult resultController = mvc.perform(post("/admin/groups/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromGroupForPostForm(newGroup))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/groups/group_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Group")
                .containsKey("new_group")
                .containsKey("err_new_group");
        checkForMessagePresenceInBindingResult(resultController, "new_group", "name");
        checkForMessagePresenceInModelMap(resultController, "err_new_group", Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_group"))
                .isInstanceOf(Group.class);

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Group newGroup = ConstantsTestGroup.newValidGroup();

        Mockito.when(groupServiceMock.add(Mockito.any(Group.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post("/admin/groups/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromGroupForPostForm(newGroup))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).add(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndHappyPath_ShouldAddGroupAndReturnGroupsView() throws Exception {
        Group newGroup = ConstantsTestGroup.newValidGroup();

        MvcResult resultController = mvc.perform(post("/admin/groups/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromGroupForPostForm(newGroup))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attributeExists("gMessages"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gMessages", "Group added successfully");

        Mockito.verify(groupServiceMock, Mockito.times(1)).add(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        Long entityForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;

        mvc.perform(get("/admin/groups/delete/{id}", entityForDeleteId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void deleteEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Long entityForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;

        mvc.perform(get("/admin/groups/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Long entityForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;

        Mockito.when(groupServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/groups/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndExistGroupAndAssignedCourseOrStudent_ShouldReturnRefererViewWithMessage()
            throws Exception {
        Long entityForDeleteId = ConstantsTestGroup.GROUP_ID_VALID_1;

        Mockito.when(groupServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new EntityDataIntegrityViolationException(
                        "Before deleting Group, remove all Students/Courses from the Group"));

        MvcResult resultController = mvc.perform(get("/admin/groups/delete/{id}", entityForDeleteId)
                .header("Referer", "/groups/edit/" + entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + entityForDeleteId))
                .andExpect(flash().attributeExists("gErrors"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gErrors",
                "Before deleting Group, remove all Students/Courses from the Group");

        Mockito.verify(groupServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "gErrors, false, Failed to delete Group",
            "gMessages, true, Group deleted successfully"
    })
    void deleteEntity_WhenCalledWithAdminRoleAndCourseNotDeletedOrDeleted_ShouldReturnGroupsViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Long entityForDeleteId = ConstantsTestGroup.GROUP_ID_FOR_DELETE;

        Mockito.when(groupServiceMock.deleteById(Mockito.anyLong()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/groups/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andExpect(flash().attributeExists(parameterizedAttribute))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(groupServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromGroupForPostForm(@NotNull Group group) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("name", group.getName());
        result.add("capacity", group.getCapacity().toString());
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
