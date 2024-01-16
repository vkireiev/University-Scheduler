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
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditPersonEmployeesControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_user_profile",
            "roles", "upd_user_roles",
            "email", "upd_user_email",
            "locked", "upd_user_locked",
            "password", "upd_user_pswd");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "username", "email", "firstName", "lastName", "phoneNumber", "locked", "birthday", "userRoles",
            //
            "userRoles");

    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeService employeeServiceMock;

    @MockBean
    PersonService personServiceMock;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomEntityValidator<Employee> customEntityValidator;

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/employees/edit/{id}",
            "/admin/employees/edit/{id}/locked/true",
            "/admin/employees/edit/{id}/locked/false"
    })
    void WhenCalledAsUnauthenticatedUserWithGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {
        Long entityId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        mvc.perform(get(parameterizedUrl, entityId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        mvc.perform(get("/admin/employees/edit/{id}", employeeForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST;

        Mockito.when(employeeServiceMock.get(employeeForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/employees/edit/{id}", employeeForUpdateId)
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
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndNotExistEmployee_ShouldReturnViewWithErrorMessage()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_NOT_EXIST;

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get("/admin/employees/edit/{id}", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Employee not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithAdminRoleAndExistEmployee_ShouldReturnEditEmployeeView() throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdate = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdate));

        MvcResult resultController = mvc.perform(get("/admin/employees/edit/{id}", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "",
                employeeForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/employees/edit/{id}/profile",
            "/admin/employees/edit/{id}/roles",
            "/admin/employees/edit/{id}/email",
            "/admin/employees/edit/{id}/password"
    })
    void WhenCalledAsUnauthenticatedUserWithPostRequest_ThenRedirectToLoginPage(String parameterizedUrl)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, employeeForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesWithForbiddenScenarioForDifferentUrls")
    void WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities, String parameterizedUrl) throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        mvc.perform(post(parameterizedUrl, employeeForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin/employees/edit/{id}/profile",
            "/admin/employees/edit/{id}/roles",
            "/admin/employees/edit/{id}/email",
            "/admin/employees/edit/{id}/password"
    })
    void WhenCalledWithAdminRoleAndNotExistUpdatingEmployee_ShouldReturnViewWithMessage(String parameterizedUrl)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.empty());

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        mvc.perform(post(parameterizedUrl, employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Employee not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeProfileEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditEmployeeViewWithMessage()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_INVALID_1);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/profile", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_profile", employeeFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday", "locked", "employeeType");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_profile", "firstName");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeProfileEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdate = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdate));
        Mockito.when(employeeServiceMock.updateProfile(Mockito.anyLong(), Mockito.any(Employee.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        employeeForUpdate.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE);
        mvc.perform(post("/admin/employees/edit/{id}/profile", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeForUpdate))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(), Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_profile, false, Failed to update Employee profile",
            "msg_upd_user_profile, true, Employee profile updated successfully"
    })
    void changeProfileEntity_WhenCalledWithAdminRoleAndProfileStudentNotUpdatedOrUpdated_ShouldReturnEditEmployeeViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updateProfile(Mockito.anyLong(), Mockito.any(Employee.class)))
                .thenReturn(parameterizedServiceResult);

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setFirstName(ConstantsTestEmployee.EMPLOYEE_FIRSTNAME_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/profile", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_profile", employeeFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday", "locked", "employeeType");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateProfile(Mockito.anyLong(), Mockito.any(Employee.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditEmployeeViewWithMessage()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.getUserRoles().clear();
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/roles", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_roles", employeeFromEditForm, "id", "userRoles");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_roles",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_roles", "userRoles");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_roles",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updateUserRoles(Mockito.anyLong(), Mockito.anySet()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.getUserRoles().add(UserRole.ADMIN);
        mvc.perform(post("/admin/employees/edit/{id}/roles", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateUserRoles(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_roles, false, Failed to update access roles",
            "msg_upd_user_roles, true, Access roles updated successfully"
    })
    void changeUserRolesEntity_WhenCalledWithAdminRoleAndUserRolesNotUpdatedOrUpdated_ShouldReturnEditEmployeeViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updateUserRoles(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.getUserRoles().add(UserRole.ADMIN);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/roles", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_roles", employeeFromEditForm, "id", "userRoles");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_roles",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateUserRoles(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeEmailEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditEmployeeViewWithMessage()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_INVALID_1);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/email", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_email", employeeFromEditForm, "id", "email");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_email", "email");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_email",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeEmailEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.getUserRoles().add(UserRole.ADMIN);
        mvc.perform(post("/admin/employees/edit/{id}/email", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_email, false, Failed to change email",
            "msg_upd_user_email, true, Email changed successfully"
    })
    void changeEmailEntity_WhenCalledWithAdminRoleAndEmailNotUpdatedOrUpdated_ShouldReturnEditEmployeeViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setEmail(ConstantsTestEmployee.EMPLOYEE_EMAIL_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/email", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributes(resultController, "upd_user_email", employeeFromEditForm, "id", "email");
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_1,
            ConstantsTestEmployee.EMPLOYEE_PASSWORD_INVALID_2
    })
    void changePasswordEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnEditEmployeeViewWithMessage(
            String parameterizedPassword) throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setPassword(parameterizedPassword);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/password", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_pswd", "password");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_pswd",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changePasswordEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE);
        mvc.perform(post("/admin/employees/edit/{id}/password", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_pswd, false, Failed to change password",
            "msg_upd_user_pswd, true, Password changed successfully"
    })
    void changePasswordEntity_WhenCalledWithAdminRoleAndPasswordNotUpdatedOrUpdated_ShouldReturnEditEmployeeViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
        Employee employeeForUpdateMock = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);

        Mockito.when(employeeServiceMock.get(employeeForUpdateId)).thenReturn(Optional.of(employeeForUpdateMock));
        Mockito.when(employeeServiceMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Employee employeeFromEditForm = ConstantsTestEmployee.getTestEmployee(employeeForUpdateId);
        employeeFromEditForm.setPassword(ConstantsTestEmployee.EMPLOYEE_PASSWORD_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/admin/employees/edit/{id}/password", employeeForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromEmployeeForPostForm(employeeFromEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditEmployeeViewAttributes(resultController);
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                employeeForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1))
                .updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void changeLockedEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        mvc.perform(get("/admin/employees/edit/{id}/locked/true", employeeForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
    }

    @Test
    void changeLockedEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        Mockito.when(employeeServiceMock.existsById(employeeForUpdateId)).thenReturn(true);
        Mockito.when(employeeServiceMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/employees/edit/{id}/locked/{locked}", employeeForUpdateId, true)
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
        Mockito.verify(employeeServiceMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
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
        long employeeForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;

        Mockito.when(employeeServiceMock.existsById(employeeForUpdateId)).thenReturn(true);
        Mockito.when(employeeServiceMock.updateLocked(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/employees/edit/{id}/locked/{locked}",
                employeeForUpdateId, parameterizedIsLocked)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees/edit/" + employeeForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateLocked(Mockito.anyLong(), Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromEmployeeForPostForm(@NotNull Employee employee) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("username", employee.getUsername());
        result.add("email", employee.getEmail());
        result.add("password", employee.getPassword());
        result.add("firstName", employee.getFirstName());
        result.add("lastName", employee.getLastName());
        result.add("phoneNumber", employee.getPhoneNumber());
        result.add("birthday", employee.getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE));
        result.add("locked", employee.isLocked() ? "yes" : "no");
        //
        result.add("employeeType", (employee.getEmployeeType() == EmployeeType.EMPLOYEE) ? EmployeeType.EMPLOYEE.name()
                : EmployeeType.LECTURER.name());
        //
        if ((Objects.isNull(employee.getUserRoles())) || (employee.getUserRoles().isEmpty())) {
            result.add("userRoles", "");
        } else {
            for (UserRole role : employee.getUserRoles()) {
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

    private void checkEditEmployeeViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Employee")
                .containsEntry("form_type", "employee")
                .containsKey("allRoles")
                //
                .containsKey("allEmployeeType");
    }

    private void checkEmployeeAttributes(MvcResult resultController, String attribute,
            Employee employeeForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Employee.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(employeeForCheck);
    }

    private void checkEmployeeAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Employee employeeForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Employee.class)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fields)
                    .isEqualTo(employeeForCheck);
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
                Arguments.of(List.of(), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(), "/admin/students/edit/{id}/password"),
                //
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/profile"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(new SimpleGrantedAuthority("VIEWER")), "/admin/students/edit/{id}/password"),
                //
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/profile"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/roles"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/email"),
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR")), "/admin/students/edit/{id}/password"));
    }

}
