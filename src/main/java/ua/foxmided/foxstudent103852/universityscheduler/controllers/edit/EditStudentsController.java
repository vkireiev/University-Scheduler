package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsEditor;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Controller
public class EditStudentsController extends AbstractEditEntityController<Student> {
    private static final String URL_KEY = "students";
    private static final String FORM_KEY = "student";

    private final StudentService studentService;
    private final GroupService groupService;

    public EditStudentsController(StudentService entityService, CustomEntityValidator<Student> customEntityValidator,
            GroupService groupService) {
        super(entityService, customEntityValidator, Student.class);
        this.studentService = entityService;
        this.groupService = groupService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "students/student_edit");
    }

    @IsEditor
    @PostMapping(URL_KEY + "/edit/{id}/group")
    public String changeGroupStudent(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_group") @NotNull Student student, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("group");

        Student studentForUpdate = getEntityOrThrowEntityNotFoundException(id);

        super.setModelAttributesForEditEntityView(model, studentForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(student, currentAttribute, result, "group");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "students/student_edit";
        }

        handleUpdate(
                () -> studentService.updateGroup(id, student.getGroup()),
                "Group changed successfully",
                "Failed to change Group",
                model,
                currentAttribute);

        return "students/student_edit";
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Student entity) {
        return Map.of(
                "form_type", FORM_KEY,
                "allGroups", groupService.getAll().stream()
                        .sorted((group1, group2) -> (int) (group1.getId() - group2.getId()))
                        .toList());
    }

    static String addModuleNameToModel() {
        return "Students";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Student";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_user_profile",
                "group", "upd_user_group");
    }

}
