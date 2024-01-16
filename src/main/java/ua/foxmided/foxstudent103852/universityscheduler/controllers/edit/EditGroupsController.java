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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
@Controller
public class EditGroupsController extends AbstractEditEntityController<Group> {
    private static final String URL_KEY = "groups";
    private static final String ENTITY_KEY = "group";

    private final GroupService groupService;
    private final CourseService courseService;

    public EditGroupsController(GroupService groupService, CustomEntityValidator<Group> customEntityValidator,
            CourseService courseService) {
        super(groupService, customEntityValidator, Group.class);
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "groups/group_edit");
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}")
    public ModelAndView changeEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_group_profile") @NotNull Group entity, BindingResult result, Model model) {
        return changeEntityImpl(id, entity, result, model,
                "upd_group_profile", "groups/group_edit", "groups/group_edit");
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_group}/courses/delete/{id_course}")
    public RedirectView editGroupDeleteCourse(
            @PathVariable(name = "id_group") @NotNull Long entityId,
            @PathVariable(name = "id_course") @NotNull Long courseId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyGroupCourses(entityId, courseId,
                getAttributesForEditView().get("courses"), attributes, request, false);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_group}/courses/add/{id_course}")
    public RedirectView editGroupAddCourse(
            @PathVariable(name = "id_group") @NotNull Long entityId,
            @PathVariable(name = "id_course") @NotNull Long courseId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyGroupCourses(entityId, courseId,
                getAttributesForEditView().get("courses"), attributes, request, true);
    }

    @Override
    protected Group setRequiredFieldsBeforeUpdateEntity(Group entity, Group source) {
        entity.setStudents(new HashSet<>(source.getStudents()));
        entity.setCourses(new HashSet<>(source.getCourses()));
        return entity;
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Group entity) {
        return Map.of(
                "addCoursesList", courseService.getAll().stream()
                        .filter(course -> !entity.getCourses().contains(course))
                        .toList());
    }

    static String addModuleNameToModel() {
        return "Groups";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Group";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_group_profile",
                "courses", "upd_group_courses");
    }

    private RedirectView modifyGroupCourses(Long groupId, Long courseId,
            String currentAttribute, RedirectAttributes attributes, HttpServletRequest request, boolean isAssign) {
        Group group = getEntityOrThrowEntityNotFoundException(groupId);
        Course course = returnEntityOrThrowEntityNotFoundException(
                courseService.get(courseId), "Course not found", "ID = " + courseId);

        boolean elementAlreadyAdded = group.getCourses().contains(course);

        if (isAssign == elementAlreadyAdded) {
            String error = isAssign
                    ? "Trying to add a Course which is already assigned to a Group"
                    : "Trying to remove a Course which is not assigned to a Group";
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, error);
            return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));
        }

        if (isAssign) {
            group.addCourse(course);
        } else {
            group.removeCourse(course);
        }

        try {
            updateGroupCourses(group, isAssign, currentAttribute, attributes);
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }
        return new RedirectView(getRedirectEditEntityViewUrl(request, courseId));

    }

    private void updateGroupCourses(Group group, boolean isAssign,
            String currentAttribute, RedirectAttributes attributes) {
        handleUpdate(
                () -> groupService.updateCourses(group.getId(), group.getCourses()),
                "Course has been successfully " + (isAssign ? "assigned to" : "unassigned for") + " the Group",
                "Failed to " + (isAssign ? "assign" : "unassign") + " Course "
                        + (isAssign ? "to" : "for") + " the Group",
                attributes,
                currentAttribute);
    }

}
