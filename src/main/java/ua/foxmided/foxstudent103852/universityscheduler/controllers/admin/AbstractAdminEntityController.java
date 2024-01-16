package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityAddDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CrudService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

public abstract class AbstractAdminEntityController<T> extends AbstractAdminControllerHelper<T> {

    private final CrudService<T, Long> entityService;

    protected AbstractAdminEntityController(CrudService<T, Long> entityService,
            CustomEntityValidator<T> customEntityValidator, Class<T> genericType) {
        super(entityService, genericType, customEntityValidator);
        this.entityService = entityService;
    }

    @IsAdmin
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, null, null);
    }

    @IsAdmin
    public ModelAndView newEntity(@NotNull T entity, BindingResult result, Model model,
            RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes, null, null, null);
    }

    @IsAdmin
    public RedirectView deleteEntity(@NotNull Long id, RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, null);
    }

    protected ModelAndView newEntityImpl(@NotNull T entity, BindingResult result, Model model,
            RedirectAttributes attributes, @NotBlank String currentAttribute,
            @NotBlank String newEntityView, @NotBlank String redirectEntitiesView) {
        setModelAttributesForNewEntityView(model, getNewModuleName());

        entity = setRequiredFieldsForNewEntity(entity);

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result);
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return new ModelAndView(newEntityView);
        }

        try {
            entityService.add(entity);
            addMessageToFlashAttributes(attributes, "gMessages",
                    genericType.getSimpleName() + " added successfully");
            return new ModelAndView("redirect:" + redirectEntitiesView);
        } catch (EntityAddDataIntegrityViolationException e) {
            addMessageToModelAttributes(model, "gErrors", e.getMessage());
        }

        return new ModelAndView(newEntityView);
    }

}
