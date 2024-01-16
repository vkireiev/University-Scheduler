package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.controllers.GenericControllerHelper;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CrudService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@RequestMapping("/admin/")
public abstract class AbstractAdminControllerHelper<T> extends GenericControllerHelper<T> {

    protected final CustomEntityValidator<T> customEntityValidator;
    private final CrudService<T, Long> entityService;

    protected AbstractAdminControllerHelper(CrudService<T, Long> entityService, Class<T> genericType,
            CustomEntityValidator<T> customEntityValidator) {
        super(genericType, entityService);
        this.customEntityValidator = customEntityValidator;
        this.entityService = entityService;
    }

    protected abstract T getGenericInstance();

    protected abstract String getNewModuleName();

    protected T setRequiredFieldsForNewEntity(T entity) {
        return entity;
    }

    protected Model setModelAttributesForNewEntityView(Model model, String moduleName) {
        if (model != null) {
            if (moduleName != null) {
                model.addAttribute("moduleName", moduleName);
            }
            //
            model.addAllAttributes(setAdditionalModelAttributesForNewEntityView());
        }
        return model;
    }

    protected Map<String, Object> setAdditionalModelAttributesForNewEntityView() {
        return Map.of();
    }

    protected String newEntityPageImpl(Model model,
            @NotBlank String currentAttribute, @NotBlank String newEntityView) {
        setModelAttributesForNewEntityView(model, getNewModuleName());

        if (!model.containsAttribute(currentAttribute)) {
            T entity = getGenericInstance();
            model.addAttribute(currentAttribute, entity);
        }

        return newEntityView;
    }

    protected RedirectView deleteEntityImpl(@NotNull Long id,
            RedirectAttributes attributes, String redirectEntitiesView) {
        if (entityService.deleteById(id)) {
            addMessageToFlashAttributes(attributes, "gMessages",
                    genericType.getSimpleName() + " deleted successfully");
        } else {
            addMessageToFlashAttributes(attributes, "gErrors",
                    "Failed to delete " + genericType.getSimpleName());
        }

        return new RedirectView(redirectEntitiesView);
    }

}
