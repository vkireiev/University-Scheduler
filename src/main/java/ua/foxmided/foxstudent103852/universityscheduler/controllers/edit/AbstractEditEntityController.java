package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsEditor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CrudService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
public abstract class AbstractEditEntityController<T> extends AbstractEditControllerHelper<T> {

    private final CrudService<T, Long> entityService;

    protected AbstractEditEntityController(CrudService<T, Long> entityService,
            CustomEntityValidator<T> customEntityValidator, Class<T> genericType) {
        super(entityService, genericType, customEntityValidator);
        this.entityService = entityService;
    }

    @IsEditor
    public ModelAndView editEntityPage(@NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, null);
    }

    @IsEditor
    public ModelAndView changeEntity(@NotNull Long id, @NotNull T entity, BindingResult result, Model model) {
        return changeEntityImpl(id, entity, result, model, null, null, null);
    }

    protected ModelAndView editEntityPageImpl(@NotNull Long id, Model model, @NotBlank String editEntityView) {
        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(), getEditModuleName());

        return new ModelAndView(editEntityView);
    }

    protected ModelAndView changeEntityImpl(@NotNull Long id, @NotNull T entity, BindingResult result, Model model,
            @NotBlank String currentAttribute, @NotBlank String entitiesView, @NotBlank String editEntityView) {
        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        entity = setRequiredFieldsBeforeUpdateEntity(entity, entityForUpdate);

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result);
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return new ModelAndView(editEntityView);
        }

        try {
            entityService.update(entity);
            addMessageToModelAttributes(model, "msg_" + currentAttribute,
                    genericType.getSimpleName() + " updated successfully");
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }

        return new ModelAndView(editEntityView);
    }

}
