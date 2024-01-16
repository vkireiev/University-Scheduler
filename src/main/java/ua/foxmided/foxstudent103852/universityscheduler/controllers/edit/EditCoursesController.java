package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.HashSet;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsEditor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
@Controller
public class EditCoursesController extends AbstractEditEntityController<Course> {
    private static final String URL_KEY = "courses";
    private static final String ENTITY_KEY = "course";

    private final CourseService courseService;
    private final EmployeeService employeeService;
    private final GroupService groupService;

    public EditCoursesController(CourseService courseService, CustomEntityValidator<Course> customEntityValidator,
            EmployeeService employeeService, GroupService groupService) {
        super(courseService, customEntityValidator, Course.class);
        this.courseService = courseService;
        this.employeeService = employeeService;
        this.groupService = groupService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "courses/course_edit");
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}")
    public ModelAndView changeEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_course_profile") @NotNull Course entity, BindingResult result, Model model) {
        return changeEntityImpl(id, entity, result, model,
                "upd_course_profile", "courses/course_edit", "courses/course_edit");
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_course}/lecturers/delete/{id_lecturer}")
    public RedirectView editCourseDeleteLecturer(
            @PathVariable(name = "id_course") @NotNull Long entityId,
            @PathVariable(name = "id_lecturer") @NotNull Long lecturerId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyCourseLecturers(entityId, lecturerId,
                getAttributesForEditView().get("lecturers"), attributes, request, false);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_course}/lecturers/add/{id_lecturer}")
    public RedirectView editCourseAddLecturer(
            @PathVariable(name = "id_course") @NotNull Long entityId,
            @PathVariable(name = "id_lecturer") @NotNull Long lecturerId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyCourseLecturers(entityId, lecturerId,
                getAttributesForEditView().get("lecturers"), attributes, request, true);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_course}/groups/delete/{id_group}")
    public RedirectView editCourseDeleteGroup(
            @PathVariable(name = "id_course") @NotNull Long entityId,
            @PathVariable(name = "id_group") @NotNull Long groupId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyCourseGroups(entityId, groupId,
                getAttributesForEditView().get("groups"), attributes, request, false);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_course}/groups/add/{id_group}")
    public RedirectView editCourseAddGroup(
            @PathVariable(name = "id_course") @NotNull Long entityId,
            @PathVariable(name = "id_group") @NotNull Long groupId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyCourseGroups(entityId, groupId,
                getAttributesForEditView().get("groups"), attributes, request, true);
    }

    @Override
    protected Course setRequiredFieldsBeforeUpdateEntity(Course entity, Course source) {
        entity.setGroups(new HashSet<>(source.getGroups()));
        entity.setLecturers(new HashSet<>(source.getLecturers()));
        return entity;
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Course entity) {
        return Map.of(
                "addLecturersList", employeeService.findAllByEmployeeType(EmployeeType.LECTURER).stream()
                        .filter(lecturer -> !entity.getLecturers().contains(lecturer))
                        .toList(),
                "addGroupsList", groupService.getAll().stream()
                        .filter(group -> !entity.getGroups().contains(group))
                        .toList());
    }

    static String addModuleNameToModel() {
        return "Courses";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Course";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_course_profile",
                "lecturers", "upd_course_lecturers",
                "groups", "upd_course_groups");
    }

    private RedirectView modifyCourseLecturers(Long courseId, Long groupId,
            String currentAttribute, RedirectAttributes attributes, HttpServletRequest request, boolean isAssign) {
        Course course = getEntityOrThrowEntityNotFoundException(courseId);
        Employee lecturer = returnEntityOrThrowEntityNotFoundException(employeeService.get(groupId),
                "Lecturer not found", "ID = " + groupId);

        boolean elementAlreadyAdded = lecturer.getCourses().contains(course);

        if (isAssign == elementAlreadyAdded) {
            String error = isAssign
                    ? "Trying to add a Lecturer who is already assigned to a Course"
                    : "Trying to remove a Lecturer who is not assigned to a Course";
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, error);
            return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));
        }

        if (isAssign) {
            lecturer.addCourse(course);
        } else {
            lecturer.removeCourse(course);
        }

        try {
            updateCourseLecturers(lecturer, isAssign, currentAttribute, attributes);
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }
        return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));

    }

    private void updateCourseLecturers(Employee lecturer, boolean isAssign,
            String currentAttribute, RedirectAttributes attributes) {
        handleUpdate(
                () -> employeeService.updateCourses(lecturer.getId(), lecturer.getCourses()),
                "Course has been successfully " + (isAssign ? "assigned to" : "unassigned from") + " the Lecturer",
                "Failed to " + (isAssign ? "assign" : "unassign") + " Course to the Lecturer",
                attributes,
                currentAttribute);
    }

    private RedirectView modifyCourseGroups(Long courseId, Long groupId,
            String currentAttribute, RedirectAttributes attributes, HttpServletRequest request, boolean isAssign) {
        Course course = getEntityOrThrowEntityNotFoundException(courseId);
        Group group = returnEntityOrThrowEntityNotFoundException(groupService.get(groupId),
                "Group not found", "ID = " + groupId);

        boolean elementAlreadyAdded = group.getCourses().contains(course);

        if (isAssign == elementAlreadyAdded) {
            String error = isAssign
                    ? "Trying to add a Group which is already assigned to a Course"
                    : "Trying to remove a Group which is not assigned to a Course";
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, error);
            return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));
        }

        if (isAssign) {
            group.addCourse(course);
        } else {
            group.removeCourse(course);
        }

        try {
            updateCourseGroups(group, isAssign, currentAttribute, attributes);
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }
        return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));

    }

    private void updateCourseGroups(Group group, boolean isAssign,
            String currentAttribute, RedirectAttributes attributes) {
        handleUpdate(
                () -> groupService.updateCourses(group.getId(), group.getCourses()),
                "Group has been successfully " + (isAssign ? "assigned to" : "unassigned for") + " the Course",
                "Failed to " + (isAssign ? "assign" : "unassign") + " Group "
                        + (isAssign ? "to" : "for") + " the Course",
                attributes,
                currentAttribute);
    }

}
