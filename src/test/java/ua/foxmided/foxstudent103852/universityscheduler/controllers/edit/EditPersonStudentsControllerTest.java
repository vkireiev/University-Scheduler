package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditPersonStudentsControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_user_profile",
            "group", "upd_user_group",
            "roles", "upd_user_roles",
            "email", "upd_user_email",
            "locked", "upd_user_locked",
            "password", "upd_user_pswd");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "username", "email", "firstName", "lastName", "phoneNumber", "locked", "birthday", "userRoles",
            //
            "group");

    @Autowired
    MockMvc mvc;

    @MockBean
    StudentService studentServiceMock;

    @MockBean
    GroupService groupServiceMock;

    @MockBean
    PersonService personServiceMock;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomEntityValidator<Student> customEntityValidator;

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/students/edit/{id}",
            "/admin/students/edit/{id}/locked/true",
            "/admin/students/edit/{id}/locked/false"
    })
    void WhenCalledAsUnauthenticatedUserWithGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {
        Long entityId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get(parameterizedUrl, entityId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get("/admin/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;

        Mockito.when(studentServiceMock.get(studentForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndNotExistStudent_ShouldReturnViewWithErrorMessage() throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_NOT_EXIST;

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get("/admin/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
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

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndExistStudent_ShouldReturnEditStudentView() throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Student studentForUpdate = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdate));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        MvcResult resultController = mvc.perform(get("/admin/students/edit/{id}", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", studentForUpdate,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/students/edit/{id}/profile",
            "/admin/students/edit/{id}/group",
            "/admin/students/edit/{id}/roles",
            "/admin/students/edit/{id}/email",
            "/admin/students/edit/{id}/password"
    })
    void WhenCalledAsUnauthenticatedUserWithPostRequest_ThenRedirectToLoginPage(String parameterizedUrl)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesWithForbiddenScenarioForDifferentUrls")
    void WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities, String parameterizedUrl) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/students/edit/{id}/profile",
            "/admin/students/edit/{id}/group",
            "/admin/students/edit/{id}/roles",
            "/admin/students/edit/{id}/email",
            "/admin/students/edit/{id}/password"
    })
    void WhenCalledWithAdminRoleAndNotExistUpdatingStudent_ShouldReturnViewWithMessage(String parameterizedUrl)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.empty());

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        mvc.perform(post(parameterizedUrl, studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
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

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void changeProfileEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_1);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/profile", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_profile", studentFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday", "locked");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_profile", "firstName");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeProfileEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Student studentForUpdate = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdate));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateProfile(Mockito.anyLong(), Mockito.any(Student.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        studentForUpdate.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE);
        mvc.perform(post("/admin/students/edit/{id}/profile", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentForUpdate))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(), Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_profile, false, Failed to update Student profile",
            "msg_upd_user_profile, true, Student profile updated successfully"
    })
    void changeProfileEntity_WhenCalledWithAdminRoleAndProfileStudentNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateProfile(Mockito.anyLong(), Mockito.any(Student.class)))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/profile", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_profile", studentFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday", "locked");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(), Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.getUserRoles().clear();
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/roles", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_roles", studentFromEditForm, "id", "userRoles");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_roles",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_roles", "userRoles");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_roles",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateUserRoles(Mockito.anyLong(), Mockito.anySet()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.getUserRoles().add(UserRole.ADMIN);
        mvc.perform(post("/admin/students/edit/{id}/roles", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateUserRoles(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_roles, false, Failed to update access roles",
            "msg_upd_user_roles, true, Access roles updated successfully"
    })
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndUserRolesNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateUserRoles(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.getUserRoles().add(UserRole.ADMIN);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/roles", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_roles", studentFromEditForm, "id", "userRoles");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_roles",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateUserRoles(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeEmailEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setEmail(ConstantsTestStudent.STUDENT_EMAIL_INVALID_1);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/email", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_email", studentFromEditForm, "id", "email");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_email", "email");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_email",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeEmailEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setEmail(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE);
        mvc.perform(post("/admin/students/edit/{id}/email", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_email, false, Failed to change email",
            "msg_upd_user_email, true, Email changed successfully"
    })
    void changeEmailEntity_WhenCalledWithAdminRoleAndEmailNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setEmail(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/email", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_email", studentFromEditForm, "id", "email");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_1,
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2
    })
    void changePasswordEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage(
            String parameterizedPassword) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setPassword(parameterizedPassword);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/password", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_pswd", "password");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_pswd",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changePasswordEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE);
        mvc.perform(post("/admin/students/edit/{id}/password", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_pswd, false, Failed to change password",
            "msg_upd_user_pswd, true, Password changed successfully"
    })
    void changePasswordEntity_WhenCalledWithAdminRoleAndPasswordNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;
        Group group1 = ConstantsTestGroup.getTestGroup(1L);
        Student studentForUpdateMock = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentForUpdateMock.setGroup(group1);
        List<Group> allGroups = ConstantsTestGroup.getAllTestGroups();

        Mockito.when(studentServiceMock.get(studentForUpdateId)).thenReturn(Optional.of(studentForUpdateMock));
        Mockito.when(groupServiceMock.getAll()).thenReturn(allGroups);
        Mockito.when(studentServiceMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(studentForUpdateId);
        studentFromEditForm.setGroup(group1);
        studentFromEditForm.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/password", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeGroupStudent_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditStudentViewWithMessage()
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
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
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

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeGroupStudent_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
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
        mvc.perform(post("/admin/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
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
    void changeGroupStudent_WhenCalledWithAdminRoleAndGroupNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
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
        MvcResult resultController = mvc.perform(post("/admin/students/edit/{id}/group", studentForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(studentFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditStudentViewAttributes(resultController);
        checkStudentAttributes(resultController, "upd_user_group", studentFromEditForm, "id", "group");
        checkStudentAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_group",
                studentForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateGroup(Mockito.anyLong(), Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void changeLockedEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get("/admin/students/edit/{id}/locked/true", studentForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void changeLockedEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        Mockito.when(studentServiceMock.existsById(studentForUpdateId)).thenReturn(true);
        Mockito.when(studentServiceMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/students/edit/{id}/locked/{locked}", studentForUpdateId, true)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .existsById(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1))
                .updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_locked, false, Failed to unlock account, false",
            "err_upd_user_locked, false, Failed to lock account, true",
            "msg_upd_user_locked, true, Account unlocked successfully, false",
            "msg_upd_user_locked, true, Account locked successfully, true"
    })
    void changeLockedEntity_WhenCalledWithAdminRoleAndLockedNotUpdatedOrUpdated_ShouldReturnEditStudentViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage,
            boolean parameterizedIsLocked) throws Exception {
        long studentForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        Mockito.when(studentServiceMock.existsById(studentForUpdateId)).thenReturn(true);
        Mockito.when(studentServiceMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(
                get("/admin/students/edit/{id}/locked/{locked}", studentForUpdateId, parameterizedIsLocked)
                        .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/students/edit/" + studentForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(studentServiceMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromStudentForPostForm(@NotNull Student student) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("username", student.getUsername());
        result.add("email", student.getEmail());
        result.add("password", student.getPassword());
        result.add("firstName", student.getFirstName());
        result.add("lastName", student.getLastName());
        result.add("phoneNumber", student.getPhoneNumber());
        result.add("birthday", student.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE));
        result.add("locked", student.isLocked() ? "yes" : "no");
        //
        if (Objects.nonNull(student.getGroup())) {
            result.add("group.id", String.valueOf(student.getGroup().getId()));
            result.add("group.name", student.getGroup().getName());
            result.add("group.capacity", String.valueOf(student.getGroup().getCapacity()));
        }
        //
        if ((Objects.isNull(student.getUserRoles())) || (student.getUserRoles().isEmpty())) {
            result.add("userRoles", "");
        } else {
            for (UserRole role : student.getUserRoles()) {
                result.add("userRoles", role.name());
            }
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
                .containsKey("allRoles")
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

    private static Stream<Arguments> provideAuthoritiesWithForbiddenScenarioForDifferentUrls() {
        return Stream.of(
                Arguments.of(List.of(), "/admin/students/edit/{id}/profile"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/group"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/password"),
                //
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/profile"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/group"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/password"),
                //
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/profile"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/group"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/password"));
    }

}
