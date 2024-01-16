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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;

@SpringBootTest
@AutoConfigureMockMvc
class EditStudentsControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_user_profile",
            "group", "upd_user_group");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "firstName", "lastName", "phoneNumber", "birthday",
            //
            "group");

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentService studentServiceMock;

    @MockBean
    GroupService groupServiceMock;

    @ParameterizedTest
    @ValueSource(strings = {
            "/students/edit/{id}"
    })
    void WhenCalledAsUnauthenticatedUserWithGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {
        Long entityId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get(parameterizedUrl, entityId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> methodAuthorities) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get("/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(methodAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;

        Mockito.when(studentServiceMock.get(studentForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndNotExistStudent_ShouldReturnViewWithErrorMessage() throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get("/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Student not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistStudent_ShouldReturnEditStudentView() throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Student studentForUpdate = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdate));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        MvcResult resultController = mvc.perform(get("/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "",
                studentForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/students/edit/{id}/group"
    })
    void WhenCalledAsUnauthenticatedUserWithPostRequest_ThenRedirectToLoginPage(String parameterizedUrl)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesWithForbiddenScenarioForDifferentUrls")
    void WhenCalledWithoutEditRoleWithPostRequest_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities, String parameterizedUrl) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/students/edit/{id}/group"
    })
    void WhenCalledWithEditRoleAndNotExistUpdatingStudent_ShouldReturnViewWithMessage(String parameterizedUrl)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.empty());

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Student not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void changeGroupStudent_WhenCalledWithEditRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(null);
        MvcResult resultController = mvc.perform(post("/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_group", studentFromEditForm, "id", "group");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_group",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_group", "group");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_group",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeGroupStudent_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateGroup(Mockito.anyLong(), Mockito.any(Group.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        mvc.perform(post("/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_group, false, Failed to change Group",
            "msg_upd_user_group, true, Group changed successfully"
    })
    void changeGroupStudent_WhenCalledWithEditRoleAndGroupNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateGroup(Mockito.anyLong(), Mockito.any(Group.class)))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(ConstantsTestStudent.STUDENT_GROUP_VALID);
        MvcResult resultController = mvc.perform(post("/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_group", studentFromEditForm, "id", "group");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_group",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromStudentForPostForm(@NotNull Student student) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("firstName", student.getFirstName());
        result.add("lastName", student.getLastName());
        result.add("phoneNumber", student.getPhoneNumber());
        result.add("birthday", student.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE));
        //
        if (Objects.nonNull(student.getGroup())) {
            result.add("group.id", String.valueOf(student.getGroup().getId()));
            result.add("group.name", student.getGroup().getName());
            result.add("group.capacity", String.valueOf(student.getGroup().getCapacity()));
        }
        return result;
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditStudentViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Student")
                .containsEntry("form_type", "student")
                //
                .containsKey("allGroups");
    }

    private void checkStudentAttributes(MvcResult resultController, String attribute,
            Student studentForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Student.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(studentForCheck);
    }

    private void checkStudentAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Student studentForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Student.class)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fields)
                    .isEqualTo(studentForCheck);
        }
    }

    private void checkForMessagePresenceInModelMap(MvcResult resultController, String attribute, String message) {
        assertThat(resultController.getModelAndView().getModelMap())
                .containsKey(attribute);
        assertThat((ArrayList<String>) resultController.getModelAndView().getModelMap().getAttribute(attribute))
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
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER"))));
    }

    private static Stream<Arguments> provideAuthoritiesWithForbiddenScenarioForDifferentUrls() {
        return Stream.of(
                Arguments.of(List.of(), "/students/edit/{id}/group"),
                //
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/students/edit/{id}/group"));
    }

}
