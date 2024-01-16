package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.junit.jupiter.params.provider.EmptySource;
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
import org.springframework.validation.BindingResult;

import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTest;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestAuditorium;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestCourse;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestEmployee;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestGroup;
import ua.foxmided.foxstudent103852.universityscheduler.util.ConstantsTestLecture;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@SpringBootTest
@AutoConfigureMockMvc
class EditLecturesControllerTest {
    private final static Map<String, String> ATTRIBUTES_FOR_EDIT_VIEW = Map.of(
            "profile", "upd_lecture_profile",
            "groups", "upd_lecture_groups");
    private final static List<String> ENTITY_FIELDS_FOR_VERIFICATION = List.of(
            "id", "subject", "course", "lecturer", "lectureDate", "timeSlot",
            //
            "groups");

    @Autowired
    MockMvc mvc;

    @MockBean
    LectureService lectureServiceMock;

    @MockBean
    AuditoriumService auditoriumServiceMock;

    @MockBean
    CourseService courseServiceMock;

    @MockBean
    GroupService groupServiceMock;

    @MockBean
    EmployeeService employeeServiceMock;

    @Autowired
    CustomEntityValidator<Lecture> customEntityValidator;

    private final Long entityForUpdateId = ConstantsTestLecture.LECTURE_ID_VALID_1;
    private Lecture entityForUpdateMock;
    private Lecture entityForEditForm;

    @BeforeEach
    void beforeEach() {
        entityForUpdateMock = ConstantsTestLecture.getTestLecture(entityForUpdateId);
        entityForEditForm = ConstantsTestLecture.getTestLecture(entityForUpdateId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/lectures/edit/{id}",
            "/lectures/edit/{id_lecture}/groups/delete/1",
            "/lectures/edit/{id_lecture}/groups/add/1"
    })
    void WhenCalledAsUnauthenticatedUserViaGetRequest_ThenRedirectToLoginPage(
            String parameterizedUrl) throws Exception {

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void WhenCalledAsUnauthenticatedUserViaPostRequest_ThenRedirectToLoginPage() throws Exception {

        mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaGetRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(get("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForForbiddenScenario")
    void editEntityPage_WhenCalledWithoutEditRoleViaPostRequest_ShouldReturnAccessDeniedView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {

        mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(view().name("errors/access_denied_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("applicationName"));

        Mockito.verifyNoInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @MethodSource("provideAuthoritiesForAccessibleScenario")
    void editEntityPage_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView(
            Collection<GrantedAuthority> parameterizedAuthorities) throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        mvc.perform(get("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(parameterizedAuthorities))
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/lectures/edit/{id}",
            "/lectures/edit/{id_lecture}/groups/delete/1",
            "/lectures/edit/{id_lecture}/groups/add/1"
    })
    void WhenCalledWithEditRoleViaGetRequestAndNotExistUpdatingLecture_ShouldReturnViewWithErrorMessage(
            String parameterizedUrl) throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Lecture not found", result.getResolvedException().getMessage()))
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void editEntityPage_WhenCalledWithEditRoleAndExistUpdatingLecture_ShouldReturnEditLectureView() throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        MvcResult resultController = mvc.perform(get("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR"))))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lecture_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditLectureViewAttributes(resultController);
        checkLectureAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "", entityForEditForm,
                getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkLectureAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
            ConstantsTestLecture.LECTURE_SUBJECT_INVALID_1,
            ConstantsTestLecture.LECTURE_SUBJECT_INVALID_2
    })
    void changeEntity_WhenCalledWithEditRoleAndExistValidationViolations_ShouldReturnEditLectureViewWithMessage(
            String parameterizedSubject) throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        entityForEditForm.setSubject(parameterizedSubject);
        MvcResult resultController = mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lecture_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditLectureViewAttributes(resultController);
        checkLectureAttributes(resultController, "upd_lecture_profile", entityForEditForm,
                "id", "subject", "course", "lecturer", "lectureDate", "timeSlot");
        checkLectureAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_lecture_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInBindingResult(resultController, "upd_lecture_profile", "subject");
        checkForMessagePresenceInModelMap(resultController, "err_upd_lecture_profile",
                Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
        checkLectureAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownDataProcessingException_ShouldReturnErrorView()
            throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(lectureServiceMock.update(Mockito.any(Lecture.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE));

        entityForEditForm.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE);
        mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("errors/blank_page"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(model().attributeExists("applicationName"))
                .andExpect(model().attributeExists("datetime"))
                .andExpect(model().attributeExists("gErrors"));

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(lectureServiceMock, Mockito.times(1)).update(Mockito.any(Lecture.class));
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndThrownDataIntegrityViolationException_ShouldReturnEditLectureViewWithErrorMessage()
            throws Exception {
        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());
        Mockito.when(lectureServiceMock.update(Mockito.any(Lecture.class)))
                .thenThrow(new EntityUpdateDataIntegrityViolationException(
                        ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE));

        entityForEditForm.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE);
        MvcResult resultController = mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lecture_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditLectureViewAttributes(resultController);
        checkLectureAttributes(resultController, "upd_lecture_profile", entityForEditForm,
                "id", "subject", "course", "lecturer", "lectureDate", "timeSlot");
        checkLectureAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_lecture_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "err_upd_lecture_profile",
                ConstantsTest.ENTITY_DATA_INTEGRITY_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        checkLectureAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(lectureServiceMock, Mockito.times(1)).update(Mockito.any(Lecture.class));
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @Test
    void changeEntity_WhenCalledWithEditRoleAndLectureUpdated_ShouldReturnEditLectureViewWithMessage()
            throws Exception {
        entityForEditForm.setSubject(ConstantsTestLecture.LECTURE_SUBJECT_FOR_UPDATE);

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(lectureServiceMock.update(Mockito.any(Lecture.class)))
                .thenReturn(entityForEditForm);
        Mockito.when(auditoriumServiceMock.findAllByAvailable(Mockito.anyBoolean()))
                .thenReturn(ConstantsTestAuditorium.getAllTestAuditoriums());
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(ConstantsTestCourse.getAllTestCourses());
        Mockito.when(groupServiceMock.getAll())
                .thenReturn(ConstantsTestGroup.getAllTestGroups());
        Mockito.when(employeeServiceMock.findAllByEmployeeType(Mockito.any(EmployeeType.class)))
                .thenReturn(ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer());

        MvcResult resultController = mvc.perform(post("/lectures/edit/{id}", entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .flashAttr("upd_lecture_profile", entityForEditForm)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/lecture_edit"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding("UTF-8"))
                .andReturn();

        checkEditLectureViewAttributes(resultController);
        checkLectureAttributes(resultController, "upd_lecture_profile", entityForEditForm,
                "id", "subject", "course", "lecturer", "lectureDate", "timeSlot");
        checkLectureAttributesWithExcept(resultController, ATTRIBUTES_FOR_EDIT_VIEW, "upd_lecture_profile",
                entityForUpdateMock, getRequiredEntityFieldsForVerification().toArray(new String[0]));
        checkForMessagePresenceInModelMap(resultController, "msg_upd_lecture_profile", "Lecture updated successfully");
        checkLectureAddGroupsList(resultController, entityForEditForm);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(lectureServiceMock, Mockito.times(1)).update(Mockito.any(Lecture.class));
        Mockito.verify(auditoriumServiceMock, Mockito.times(1)).findAllByAvailable(Mockito.anyBoolean());
        Mockito.verifyNoMoreInteractions(auditoriumServiceMock);
        Mockito.verify(courseServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verify(employeeServiceMock, Mockito.times(1)).findAllByEmployeeType(Mockito.any(EmployeeType.class));
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/lectures/edit/{id_lecture}/groups/delete/{id_group}",
            "/lectures/edit/{id_lecture}/groups/add/{id_group}"
    })
    void WhenCalledWithEditRoleAndNotExistGroup_ShouldReturnViewWithErrorMessage(String parameterizedUrl)
            throws Exception {
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1));
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Group groupForDelete = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_NOT_EXIST);
        assertFalse(entityForUpdateMock.getGroups().contains(groupForDelete));

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get(parameterizedUrl, entityForUpdateId, groupForDelete.getId())
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

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void editLectureDeleteGroup_WhenCalledAndGroupNotAssignedOnLecture_ShouldReturnRefererViewWithMessage()
            throws Exception {
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1));
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Group groupForDelete = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_FOR_DELETE);
        assertFalse(entityForUpdateMock.getGroups().contains(groupForDelete));

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForDelete));

        MvcResult resultController = mvc.perform(get("/lectures/edit/{id_lecture}/groups/delete/{id_group}",
                entityForUpdateId, groupForDelete.getId())
                .header("Referer", "/lectures/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_lecture_groups",
                "Trying to remove a Group not assigned to a Lecture");

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_lecture_groups, false, Failed to unassign Group for the Lecture",
            "msg_upd_lecture_groups, true, Group has been successfully unassigned from the Lecture"
    })
    void editLectureDeleteGroup_WhenCalledAndGroupNotDeletedOrDeleted_ShouldReturnRefererViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1));
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Group groupForDelete = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        assertTrue(entityForUpdateMock.getGroups().contains(groupForDelete));

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForDelete));
        Mockito.when(lectureServiceMock.updateGroups(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/lectures/edit/{id_lecture}/groups/delete/{id_group}",
                entityForUpdateId, groupForDelete.getId())
                .header("Referer", "/lectures/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(lectureServiceMock, Mockito.times(1)).updateGroups(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @Test
    void editLectureAddGroup_WhenCalledAndGroupAlreadyAssignedOnLecture_ShouldReturnEditCourseViewWithMessage()
            throws Exception {
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1));
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Group groupForAdd = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        assertTrue(entityForUpdateMock.getGroups().contains(groupForAdd));

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForAdd));

        MvcResult resultController = mvc.perform(get("/lectures/edit/{id_lecture}/groups/add/{id_group}",
                entityForUpdateId, groupForAdd.getId())
                .header("Referer", "/lectures/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, "err_upd_lecture_groups",
                "Trying to add a Group that is already assigned to a Lecture");

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "err_upd_lecture_groups, false, Failed to assign Group to the Lecture",
            "msg_upd_lecture_groups, true, Group has been successfully assigned to the Lecture"
    })
    void editLectureAddGroup_WhenCalledAndGroupNotAddedOrAdded_ShouldReturnRefererViewWithMessage(
            String parameterizedAttribute, boolean parameterizedServiceResult, String parameterizedServiceMessage)
            throws Exception {
        entityForUpdateMock.addGroup(ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_2));
        Group groupForAdd = ConstantsTestGroup.getTestGroup(ConstantsTestGroup.GROUP_ID_VALID_1);
        assertFalse(entityForUpdateMock.getGroups().contains(groupForAdd));

        Mockito.when(lectureServiceMock.get(entityForUpdateId))
                .thenReturn(Optional.of(entityForUpdateMock));
        Mockito.when(groupServiceMock.get(Mockito.anyLong()))
                .thenReturn(Optional.of(groupForAdd));
        Mockito.when(lectureServiceMock.updateGroups(Mockito.anyLong(), Mockito.anySet()))
                .thenReturn(parameterizedServiceResult);

        MvcResult resultController = mvc.perform(get("/lectures/edit/{id_lecture}/groups/add/{id_group}",
                entityForUpdateId, groupForAdd.getId())
                .header("Referer", "/lectures/edit/" + entityForUpdateId)
                .with(user("user").authorities(new SimpleGrantedAuthority("EDITOR")))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures/edit/" + entityForUpdateId))
                .andReturn();

        checkForMessagePresenceInFlashMap(resultController, parameterizedAttribute, parameterizedServiceMessage);

        Mockito.verify(lectureServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verify(lectureServiceMock, Mockito.times(1)).updateGroups(Mockito.anyLong(), Mockito.anySet());
        Mockito.verifyNoMoreInteractions(lectureServiceMock);
        Mockito.verify(groupServiceMock, Mockito.times(1)).get(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
        Mockito.verifyNoInteractions(auditoriumServiceMock);
        Mockito.verifyNoInteractions(courseServiceMock);
        Mockito.verifyNoInteractions(employeeServiceMock);
    }

    private List<String> getRequiredEntityFieldsForVerification(String... excludeFields) {
        List<String> listExcludeFields = Stream.of(excludeFields).collect(Collectors.toList());
        return ENTITY_FIELDS_FOR_VERIFICATION.stream()
                .filter(field -> !listExcludeFields.contains(field))
                .collect(Collectors.toList());
    }

    private void checkEditLectureViewAttributes(MvcResult resultController) {
        assertThat(resultController.getModelAndView().getModel())
                .containsEntry("applicationName", "University-Scheduler")
                .containsEntry("moduleName", "Edit Lecture")
                .containsEntry("availableAuditoriums", ConstantsTestAuditorium.getAllTestAuditoriums())
                .containsEntry("allCourses", ConstantsTestCourse.getAllTestCourses())
                .containsEntry("allLecturers", ConstantsTestEmployee.getAllTestEmployeesWithEmployeeTypeAsLecturer())
                .containsKey("timeSlots");
    }

    private void checkLectureAttributes(MvcResult resultController, String attribute,
            Lecture entityForCheck, String... fields) {
        assertThat(resultController.getModelAndView().getModel())
                .containsKey(attribute);
        assertThat(resultController.getModelAndView().getModel().get(attribute))
                .isInstanceOf(Lecture.class)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(entityForCheck);
    }

    private void checkLectureAttributesWithExcept(MvcResult resultController, Map<String, String> attributes,
            String excludeAttribute, Lecture entityForCheck, String... fields) {
        List<String> checkAttributes = attributes.values().stream()
                .filter(attribut -> !attribut.equalsIgnoreCase(excludeAttribute)).collect(Collectors.toList());
        for (String attribute : checkAttributes) {
            assertThat(resultController.getModelAndView().getModel())
                    .containsKey(attribute);
            assertThat(resultController.getModelAndView().getModel().get(attribute))
                    .isInstanceOf(Lecture.class)
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

    private void checkLectureAddGroupsList(MvcResult resultController, Lecture entity) {
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
