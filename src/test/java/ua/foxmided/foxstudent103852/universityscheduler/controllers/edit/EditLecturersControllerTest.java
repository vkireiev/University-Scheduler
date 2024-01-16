package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditLecturersControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_user_profile",
            "courses", "upd_lecturer_courses");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "firstName", "lastName", "phoneNumber", "birthday", "employeeType",
            //
            "courses");

    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeService employeeServiceMock;

    @MockBean
    CourseService courseServiceMock;

    @Autowired
    CustomEntityValidator<Group> customEntityValidator;

    private final Long entityForUpdateId = ConstantsTestEmployee.EMPLOYEE_ID_VALID_1;
    private Employee entityForUpdateMock;
    private Employee entityForEditForm;
    private Course courseForDelete;

    @BeforeEach
    void beforeEach() {
        Course course6 = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_1);
        courseForDelete = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_FOR_DELETE);

        entityForUpdateMock = ConstantsTestEmployee.getTestEmployee(entityForUpdateId);
        entityForUpdateMock.addCourse(course6);
        entityForUpdateMock.addCourse(courseForDelete);

        entityForEditForm = ConstantsTestEmployee.getTestEmployee(entityForUpdateId);
        entityForEditForm.addCourse(course6);
        entityForEditForm.addCourse(courseForDelete);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/lecturers/edit/{id_lecturer}",
            "/lecturers/edit/{id_lecturer}/courses/delete/1",
            "/lecturers/edit/{id_lecturer}/courses/add/1"
    })
    void WhenCalledAsUnauthenticatedUserViaGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRole_ThenForwardToForbiddenPage(
            Collection<GrantedAuthority> methodAuthorities) throws Exception {

        mvc.perform(get("/lecturers/edit/{id_lecturer}", entityForUpdateId)
                .with(user("user").authorities(methodAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(employeeServiceMock.get(entityForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/lecturers/edit/{id_lecturer}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndNotExistLecturer_ShouldReturnViewWithErrorMessage() throws Exception {
        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.empty());

        mvc.perform(get("/lecturers/edit/{id_lecturer}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
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

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistLecturer_ShouldReturnEditLecturerView() throws Exception {
        List<Course> allCourses = ConstantsTestCourse.getAllTestCourses();

        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.getAll()).thenReturn(allCourses);

        MvcResult resultController = mvc.perform(get("/lecturers/edit/{id_lecturer}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturers/lecturer_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditLecturerViewAttributes(resultController);
        checkEmployeeAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", entityForUpdateMock,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkLecturerAddCoursesList(resultController, entityForEditForm);

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/lecturers/edit/{id_lecturer}/courses/delete/{id_course}",
            "/lecturers/edit/{id_lecturer}/courses/add/{id_course}"
    })
    void WhenCalledWithEditRoleAndNotExistCourse_ShouldReturnViewWithErrorMessage(String parameterizedUrl)
            throws Exception {
        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
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

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void editLecturerDeleteCourse_WhenCalledAndCourseNotAssignedToLecturer_ShouldReturnEditLecturerViewWithMessage()
            throws Exception {
        Course courseForDelete = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_2);
        assertThat(entityForUpdateMock.getCourses()).doesNotContain(courseForDelete);

        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(courseForDelete));

        MvcResult resultController = mvc.perform(get("/lecturers/edit/{id_lecturer}/courses/delete/{id_course}",
                entityForUpdateId, courseForDelete.getId())
                .header("Referer", "/lecturers/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_lecturer_courses",
                "Trying to unassign Course that is not assigned to a Lecturer");

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_lecturer_courses, false, Failed to unassign Course for the Lecturer",
            "msg_upd_lecturer_courses, true, Course has been successfully unassigned for the Lecturer"
    })
    void editLecturerDeleteCourse_WhenCalledAndCourseNotDeletedOrDeleted_ShouldReturnEditLecturerViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        assertThat(entityForUpdateMock.getCourses()).contains(courseForDelete);

        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);
        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(courseForDelete));

        MvcResult resultController = mvc.perform(get("/lecturers/edit/{id_lecturer}/courses/delete/{id_course}",
                entityForUpdateId, courseForDelete.getId())
                .header("Referer", "/lecturers/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @Test
    void editLecturerAddCourse_WhenCalledAndCourseAlreadyAssignedToLecturer_ShouldReturnEditLecturerViewWithMessage()
            throws Exception {
        Course courseForAdd = courseForDelete;
        assertThat(entityForUpdateMock.getCourses()).contains(courseForAdd);

        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(courseForAdd));

        MvcResult resultController = mvc.perform(get("/lecturers/edit/{id_lecturer}/courses/add/{id_course}",
                entityForUpdateId, courseForAdd.getId())
                .header("Referer", "/lecturers/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_lecturer_courses",
                "Trying to assign Course that is already assigned to a Lecturer");

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_lecturer_courses, false, Failed to assign Course to the Lecturer",
            "msg_upd_lecturer_courses, true, Course has been successfully assigned to the Lecturer"
    })
    void editLecturerAddCourse_WhenCalledAndCourseNotAddedOrAdded_ShouldReturnEditLecturerViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        Course courseForAdd = ConstantsTestCourse.getTestCourse(ConstantsTestCourse.COURSE_ID_VALID_2);
        assertThat(entityForUpdateMock.getCourses()).doesNotContain(courseForAdd);

        Mockito.when(employeeServiceMock.get(entityForUpdateId)).thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(employeeServiceMock.updateCourses(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);
        Mockito.when(courseServiceMock.get(Mockito.anyLong())).thenReturn(Optional.of(courseForAdd));

        MvcResult resultController = mvc.perform(get("/lecturers/edit/{id_lecturer}/courses/add/{id_course}",
                entityForUpdateId, courseForAdd.getId())
                .header("Referer", "/lecturers/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(employeeServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(employeeServiceMock, Mockito.times(1)).updateCourses(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditLecturerViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Lecturer")
                .containsEntry("form_type", "lecturer");
    }

    private void checkEmployeeAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Employee lecturerForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Employee.class)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(fields)
                    .isEqualTo(lecturerForCheck);
        }
    }

    private void checkForMessagePresenceInFlashMap(MvcResult resultController, String attribute, String message) {
        assertTrue(resultController.getFlashMap().containsKey(attribute));
        assertThat((ArrayList<String>) resultController.getFlashMap().get(attribute))
                .containsSequence(message);
    }

    private void checkLecturerAddCoursesList(MvcResult resultController, Employee entity) {
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

}
