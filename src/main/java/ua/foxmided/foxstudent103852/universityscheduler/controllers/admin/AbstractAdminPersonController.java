package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AdminPersonService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@RequestMapping("/admin/")
public abstract class AbstractAdminPersonController<T extends Person> extends AbstractAdminControllerHelper<T> {
    protected static final Set<UserRole> USER_ROLES = Set.of(
            UserRole.VIEWER,
            UserRole.EDITOR,
            UserRole.ADMIN);

    protected final AdminPersonService<T, Long> entityService;
    protected final PersonService personService;

    protected AbstractAdminPersonController(AdminPersonService<T, Long> entityService, Class<T> genericType,
            CustomEntityValidator<T> customEntityValidator, PersonService personService) {
        super(entityService, genericType, customEntityValidator);
        this.entityService = entityService;
        this.personService = personService;
    }

    @IsAdmin
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, null, null);
    }

    @IsAdmin
    public ModelAndView newEntity(@NotNull T entity, BindingResult result, Model model,
            RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes);
    }

    @IsAdmin
    public RedirectView deleteEntity(@NotNull Long id, RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, getPreviousPageByRequestOrHomePage(request));
    }

    protected abstract @NotBlank String getFormType();

    protected ModelAndView newEntityImpl(@NotNull T entity, BindingResult result,
            Model model, RedirectAttributes attributes) {
        String currentAttribute = "new_user";
        setModelAttributesForNewEntityView(model, getNewModuleName());

        if (personService.existsByUsername(entity.getUsername())) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    "User with such username already exists");
            return new ModelAndView("admin/users/user_new");
        }

        entity = setRequiredFieldsForNewEntity(entity);

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result);
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return new ModelAndView("admin/users/user_new");
        }

        entityService.add(entity);
        addMessageToFlashAttributes(attributes, "gMessages",
                entity.getClass().getSimpleName() + " added successfully");

        return new ModelAndView("redirect:/admin/users");
    }

}
