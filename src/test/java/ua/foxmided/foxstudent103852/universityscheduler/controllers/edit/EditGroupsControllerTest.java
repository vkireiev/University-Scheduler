package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
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
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditGroupsControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_group_profile",
            "courses", "upd_group_courses");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "name", "capacity",
            //
            "students", "courses");

    @Autowired
    MockMvc mvc;

    @MockBean
    GroupService groupServiceMock;

    @MockBean
    CourseService courseServiceMock;

    @Autowired
    CustomEntityValidator<Group> customEntityValidator;

    private final Long entityForUpdateId = ConstantsTestGroup.GROUP_ID_VALID_1;
    private Group entityForUpdateMock;
    private Group entityForEditForm;
    private Course courseForDelete;
    private Course courseForAdd;

    @BeforeEach
    void beforeEach() {
        Student student1 = ConstantsTestStudent.getTestStudent(51L);
        Course course4 = ConstantsTestCourse.getTestCourse(4L);
        Course course3 = ConstantsTestCourse.getTestCourse(3L);
        courseForDelete = ConstantsTestCourse.getTestCourse(1L);

        entityForUpdateMock = ConstantsTestGroup.getTestGroup(entityForUpdateId);
        entityForUpdateMock.addStudent(student1);
        entityForUpdateMock.addCourse(course4);
        entityForUpdateMock.addCourse(course3);
        entityForUpdateMock.addCourse(courseForDelete);

        entityForEditForm = ConstantsTestGroup.getTestGroup(entityForUpdateId);
        entityForEditForm.addStudent(student1);
        entityForEditForm.addCourse(course4);
        entityForEditForm.addCourse(course3);
        entityForEditForm.addCourse(courseForDelete);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/groups/edit/{id_group}",
            "/groups/edit/{id_group}/courses/delete/1",
            "/groups/edit/{id_group}/courses/add/1"
    })
    void WhenCalledAsUnauthenticatedUserViaGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void WhenCalledAsUnauthenticatedUserViaPostRequest_ThenRedirectToLoginPage() throws Exception {

        mvc.perform(post("/groups/edit/{id_group}", entityForUpdateId)
                .params(getMultiValueMapFromGroupForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaGetRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(get("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaPostRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(post("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .params(getMultiValueMapFromGroupForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/groups/edit/{id_group}",
            "/groups/edit/{id_group}/courses/delete/1",
            "/groups/edit/{id_group}/courses/add/1"
    })
    void WhenCalledWithEditRoleViaGetRequestAndNotExistUpdatingGroup_ShouldReturnViewWithErrorMessage(
            String parameterizedUrl) throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
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
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistUpdatingGroup_ShouldReturnEditGroupView() throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());

        MvcResult resultController = mvc.perform(get("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR"))))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditGroupViewAttributes(resultController);
        checkGroupAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", entityForEditForm,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkGroupAddCoursesList(resultController, entityForEditForm);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestGroup.GROUP_NAME_INVALID_1,
            ConstantsTestGroup.GROUP_NAME_INVALID_2
    })
    void changeEntity_WhenCalledWithEditRoleAndExistValidationViolations_ShouldReturnEditGroupViewWithMessage(
            String parameterizedName) throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());

        entityForEditForm.setName(parameterizedName);
        MvcResult resultController = mvc.perform(post("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromGroupForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditGroupViewAttributes(resultController);
        checkGroupAttributes(resultController, "upd_group_profile", entityForEditForm,
                "id", "name", "capacity");
        checkGroupAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_group_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_group_profile", "name");
        checkForMessagePresenceInModelMap(resultController, "err_upd_group_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        checkGroupAddCoursesList(resultController, entityForEditForm);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.update(Mockito.any(Group.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());

        entityForEditForm.setName(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE);
        mvc.perform(post("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromGroupForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).update(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndGroupUpdated_ShouldReturnEditGroupViewWithMessage()
            throws Exception {
        entityForEditForm.setName(ConstantsTestGroup.GROUP_NAME_FOR_UPDATE);

        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.update(Mockito.any(Group.class)))
                .thenReturn(entityForEditForm);
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());

        MvcResult resultController = mvc.perform(post("/groups/edit/{id_group}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromGroupForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditGroupViewAttributes(resultController);
        checkGroupAttributes(resultController, "upd_group_profile", entityForEditForm,
                "id", "name", "description");
        checkGroupAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_group_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "msg_upd_group_profile", "Group updated successfully");
        checkGroupAddCoursesList(resultController, entityForEditForm);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).update(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/groups/edit/{id_group}/courses/delete/{id_course}",
            "/groups/edit/{id_group}/courses/add/{id_course}"
    })
    void WhenCalledWithEditRoleAndNotExistCourse_ShouldReturnViewWithErrorMessage(String parameterizedUrl)
            throws Exception {
        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId, courseForDelete.getId())
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Course not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void editGroupDeleteCourse_WhenCalledAndCourseNotAssignedOnGroup_ShouldReturnEditGroupViewWithMessage()
            throws Exception {
        Course courseForDelete = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE);
        assertThat(entityForUpdateMock.getCourses()).doesNotContain(courseForDelete);

        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(courseForDelete));

        MvcResult resultController = mvc.perform(get("/groups/edit/{id_group}/courses/delete/{id_course}",
                entityForUpdateId, courseForDelete.getId())
                .header("Referer", "/groups/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_group_courses",
                "Trying to remove a Course which is not assigned to a Group");

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_group_courses, false, Failed to unassign Course for the Group",
            "msg_upd_group_courses, true, Course has been successfully unassigned for the Group"
    })
    void editGroupDeleteCourse_WhenCalledAndCourseNotDeletedOrDeleted_ShouldReturnEditGroupViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        assertThat(entityForUpdateMock.getCourses()).contains(courseForDelete);

        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(courseForDelete));

        MvcResult resultController = mvc.perform(get("/groups/edit/{id_group}/courses/delete/{id_course}",
                entityForUpdateId, courseForDelete.getId())
                .header("Referer", "/groups/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void editGroupAddCourse_WhenCalledAndCourseAlreadyAssignedOnGroup_ShouldReturnEditGroupViewWithMessage()
            throws Exception {
        courseForAdd = courseForDelete;
        assertThat(entityForUpdateMock.getCourses()).contains(courseForAdd);

        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(courseForAdd));

        MvcResult resultController = mvc.perform(get("/groups/edit/{id_group}/courses/add/{id_course}",
                entityForUpdateId, courseForAdd.getId())
                .header("Referer", "/groups/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_group_courses",
                "Trying to add a Course which is already assigned to a Group");

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_group_courses, false, Failed to assign Course to the Group",
            "msg_upd_group_courses, true, Course has been successfully assigned to the Group"
    })
    void editGroupAddCourse_WhenCalledAndCourseNotAddedOrAdded_ShouldReturnEditGroupViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        courseForAdd = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_2);
        assertThat(entityForUpdateMock.getCourses()).doesNotContain(courseForAdd);

        Mockito.when(groupServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);
        Mockito.when(courseServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(courseForAdd));

        MvcResult resultController = mvc.perform(get("/groups/edit/{id_group}/courses/add/{id_course}",
                entityForUpdateId, courseForAdd.getId())
                .header("Referer", "/groups/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromGroupForPostForm(@NotNull Group group) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("id", group.getId().toString());
        result.add("name", group.getName());
        result.add("capacity", group.getCapacity().toString());
        return result;
    }

    private void checkEditGroupViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Group");
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkGroupAttributes(MvcResult resultController, String attribute,
            Group entityForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Group.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(entityForCheck);
    }

    private void checkGroupAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Group entityForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Group.class)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fields)
                    .isEqualTo(entityForCheck);
        }
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

    private void checkGroupAddCoursesList(MvcResult resultController, Group entity) {
        List<Course> addCoursesList = ConstantsTestCourse.getAllTestCourses().stream()
                .filter(course -> !entity.getCourses().contains(course)).toList();
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("addCoursesList");
        assertThat(resultController.getModelAndView().getModel().get("addCoursesList"))
                .usingRecursiveComparison()
                .isEqualTo(addCoursesList);
    }

    private static Stream<Arguments> provideAuthoritiesForForbiddenScenario() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER"))));
    }

    private static Stream<Arguments> provideAuthoritiesForAccessibleScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("ADMIN"))));
    }

}
