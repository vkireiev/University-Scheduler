package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Map;
import java.util.Set;

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
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AdminPersonService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Controller
@RequestMapping("/admin/")
public class EditPersonEmployeesController extends AbstractEditPersonController<Employee> {
    private static final String URL_KEY = "employees";
    private static final String FORM_KEY = "employee";
    private static final Set<EmployeeType> EMPLOYEE_TYPES = Set.of(
            EmployeeType.EMPLOYEE,
            EmployeeType.LECTURER);

    public EditPersonEmployeesController(AdminPersonService<Employee, Long> entityService,
            CustomEntityValidator<Employee> customEntityValidator) {
        super(entityService, Employee.class, customEntityValidator);
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public String editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/profile")
    public String changeProfileEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_profile") @NotNull Employee employee, BindingResult result, Model model) {
        return changeProfileEntityImpl(id, employee, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/roles")
    public String changeUserRolesEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_roles") @NotNull Employee employee, BindingResult result, Model model) {
        return changeUserRolesEntityImpl(id, employee, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/email")
    public String changeEmailEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_email") @NotNull Employee employee, BindingResult result, Model model) {
        return changeEmailEntityImpl(id, employee, result, model);
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}/password")
    public String changePasswordEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_pswd") @NotNull Employee employee, BindingResult result, Model model) {
        return changePasswordEntityImpl(id, employee, result, model);
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}/locked/{locked}")
    public RedirectView changeLockedEntity(@PathVariable(name = "id") @NotNull Long id,
            @PathVariable(name = "locked") @NotNull Boolean locked, RedirectAttributes attributes) {
        return changeLockedEntityImpl(id, locked, attributes, URL_KEY);
    }

    static String addModuleNameToModel() {
        return "Employees";
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_user_profile",
                "roles", "upd_user_roles",
                "locked", "upd_user_locked",
                "email", "upd_user_email",
                "password", "upd_user_pswd");
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Employee entity) {
        return Map.of(
                "form_type", getFormType(),
                "allRoles", USER_ROLES,
                "allEmployeeType", EMPLOYEE_TYPES);
    }

    @Override
    protected String getFormType() {
        return FORM_KEY;
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Employee";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

}
