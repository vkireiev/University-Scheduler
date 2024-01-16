package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Controller
@RequestMapping("/admin/")
public class EditPersonStudentsController extends AbstractEditPersonController<Student> {
    private static final String URL_KEY = "students";
    private static final String FORM_KEY = "student";

    private final StudentService studentService;
    private final GroupService groupService;

    public EditPersonStudentsController(StudentService entityService,
            CustomEntityValidator<Student> customEntityValidator, GroupService groupService) {
        super(entityService, Student.class, customEntityValidator);
        this.studentService = entityService;
        this.groupService = groupService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public String editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/profile")
    public String changeProfileEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_profile") @NotNull Student student, BindingResult result, Model model) {
        return changeProfileEntityImpl(id, student, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/roles")
    public String changeUserRolesEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_roles") @NotNull Student student, BindingResult result, Model model) {
        return changeUserRolesEntityImpl(id, student, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/email")
    public String changeEmailEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_email") @NotNull Student student, BindingResult result, Model model) {
        return changeEmailEntityImpl(id, student, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/password")
    public String changePasswordEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_pswd") @NotNull Student student, BindingResult result, Model model) {
        return changePasswordEntityImpl(id, student, result, model);
    }

    @IsAdmin
    @PostMapping(URL_KEY + "/edit/{id}/group")
    public String changeGroupStudent(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_group") @NotNull Student student, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("group");

        Student studentForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, studentForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(student, currentAttribute, result, "group");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute, Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "admin/users/user_edit";
        }

        handleUpdate(
                () -> studentService.updateGroup(id, student.getGroup()),
                "Group changed successfully",
                "Failed to change Group",
                model,
                currentAttribute);

        return "admin/users/user_edit";
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}/locked/{locked}")
    public RedirectView changeLockedEntity(@PathVariable(name = "id") @NotNull Long id,
            @PathVariable(name = "locked") @NotNull Boolean locked, RedirectAttributes attributes) {
        return changeLockedEntityImpl(id, locked, attributes, URL_KEY);
    }

    static String addModuleNameToModel() {
        return "Students";
    }

    @Override
    protected @NotNull Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_user_profile",
                "group", "upd_user_group",
                "roles", "upd_user_roles",
                "locked", "upd_user_locked",
                "email", "upd_user_email",
                "password", "upd_user_pswd");
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Student entity) {
        return Map.of(
                "form_type", getFormType(),
                "allRoles", USER_ROLES,
                "allGroups", groupService.getAll().stream()
                        .sorted((group1, group2) -> (int) (group1.getId() - group2.getId()))
                        .toList());
    }

    @Override
    protected String getFormType() {
        return FORM_KEY;
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Student";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

}
