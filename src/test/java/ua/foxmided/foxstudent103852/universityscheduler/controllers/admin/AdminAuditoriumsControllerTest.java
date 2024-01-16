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
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAuditoriumsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AuditoriumService auditoriumServiceMock;

    @Autowired
    CustomEntityValidator<Auditorium> customEntityValidator;

    @Test
    void newEntityPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/admin/auditoriums/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> methodAuthorities) throws Exception {
        mvc.perform(get("/admin/auditoriums/new")
                .with(user("user").authorities(methodAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @Test
    void newEntityPage_WhenCalledWithAdminRole_ShouldReturnNewAuditoriumView() throws Exception {
        MvcResult resultController = mvc.perform(get("/admin/auditoriums/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/auditoriums/auditorium_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Auditorium")
                .containsKey("new_auditorium");
        assertThat(resultController.getModelAndView().getModel().get("new_auditorium"))
                .isInstanceOf(Auditorium.class);

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @Test
    void newEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(post("/admin/auditoriums/new")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(post("/admin/auditoriums/new")
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            Constants.EMPTY_STRING,
            ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_1,
            ConstantsTestAuditorium.AUDITORIUM_NUMBER_INVALID_2
    })
    void newEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnNewAuditoriumViewWithMessage(
            String parameterizedNumber) throws Exception {
        Auditorium newEntity = ConstantsTestAuditorium.newValidAuditorium();

        newEntity.setNumber(parameterizedNumber);
        MvcResult resultController = mvc.perform(post("/admin/auditoriums/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromAuditoriumForPostForm(newEntity))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/auditoriums/auditorium_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Auditorium")
                .containsKey("new_auditorium")
                .containsKey("err_new_auditorium");
        checkForMessagePresenceInBindingResult(resultController, "new_auditorium", "number");
        checkForMessagePresenceInModelMap(resultController, "err_new_auditorium",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_auditorium"))
                .isInstanceOf(Auditorium.class);

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Auditorium newEntity = ConstantsTestAuditorium.newValidAuditorium();

        Mockito.when(auditoriumServiceMock.add(Mockito.any(Auditorium.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post("/admin/auditoriums/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromAuditoriumForPostForm(newEntity))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).add(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownEntityDataIntegrityViolationException_ShouldReturnNewAuditoriumViewWithErrorMessage()
            throws Exception {
        Auditorium newEntity = ConstantsTestAuditorium.newValidAuditorium();

        Mockito.when(auditoriumServiceMock.add(Mockito.any(Auditorium.class)))
                .thenThrow(new EntityAddDataIntegrityViolationException(
                        ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE));

        MvcResult resultController = mvc.perform(post("/admin/auditoriums/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromAuditoriumForPostForm(newEntity))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/auditoriums/auditorium_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Auditorium")
                .containsKey("new_auditorium")
                .containsKey("gErrors");
        checkForMessagePresenceInModelMap(resultController, "gErrors",
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_auditorium"))
                .isInstanceOf(Auditorium.class);

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).add(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndHappyPath_ShouldAddAuditoriumAndReturnAuditoriumsView()
            throws Exception {
        Auditorium newEntity = ConstantsTestAuditorium.newValidAuditorium();

        MvcResult resultController = mvc.perform(post("/admin/auditoriums/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromAuditoriumForPostForm(newEntity))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditoriums"))
                .andExpect(flash().attributeExists("gMessages"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gMessages", "Auditorium added successfully");

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).add(Mockito.any(Auditorium.class));
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        long entityForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

        mvc.perform(get("/admin/auditoriums/delete/{id}", entityForDeleteId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void deleteEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        long entityForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

        mvc.perform(get("/admin/auditoriums/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(auditoriumServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        long entityForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

        Mockito.when(auditoriumServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/auditoriums/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndExistAuditoriumAndAssignedLectures_ShouldReturnRefererViewWithMessage()
            throws Exception {
        long entityForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

        Mockito.when(auditoriumServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new EntityDataIntegrityViolationException(
                        "Before deleting Auditorium, reassign Lectures to another Auditorium(s)"));

        MvcResult resultController = mvc.perform(get("/admin/auditoriums/delete/{id}", entityForDeleteId)
                .header("Referer", "/auditoriums/")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditoriums/"))
                .andExpect(flash().attributeExists("gErrors"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gErrors",
                "Before deleting Auditorium, reassign Lectures to another Auditorium(s)");

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "gErrors, false, Failed to delete Auditorium",
            "gMessages, true, Auditorium deleted successfully"
    })
    void deleteEntity_WhenCalledWithAdminRoleAndAuditoriumNotDeletedOrDeleted_ShouldReturnAuditoriumsViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        long entityForDeleteId = ConstantsTestAuditorium.AUDITORIUM_ID_VALID_1;

        Mockito.when(auditoriumServiceMock.deleteById(entityForDeleteId))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/auditoriums/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditoriums"))
                .andExpect(flash().attributeExists(parameterizedAttribute))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromAuditoriumForPostForm(@NotNull Auditorium entity) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("number", entity.getNumber());
        result.add("capacity", entity.getCapacity().toString());
        result.add("available", entity.isAvailable() ? "yes" : "no");
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
