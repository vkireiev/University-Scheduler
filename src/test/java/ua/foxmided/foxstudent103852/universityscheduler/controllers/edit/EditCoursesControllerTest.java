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
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditCoursesControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_course_profile",
            "lecturers", "upd_course_lecturers",
            "groups", "upd_course_groups");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "name", "description",
            //
            "lecturers", "groups");

    @Autowired
    MockMvc mvc;

    @MockBean
    CourseService courseServiceMock;

    @MockBean
    EmployeeService employeeServiceMock;

    @MockBean
    GroupService groupServiceMock;

    @Autowired
    CustomEntityValidator<Employee> customEntityValidator;

    private final Long entityForUpdateId = ConstantsTestCourse.COURSE_ID_VALID_1;
    private Course entityForUpdateMock;
    private Course entityForEditForm;
    private Employee lecturerForDelete;
    private Group groupForDelete;
    private Employee lecturerForAdd;
    private Group groupForAdd;

    @BeforeEach
    void beforeEach() {
        Employee lecturer1 = ConstantsTestEmployee.getTestEmployee(1L);
        lecturerForDelete = ConstantsTestEmployee.getTestEmployee(3L);
        groupForDelete = ConstantsTestGroup.getTestGroup(1L);
        Group group4 = ConstantsTestGroup.getTestGroup(4L);

        entityForUpdateMock = ConstantsTestCourse.getTestCourse(entityForUpdateId);
        lecturer1.addCourse(entityForUpdateMock);
        lecturerForDelete.addCourse(entityForUpdateMock);
        groupForDelete.addCourse(entityForUpdateMock);
        group4.addCourse(entityForUpdateMock);

        entityForEditForm = ConstantsTestCourse.getTestCourse(entityForUpdateId);
        lecturer1.addCourse(entityForEditForm);
        lecturerForDelete.addCourse(entityForEditForm);
        groupForDelete.addCourse(entityForEditForm);
        group4.addCourse(entityForEditForm);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/courses/edit/{id}",
            "/courses/edit/{id_course}/lecturers/delete/1",
            "/courses/edit/{id_course}/lecturers/add/1",
            "/courses/edit/{id_course}/groups/delete/1",
            "/courses/edit/{id_course}/groups/add/1"
    })
    void WhenCalledAsUnauthenticatedUserViaGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void WhenCalledAsUnauthenticatedUserViaPostRequest_ThenRedirectToLoginPage() throws Exception {

        mvc.perform(post("/courses/edit/{id}", entityForUpdateId)
                .params(getMultiValueMapFromCourseForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaGetRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(get("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaPostRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(post("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .params(getMultiValueMapFromCourseForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/courses/edit/{id}",
            "/courses/edit/{id_course}/lecturers/delete/1",
            "/courses/edit/{id_course}/lecturers/add/1",
            "/courses/edit/{id_course}/groups/delete/1",
            "/courses/edit/{id_course}/groups/add/1"
    })
    void WhenCalledWithEditRoleViaGetRequestAndNotExistUpdatingCourse_ShouldReturnViewWithErrorMessage(
            String parameterizedUrl) throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
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

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistUpdatingCourse_ShouldReturnEditCourseView() throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.findAllByEmployeeType(EmployeeType.LECTURER))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());

        MvcResult resultController = mvc.perform(get("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR"))))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/course_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditCourseViewAttributes(resultController);
        checkCourseAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", entityForEditForm,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkCourseAddLecturersList(resultController, entityForEditForm);
        checkCourseAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            ConstantsTestCourse.COURSE_NAME_INVALID_1,
            ConstantsTestCourse.COURSE_NAME_INVALID_2
    })
    void changeEntity_WhenCalledWithEditRoleAndExistValidationViolations_ShouldReturnEditCourseViewWithMessage(
            String parameterizedName) throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.findAllByEmployeeType(EmployeeType.LECTURER))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());

        entityForEditForm.setName(parameterizedName);
        MvcResult resultController = mvc.perform(post("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromCourseForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/course_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditCourseViewAttributes(resultController);
        checkCourseAttributes(resultController, "upd_course_profile", entityForEditForm,
                "id", "name", "description");
        checkCourseAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_course_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_course_profile", "name");
        checkForMessagePresenceInModelMap(resultController, "err_upd_course_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        checkCourseAddLecturersList(resultController, entityForEditForm);
        checkCourseAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.update(Mockito.any(Course.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));
        Mockito.when(employeeServiceMock.findAllByEmployeeType(EmployeeType.LECTURER))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());

        entityForEditForm.setName(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE);
        mvc.perform(post("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromCourseForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(courseServiceMock, Mockito.times(1)).update(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndCourseUpdated_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        entityForEditForm.setName(ConstantsTestCourse.COURSE_NAME_FOR_UPDATE);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.update(Mockito.any(Course.class)))
                .thenReturn(entityForEditForm);
        Mockito.when(employeeServiceMock.findAllByEmployeeType(EmployeeType.LECTURER))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());

        MvcResult resultController = mvc.perform(post("/courses/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .params(getMultiValueMapFromCourseForPostForm(entityForEditForm))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/course_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditCourseViewAttributes(resultController);
        checkCourseAttributes(resultController, "upd_course_profile", entityForEditForm,
                "id", "name", "description");
        checkCourseAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_course_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "msg_upd_course_profile", "Course updated successfully");
        checkCourseAddLecturersList(resultController, entityForEditForm);
        checkCourseAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(courseServiceMock, Mockito.times(1)).update(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/courses/edit/{id_course}/lecturers/delete/{id_lecturer}",
            "/courses/edit/{id_course}/lecturers/add/{id_lecturer}"
    })
    void WhenCalledWithEditRoleAndNotExistLecturer_ShouldReturnViewWithErrorMessage(String parameterizedUrl)
            throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId, lecturerForDelete.getId())
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Lecturer not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editCourseDeleteLecturer_WhenCalledAndLecturerNotAssignedOnCourse_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        Employee lecturerForDelete = ConstantsTestEmployee
                .getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_FOR_DELETE);
        assertThat(entityForUpdateMock.getLecturers()).doesNotContain(lecturerForDelete);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(lecturerForDelete));

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/lecturers/delete/{id_lecturer}",
                entityForUpdateId, lecturerForDelete.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_course_lecturers",
                "Trying to remove a Lecturer who is not assigned to a Course");

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_course_lecturers, false, Failed to unassign Course to the Lecturer",
            "msg_upd_course_lecturers, true, Course has been successfully unassigned from the Lecturer"
    })
    void editCourseDeleteLecturer_WhenCalledAndLecturerNotDeletedOrDeleted_ShouldReturnEditCourseViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        assertThat(entityForUpdateMock.getLecturers()).contains(lecturerForDelete);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(lecturerForDelete));
        Mockito.when(employeeServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/lecturers/delete/{id_lecturer}",
                entityForUpdateId, lecturerForDelete.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();
        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @Test
    void editCourseAddLecturer_WhenCalledAndLecturerAlreadyAssignedOnCourse_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        lecturerForAdd = lecturerForDelete;
        assertThat(entityForUpdateMock.getLecturers()).contains(lecturerForAdd);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(lecturerForAdd));

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/lecturers/add/{id_lecturer}",
                entityForUpdateId, lecturerForAdd.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_course_lecturers",
                "Trying to add a Lecturer who is already assigned to a Course");

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_course_lecturers, false, Failed to assign Course to the Lecturer",
            "msg_upd_course_lecturers, true, Course has been successfully assigned to the Lecturer"
    })
    void editCourseAddLecturer_WhenCalledAndLecturerNotAddedOrAdded_ShouldReturnEditCourseViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        lecturerForAdd = ConstantsTestEmployee.getTestEmployee(ConstantsTestEmployee.EMPLOYEE_ID_VALID_2);
        assertThat(entityForUpdateMock.getLecturers()).doesNotContain(lecturerForAdd);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(lecturerForAdd));
        Mockito.when(employeeServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/lecturers/add/{id_lecturer}",
                entityForUpdateId, lecturerForAdd.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();
        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/courses/edit/{id_course}/groups/delete/{id_group}",
            "/courses/edit/{id_course}/groups/add/{id_group}"
    })
    void WhenCalledWithEditRoleAndNotExistGroup_ShouldReturnViewWithErrorMessage(String parameterizedUrl)
            throws Exception {
        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId, lecturerForDelete.getId())
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

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void editCourseDeleteGroup_WhenCalledAndGroupNotAssignedOnCourse_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        Group groupForDelete = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE);
        assertThat(entityForUpdateMock.getGroups()).doesNotContain(groupForDelete);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForDelete));

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/groups/delete/{id_group}",
                entityForUpdateId, groupForDelete.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_course_groups",
                "Trying to remove a Group which is not assigned to a Course");

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_course_groups, false, Failed to unassign Group for the Course",
            "msg_upd_course_groups, true, Group has been successfully unassigned for the Course"
    })
    void editCourseDeleteGroup_WhenCalledAndGroupNotDeletedOrDeleted_ShouldReturnEditCourseViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        assertThat(entityForUpdateMock.getGroups()).contains(groupForDelete);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForDelete));
        Mockito.when(groupServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/groups/delete/{id_group}",
                entityForUpdateId, groupForDelete.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void editCourseAddGroup_WhenCalledAndGroupAlreadyAssignedOnCourse_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        groupForAdd = groupForDelete;
        assertThat(entityForUpdateMock.getGroups()).contains(groupForAdd);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForAdd));

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/groups/add/{id_group}",
                entityForUpdateId, groupForAdd.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_course_groups",
                "Trying to add a Group which is already assigned to a Course");

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_course_groups, false, Failed to assign Group to the Course",
            "msg_upd_course_groups, true, Group has been successfully assigned to the Course"
    })
    void editCourseAddGroup_WhenCalledAndGroupNotAddedOrAdded_ShouldReturnEditCourseViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        groupForAdd = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2);
        assertThat(entityForUpdateMock.getGroups()).doesNotContain(groupForAdd);

        Mockito.when(courseServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForAdd));
        Mockito.when(groupServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/courses/edit/{id_course}/groups/add/{id_group}",
                entityForUpdateId, groupForAdd.getId())
                .header("Referer", "/courses/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(groupServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    private MultiValueMap<String, String> getMultiValueMapFromCourseForPostForm(@NotNull Course course) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();
        result.add("id", course.getId().toString());
        result.add("name", course.getName());
        result.add("description", course.getDescription());
        return result;
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditCourseViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Course");
    }

    private void checkCourseAttributes(MvcResult resultController, String attribute,
            Course entityForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Course.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(entityForCheck);
    }

    private void checkCourseAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Course entityForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Course.class)
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

    private void checkCourseAddLecturersList(MvcResult resultController, Course entity) {
        List<Employee> addLecturersList = ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer().stream()
                .filter(lecturer -> !entity.getLecturers().contains(lecturer)).toList();
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("addLecturersList");
        assertThat(resultController.getModelAndView().getModel().get("addLecturersList"))
                .usingRecursiveComparison()
                .isEqualTo(addLecturersList);
    }

    private void checkCourseAddGroupsList(MvcResult resultController, Course entity) {
        List<Group> addGroupsList = ConstantsTestGroup.getAllTestGroups().stream()
                .filter(group -> !entity.getGroups().contains(group)).toList();
        assertThat(resultController.getModelAndView().getModel())
                .containsKey("addGroupsList");
        assertThat(resultController.getModelAndView().getModel().get("addGroupsList"))
                .usingRecursiveComparison()
                .isEqualTo(addGroupsList);
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
