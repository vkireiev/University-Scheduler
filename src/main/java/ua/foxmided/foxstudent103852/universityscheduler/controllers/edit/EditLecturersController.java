package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
@Controller
public class EditLecturersController extends AbstractEditEntityController<Employee> {
    private static final String URL_KEY = "lecturers";
    private static final String FORM_KEY = "lecturer";

    private final EmployeeService employeeService;
    private final CourseService courseService;

    public EditLecturersController(EmployeeService employeeService,
            CustomEntityValidator<Employee> customEntityValidator, CourseService courseService) {
        super(employeeService, customEntityValidator, Employee.class);
        this.employeeService = employeeService;
        this.courseService = courseService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "lecturers/lecturer_edit");
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_lecturer}/courses/delete/{id_course}")
    public RedirectView editLecturerDeleteCourse(
            @PathVariable(name = "id_lecturer") @NotNull Long entityId,
            @PathVariable(name = "id_course") @NotNull Long courseId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyLecturerCourses(entityId, courseId, attributes, request, false);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_lecturer}/courses/add/{id_course}")
    public RedirectView editLecturerAddCourse(
            @PathVariable(name = "id_lecturer") @NotNull Long entityId,
            @PathVariable(name = "id_course") @NotNull Long courseId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyLecturerCourses(entityId, courseId, attributes, request, true);
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Employee entity) {
        return Map.of(
                "form_type", FORM_KEY,
                "addCoursesList", courseService.getAll().stream()
                        .filter(course -> !entity.getCourses().contains(course))
                        .toList());
    }

    static String addModuleNameToModel() {
        return "Lecturers";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Lecturer";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_user_profile",
                "courses", "upd_lecturer_courses");
    }

    private RedirectView modifyLecturerCourses(Long entityId, Long courseId,
            RedirectAttributes attributes, HttpServletRequest request, boolean isAssign) {
        Employee lecturer = getEntityOrThrowEntityNotFoundException(entityId);
        Course course = returnEntityOrThrowEntityNotFoundException(
                courseService.get(courseId), "Course not found", "ID = " + courseId);

        String currentAttribute = getAttributesForEditView().get("courses");
        boolean courseAlreadyAssigned = lecturer.getCourses().contains(course);

        if (isAssign == courseAlreadyAssigned) {
            String error = isAssign
                    ? "Trying to assign Course that is already assigned to a Lecturer"
                    : "Trying to unassign Course that is not assigned to a Lecturer";
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, error);
            return new RedirectView(getRedirectEditEntityViewUrl(request, entityId));
        }

        if (isAssign) {
            lecturer.addCourse(course);
        } else {
            lecturer.removeCourse(course);
        }

        try {
            updateLecturerCourses(lecturer, isAssign, currentAttribute, attributes);
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }
        return new RedirectView(getRedirectEditEntityViewUrl(request, entityId));
    }

    private void updateLecturerCourses(Employee lecturer, boolean isAssign,
            String currentAttribute, RedirectAttributes attributes) {
        handleUpdate(
                () -> employeeService.updateCourses(lecturer.getId(), lecturer.getCourses()),
                "Course has been successfully " + (isAssign ? "assigned to" : "unassigned for") + " the Lecturer",
                "Failed to " + (isAssign ? "assign" : "unassign") + " Course "
                        + (isAssign ? "to" : "for") + " the Lecturer",
                attributes,
                currentAttribute);
    }

}
