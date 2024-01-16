package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.security.SecurityPersonDetails;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.UserService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditUserProfileControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_user_profile",
            "email", "upd_user_email",
            "password", "upd_user_pswd");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "email", "firstName", "lastName", "phoneNumber", "birthday");

    private final long personForUpdateId = ConstantsTestStudent.STUDENT_ID_VALID_1;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userServiceMock;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomEntityValidator<Person> customEntityValidator;

    Person loggedUserMock;
    Person personForUpdate;
    String validPassword;

    @BeforeEach
    void beforeEach() {
        loggedUserMock = ConstantsTestStudent.getTestStudent(personForUpdateId);
        loggedUserMock.setUserRoles(Set.of(UserRole.VIEWER));
        personForUpdate = ConstantsTestStudent.getTestStudent(personForUpdateId);
        personForUpdate.setUserRoles(Set.of(UserRole.VIEWER));
        validPassword = personForUpdate.getPassword();
        personForUpdate.setPassword(passwordEncoder.encode(personForUpdate.getPassword()));
    }

    @Test
    void WhenCalledAsUnauthenticatedUserWithGetRequest_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/profile/edit")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(userServiceMock);
    }

    @Test
    void userProfilePage_WhenCalledAndLoggedAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(userServiceMock.get(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/profile/edit")
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    void userProfilePage_WhenCalledAndLoggedAndPersonNotFound_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(userServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get("/profile/edit")
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Person not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    void userProfilePage_WhenCalledAndLoggedAndHappyPath_ShouldReturnEditProfileView()
            throws Exception {
        Mockito.when(userServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(personForUpdate));

        MvcResult resultController = mvc.perform(get("/profile/edit")
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "/profile/edit/{id}/profile",
            "/profile/edit/{id}/email",
            "/profile/edit{id}/password"
    })
    void WhenCalledAsUnauthenticatedUserWithPostRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {
        mvc.perform(post(parameterizedUrl, personForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "/profile/edit/{id}/profile",
            "/profile/edit/{id}/email",
            "/profile/edit/{id}/password"
    })
    void WhenCalledAndLoggedAndPersonNotFound_ShouldReturnErrorView(
            String parameterizedUrl) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.empty());

        mvc.perform(post(parameterizedUrl, personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", personForUpdate)
                .flashAttr("upd_user_email", personForUpdate)
                .flashAttr("upd_user_pswd", personForUpdate)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Person not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "/profile/edit/{id}/profile",
            "/profile/edit/{id}/email",
            "/profile/edit/{id}/password"
    })
    void WhenCalledAndLoggedAndPersonIdNotMatchesWithLoggedUserId_ShouldReturnErrorView(
            String parameterizedUrl) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId)).thenReturn(Optional.empty());

        assertNotEquals(personForUpdate.getId(), ConstantsTestStudent.STUDENT_ID_VALID_2);
        mvc.perform(post(parameterizedUrl, ConstantsTestStudent.STUDENT_ID_VALID_2)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", personForUpdate)
                .flashAttr("upd_user_email", personForUpdate)
                .flashAttr("upd_user_pswd", personForUpdate)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("User not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "/profile/edit/{id}/profile, err_upd_user_profile",
            "/profile/edit/{id}/email, err_upd_user_email",
            "/profile/edit/{id}/password, err_upd_user_pswd"
    })
    void WhenCalledAndLoggedAndPasswordConfirmationFail_ShouldReturnEditProfileViewWithErrorMessage(
            String parameterizedUrl, String parameterizedAttribute) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId)).thenReturn(Optional.of(personForUpdate));

        assertFalse(passwordEncoder.matches(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE,
                personForUpdate.getPassword()));
        MvcResult resultController = mvc.perform(post(parameterizedUrl, personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", personForUpdate)
                .flashAttr("upd_user_email", personForUpdate)
                .flashAttr("upd_user_pswd", personForUpdate)
                .param("curr_password", ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute,
                "Entered password is not valid");

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "/profile/edit/{id}/profile",
            "/profile/edit/{id}/email",
            "/profile/edit/{id}/password"
    })
    void WhenCalledAndLoggedAndThrownDataProcessingException_ShouldReturnErrorView(
            String parameterizedUrl) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post(parameterizedUrl, personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", personForUpdate)
                .flashAttr("upd_user_email", personForUpdate)
                .flashAttr("upd_user_pswd", personForUpdate)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({
            ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_1,
            ConstantsTestStudent.STUDENT_FIRSTNAME_INVALID_2
    })
    void changeProfileEntity_WhenCalledAndExistValidationViolations_ShouldReturnEditProfileViewWithMessage(
            String parameterizedFirstName) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setFirstName(parameterizedFirstName);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/profile", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttribute(resultController, "upd_user_profile", studentFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday");
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_profile", "firstName");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_profile, false, Failed to change User profile",
            "msg_upd_user_profile, true, User profile updated successfully"
    })
    void changeUserProfile_WhenCalledAndPersonProfileUpdatedOrNotUpdated_ShouldReturnEditProfileViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));
        Mockito.when(userServiceMock.updateProfile(Mockito.anyLong(), Mockito.any(Person.class)))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setFirstName(ConstantsTestStudent.STUDENT_FIRSTNAME_FOR_UPDATE);
        studentFromEditForm.setPhoneNumber(ConstantsTestStudent.STUDENT_PHONE_NUMBER_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/profile", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_profile", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttribute(resultController, "upd_user_profile", studentFromEditForm,
                "id", "firstName", "lastName", "phoneNumber", "birthday");
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_profile",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(userServiceMock, Mockito.times(1)).updateProfile(Mockito.anyLong(), Mockito.any(Person.class));
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({
            ConstantsTestStudent.STUDENT_EMAIL_INVALID_1,
            ConstantsTestStudent.STUDENT_EMAIL_INVALID_2
    })
    void changeUserEmail_WhenCalledAndExistValidationViolations_ShouldReturnEditProfileViewWithMessage(
            String parameterizedEmail) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setEmail(parameterizedEmail);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/email", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_email", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttribute(resultController, "upd_user_email", studentFromEditForm, "id", "email");
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_email", "email");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_email",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_email, false, Failed to change email",
            "msg_upd_user_email, true, Email changed successfully"
    })
    void changeUserEmail_WhenCalledAndPersonEmailUpdatedOrNotUpdated_ShouldReturnEditProfileViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));
        Mockito.when(userServiceMock.updateEmail(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setEmail(ConstantsTestStudent.STUDENT_EMAIL_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/email", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_email", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttribute(resultController, "upd_user_email", studentFromEditForm, "id", "email");
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_email",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(userServiceMock, Mockito.times(1)).updateEmail(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_1,
            ConstantsTestStudent.STUDENT_PASSWORD_INVALID_2
    })
    void changeUserPassword_WhenCalledAndExistValidationViolations_ShouldReturnEditProfileViewWithMessage(
            String parameterizedPassword) throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setPassword(parameterizedPassword);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/password", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_pswd", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_user_pswd", "password");
        checkForMessagePresenceInModelMap(resultController, "err_upd_user_pswd",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_user_pswd, false, Failed to change password",
            "msg_upd_user_pswd, true, Password changed successfully"
    })
    void changeUserPassword_WhenCalledAndPersonPasswordUpdatedOrNotUpdated_ShouldReturnEditProfileViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Mockito.when(userServiceMock.get(personForUpdateId))
                .thenReturn(Optional.of(personForUpdate));
        Mockito.when(userServiceMock.updatePassword(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(parameterizedServiceResult);

        Student studentFromEditForm = ConstantsTestStudent.getTestStudent(personForUpdateId);
        studentFromEditForm.setPassword(ConstantsTestStudent.STUDENT_PASSWORD_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/profile/edit/{id}/password", personForUpdateId)
                .with(user(new SecurityPersonDetails(loggedUserMock)))
                .flashAttr("upd_user_pswd", studentFromEditForm)
                .param("curr_password", validPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditProfileViewAttributes(resultController);
        checkPersonAttribute(resultController, "upd_user_pswd", studentFromEditForm, "id", "password");
        checkPersonAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_user_pswd",
                personForUpdate, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(userServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(userServiceMock, Mockito.times(1)).updatePassword(Mockito.anyLong(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditProfileViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Profile")
                .containsKey("upd_user_profile")
                .containsKey("upd_user_email")
                .containsKey("upd_user_pswd");
    }

    private void checkPersonAttribute(MvcResult resultController, String attribute,
            Person personForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Person.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(personForCheck);
    }

    private void checkPersonAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Person personForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Person.class)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fields)
                    .isEqualTo(personForCheck);
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

}
