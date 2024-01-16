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
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditAuditoriumsControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_auditorium_profile");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "number", "capacity", "available");

    @Autowired
    MockMvc mvc;

    @MockBean
    AuditoriumService auditoriumServiceMock;

    @Autowired
    CustomEntityValidator<Auditorium> customEntityValidator;

    private final Long entityForUpdateId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;
    private Auditorium entityForUpdateMock;
    private Auditorium entityForEditForm;

    @BeforeEach
    void beforeEach() {
        entityForUpdateMock = ConstantsTestAuditorium.getTestAuditorium(entityForUpdateId);
        entityForEditForm = ConstantsTestAuditorium.getTestAuditorium(entityForUpdateId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/auditoriums/edit/{id}"
    })
    void WhenCalledAsUnauthenticatedUserViaGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @Test
    void WhenCalledAsUnauthenticatedUserViaPostRequest_ThenRedirectToLoginPage() throws Exception {

        mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaGetRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(get("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaPostRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/auditoriums/edit/{id}"
    })
    void WhenCalledWithEditRoleViaGetRequestAndNotExistUpdatingAuditorium_ShouldReturnViewWithErrorMessage(
            String parameterizedUrl) throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
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
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistUpdatingAuditorium_ShouldReturnEditAuditoriumView()
            throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));

        MvcResult resultController = mvc.perform(get("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR"))))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditAuditoriumViewAttributes(resultController);
        checkAuditoriumAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", entityForEditForm,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            Constants.EMPTY_STRING,
            ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1,
            ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_2
    })
    void changeEntity_WhenCalledWithEditRoleAndExistValidationViolations_ShouldReturnEditAuditoriumViewWithMessage(
            String parameterizedNumber) throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));

        entityForEditForm.setNumber(parameterizedNumber);
        MvcResult resultController = mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditAuditoriumViewAttributes(resultController);
        checkAuditoriumAttributes(resultController, "upd_auditorium_profile", entityForEditForm,
                "id", "number", "capacity", "available");
        checkAuditoriumAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_auditorium_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_auditorium_profile", "number");
        checkForMessagePresenceInModelMap(resultController, "err_upd_auditorium_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndNotExistUpdatingAuditorium_ShouldReturnViewWithErrorMessage()
            throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
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
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.update(Mockito.any(Auditorium.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        entityForEditForm.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_FOR_UPDATE);
        mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).update(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownEntityUpdateDataIntegrityViolationException_ShouldReturnEditAuditoriumViewWithErrorMessage()
            throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.update(Mockito.any(Auditorium.class)))
                .thenThrow(new EntityUpdateDataIntegrityViolationException(
                        ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE));

        entityForEditForm.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditAuditoriumViewAttributes(resultController);
        checkAuditoriumAttributes(resultController, "upd_auditorium_profile", entityForEditForm,
                "id", "number", "capacity", "available");
        checkAuditoriumAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_auditorium_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "err_upd_auditorium_profile",
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).update(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndAuditoriumUpdated_ShouldReturnEditAuditoriumViewWithMessage()
            throws Exception {
        Mockito.when(auditoriumServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.update(Mockito.any(Auditorium.class)))
                .thenReturn(entityForEditForm);

        entityForEditForm.setNumber(ConstantsTestAuditorium.AUDITORIUM_NUMBER_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/auditoriums/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromAuditoriumForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auditoriums/auditorium_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditAuditoriumViewAttributes(resultController);
        checkAuditoriumAttributes(resultController, "upd_auditorium_profile", entityForEditForm,
                "id", "number", "capacity", "available");
        checkAuditoriumAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_auditorium_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "msg_upd_auditorium_profile",
                "Auditorium updated successfully");

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).update(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromAuditoriumForPostForm(@NotNull Auditorium entity) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("id", entity.getId().toString());
        result.add("number", entity.getNumber());
        result.add("capacity", entity.getCapacity().toString());
        result.add("available", entity.isAvailable() ? "yes" : "no");
        return result;
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditAuditoriumViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Auditorium");
    }

    private void checkAuditoriumAttributes(MvcResult resultController, String attribute,
            Auditorium entityForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Auditorium.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(entityForCheck);
    }

    private void checkAuditoriumAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Auditorium entityForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Auditorium.class)
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

    private static Stream<Arguments> provideAuthoritiesForAccessibleScenario() {
        return Stream.of(
                Arguments.of(List.of(new SimpleGrantedAuthority("EDITOR"))),
                Arguments.of(List.of(new SimpleGrantedAuthority("ADMIN"))));
    }

}
