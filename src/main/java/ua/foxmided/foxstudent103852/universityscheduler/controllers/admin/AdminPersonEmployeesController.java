package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

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
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AdminPersonService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;
import ua.foxmided.foxstudent103852.universityscheduler.util.GenericCreator;

@Controller
public class AdminPersonEmployeesController extends AbstractAdminPersonController<Employee> {
    private static final String URL_KEY = "employees";
    private static final String FORM_KEY = "employee";
    private static final Set<EmployeeType> EMPLOYEE_TYPES = Set.of(
            EmployeeType.EMPLOYEE,
            EmployeeType.LECTURER);

    public AdminPersonEmployeesController(AdminPersonService<Employee, Long> entityService,
            CustomEntityValidator<Employee> customEntityValidator, PersonService personService) {
        super(entityService, Employee.class, customEntityValidator, personService);
    }

    @Override
    @GetMapping(URL_KEY + "/new")
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, "new_user", "admin/users/user_new");
    }

    @Override
    @PostMapping(URL_KEY + "/new")
    public ModelAndView newEntity(@ModelAttribute("new_user") @NotNull Employee entity, BindingResult result,
            Model model, RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes);
    }

    @Override
    @GetMapping(URL_KEY + "/delete/{id}")
    public RedirectView deleteEntity(@PathVariable(name = "id") @NotNull Long id,
            RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, getPreviousPageByRequestOrHomePage(request));
    }

    static String addModuleName() {
        return "Employees";
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForNewEntityView() {
        return Map.of(
                "form_type", getFormType(),
                "allEmployeeType", EMPLOYEE_TYPES);
    }

    @Override
    protected String getFormType() {
        return FORM_KEY;
    }

    @Override
    protected Employee getGenericInstance() {
        return new GenericCreator<>(Employee::new).getInstance();
    }

    @Override
    protected String getNewModuleName() {
        return "Add new Employee";
    }

    @Override
    protected @NotNull Employee setRequiredFieldsForNewEntity(@NotNull Employee entity) {
        entity.setUserRoles(Set.of(UserRole.VIEWER));
        entity.setRegisterDate(LocalDateTime.now());
        return entity;
    }

}
