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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestStudent;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class AdminPersonStudentsControllerTest {

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

    @Test
    void newEntityPage_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(get("/admin/students/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntityPage_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(get("/admin/students/new")
                .with(user("user").authorities(parameterizedAuthorities)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntityPage_WhenCalledWithAdminRole_ShouldReturnNewStudentView() throws Exception {
        MvcResult resultController = mvc.perform(get("/admin/students/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Student")
                .containsEntry("form_type", "student")
                .containsKey("new_user");
        assertThat(resultController.getModelAndView().getModel().get("new_user"))
                .isInstanceOf(Student.class);

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        mvc.perform(post("/admin/students/new")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void newEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        mvc.perform(post("/admin/students/new")
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndExistStudentWithSuchUsername_ShouldReturnNewStudentViewWithMessage()
            throws Exception {
        Student newStudent = ConstantsTestStudent.newValidStudent();

        Mockito.when(personServiceMock.existsByUsername(newStudent.getUsername())).thenReturn(true);

        MvcResult resultController = mvc.perform(post("/admin/students/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(newStudent))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Student")
                .containsEntry("form_type", "student")
                .containsKey("new_user")
                .containsKey("err_new_user");
        checkForMessagePresenceInModelMap(resultController, "err_new_user", "User with such username already exists");
        assertThat(resultController.getModelAndView().getModel().get("new_user"))
                .isInstanceOf(Student.class);

        Mockito.verify(personServiceMock, Mockito.times(1)).existsByUsername(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(personServiceMock);
        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndExistValidationViolations_ShouldReturnNewStudentViewWithMessage()
            throws Exception {
        Student newStudent = ConstantsTestStudent.newValidStudent();
        newStudent.setUsername(ConstantsTestStudent.STUDENT_USERNAME_INVALID_1);

        Mockito.when(personServiceMock.existsByUsername(newStudent.getUsername())).thenReturn(false);

        MvcResult resultController = mvc.perform(post("/admin/students/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(newStudent))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/user_new"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Add new Student")
                .containsEntry("form_type", "student")
                .containsKey("new_user")
                .containsKey("err_new_user");
        checkForMessagePresenceInBindingResult(resultController, "new_user", "username");
        checkForMessagePresenceInModelMap(resultController, "err_new_user", Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        assertThat(resultController.getModelAndView().getModel().get("new_user"))
                .isInstanceOf(Student.class);

        Mockito.verify(personServiceMock, Mockito.times(1)).existsByUsername(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(personServiceMock);
        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Student newStudent = ConstantsTestStudent.newValidStudent();

        Mockito.when(personServiceMock.existsByUsername(newStudent.getUsername())).thenReturn(false);
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(post("/admin/students/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(newStudent))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(personServiceMock, Mockito.times(1)).existsByUsername(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).add(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void newEntity_WhenCalledWithAdminRoleAndHappyPath_ShouldAddStudentAndReturnStudentsView() throws Exception {
        Student newStudent = ConstantsTestStudent.newValidStudent();

        Mockito.when(personServiceMock.existsByUsername(newStudent.getUsername())).thenReturn(false);

        MvcResult resultController = mvc.perform(post("/admin/students/new")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .params(getMultiValueMapFromStudentForPostForm(newStudent))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("gMessages"))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "gMessages", "Student added successfully");

        Mockito.verify(personServiceMock, Mockito.times(1)).existsByUsername(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).add(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledAsUnauthenticatedUser_ThenRedirectToLoginPage() throws Exception {
        Long entityId = ConstantsTestStudent.STUDENT_ID_VALID_1;

        mvc.perform(get("/admin/students/delete/{id}", entityId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void deleteEntity_WhenCalledWithoutAdminRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Long entityForDeleteId = ConstantsTestStudent.STUDENT_ID_FOR_DELETE;

        mvc.perform(get("/admin/students/delete/{id}", entityForDeleteId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/forbidden"));

        Mockito.verifyNoInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void deleteEntity_WhenCalledWithAdminRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Long entityForDeleteId = ConstantsTestStudent.STUDENT_ID_FOR_DELETE;

        Mockito.when(studentServiceMock.deleteById(Mockito.anyLong()))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/admin/students/delete/{id}", entityForDeleteId)
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
        Mockito.verify(studentServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "gErrors, false, Failed to delete Student",
            "gMessages, true, Student deleted successfully"
    })
    void deleteEntity_WhenCalledWithAdminRoleAndSEntityNotDeletedOrDeleted_ShouldReturnEntitiesViewWithErrorMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Long entityForDeleteId = ConstantsTestStudent.STUDENT_ID_FOR_DELETE;

        Mockito.when(studentServiceMock.deleteById(entityForDeleteId)).thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/admin/students/delete/{id}", entityForDeleteId)
                .header("Referer", "/admin/users")
                .with(user("user").authorities(new SimpleGrantedAuthority("ADMIN")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists(parameterizedAttribute))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verifyNoInteractions(personServiceMock);
        Mockito.verify(studentServiceMock, Mockito.times(1)).deleteById(Mockito.anyLong());
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
