package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.controllers.GenericControllerHelper;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.ReadService;
import ua.foxmided.foxstudent103852.universityscheduler.util.ApplyOperation;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

public abstract class AbstractEditControllerHelper<T> extends GenericControllerHelper<T> {

    protected final CustomEntityValidator<T> customEntityValidator;

    protected AbstractEditControllerHelper(ReadService<T, Long> entityService, Class<T> genericType,
            CustomEntityValidator<T> customEntityValidator) {
        super(genericType, entityService);
        this.customEntityValidator = customEntityValidator;
    }

    protected abstract String getEditModuleName();

    protected abstract String getControllerUrlKey();

    protected @NotNull Map<@NotNull String, @NotNull String> getAttributesForEditView() {
        return Map.of();
    }

    protected T setRequiredFieldsBeforeUpdateEntity(T entity, T source) {
        return entity;
    }

    protected Model setModelAttributesForEditEntityView(Model model, T entity,
            Set<String> attributeNames, String moduleName) {
        if (model != null) {
            if (attributeNames != null) {
                for (String attributeName : attributeNames) {
                    model.addAttribute(attributeName, entity);
                }
            }
            if (moduleName != null) {
                model.addAttribute("moduleName", moduleName);
            }

            model.addAllAttributes(setAdditionalModelAttributesForEditEntityView(entity));
        }

        return model;
    }

    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(T entity) {
        return Map.of();
    }

    protected void handleUpdate(ApplyOperation operation, String successMessage, String failureMessage,
            RedirectAttributes attributes, String currentAttribute) {
        if (operation.apply()) {
            addMessageToFlashAttributes(attributes, "msg_" + currentAttribute, successMessage);
        } else {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, failureMessage);
        }
    }

    protected void handleUpdate(ApplyOperation operation, String successMessage, String failureMessage,
            Model model, String currentAttribute) {
        if (operation.apply()) {
            addMessageToModelAttributes(model, "msg_" + currentAttribute, successMessage);
        } else {
            addMessageToModelAttributes(model, "err_" + currentAttribute, failureMessage);
        }
    }

    protected @NotNull Set<String> getMissingAttributesForEditView(String... attributesForExclude) {
        if (attributesForExclude != null) {
            return getAttributesForEditView().values().stream()
                    .filter(attribute -> !List.of(attributesForExclude).contains(attribute))
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    protected String getRedirectEditEntityViewUrl(HttpServletRequest request, Long entityId) {
        return request != null
                ? getPreviousPageByRequestOrHomePage(request)
                : "/" + getControllerUrlKey() + "/edit/" + entityId;
    }

}
