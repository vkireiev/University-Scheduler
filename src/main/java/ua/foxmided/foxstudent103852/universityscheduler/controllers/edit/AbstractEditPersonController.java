package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.exception.PersonNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AdminPersonService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@RequestMapping("/admin/")
public abstract class AbstractEditPersonController<T extends Person> extends AbstractEditControllerHelper<T> {
    protected static final Set<UserRole> USER_ROLES = Set.of(
            UserRole.VIEWER,
            UserRole.EDITOR,
            UserRole.ADMIN);

    protected final AdminPersonService<T, Long> entityService;

    protected AbstractEditPersonController(AdminPersonService<T, Long> entityService, Class<T> genericType,
            CustomEntityValidator<T> customEntityValidator) {
        super(entityService, genericType, customEntityValidator);
        this.entityService = entityService;
    }

    @IsAdmin
    public String editEntityPage(@NotNull Long id, Model model) {
        return editEntityPageImpl(id, model);
    }

    @IsAdmin
    public String changeProfileEntity(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        return changeProfileEntityImpl(id, entity, result, model);
    }

    @IsAdmin
    public String changeUserRolesEntity(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        return changeUserRolesEntityImpl(id, entity, result, model);
    }

    @IsAdmin
    public String changeEmailEntity(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        return changeEmailEntityImpl(id, entity, result, model);
    }

    @IsAdmin
    public String changePasswordEntity(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        return changePasswordEntityImpl(id, entity, result, model);
    }

    @IsAdmin
    public RedirectView changeLockedEntity(@NotNull Long id,
            @NotNull Boolean locked, RedirectAttributes attributes) {
        return changeLockedEntityImpl(id, locked, attributes, null);
    }

    protected abstract @NotBlank String getFormType();

    protected String editEntityPageImpl(@NotNull Long id, Model model) {
        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(), getEditModuleName());

        return "admin/users/user_edit";
    }

    protected String changeProfileEntityImpl(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("profile");

        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result,
                "firstName",
                "lastName",
                "birthday",
                "phoneNumber");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "admin/users/user_edit";
        }

        handleUpdate(
                () -> entityService.updateProfile(id, entity),
                genericType.getSimpleName() + " profile updated successfully",
                "Failed to update " + genericType.getSimpleName() + " profile",
                model,
                currentAttribute);

        return "admin/users/user_edit";
    }

    protected String changeUserRolesEntityImpl(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("roles");

        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result, "userRoles");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "admin/users/user_edit";
        }

        handleUpdate(
                () -> entityService.updateUserRoles(id, entity.getUserRoles()),
                "Access roles updated successfully",
                "Failed to update access roles",
                model,
                currentAttribute);

        return "admin/users/user_edit";
    }

    protected String changeEmailEntityImpl(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("email");

        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result, "email");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "admin/users/user_edit";
        }

        handleUpdate(
                () -> entityService.updateEmail(id, entity.getEmail()),
                "Email changed successfully",
                "Failed to change email",
                model,
                currentAttribute);

        return "admin/users/user_edit";
    }

    protected String changePasswordEntityImpl(@NotNull Long id,
            @NotNull T entity, BindingResult result, Model model) {
        String currentAttribute = getAttributesForEditView().get("password");

        T entityForUpdate = getEntityOrThrowEntityNotFoundException(id);

        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result, "password");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "admin/users/user_edit";
        }

        handleUpdate(
                () -> entityService.updatePassword(id, entity.getPassword()),
                "Password changed successfully",
                "Failed to change password",
                model,
                currentAttribute);

        return "admin/users/user_edit";
    }

    protected RedirectView changeLockedEntityImpl(@NotNull Long id, boolean locked,
            RedirectAttributes attributes, @NotBlank String urlKey) {
        String currentAttribute = getAttributesForEditView().get("locked");

        if (!entityService.existsById(id)) {
            throw new PersonNotFoundException(genericType.getSimpleName() + " not found");
        }

        if (entityService.updateLocked(id, locked)) {
            addMessageToFlashAttributes(attributes, "msg_" + currentAttribute,
                    "Account " + ((locked) ? "locked" : "unlocked") + " successfully");
        } else {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    "Failed to " + ((locked) ? "lock" : "unlock") + " account");
        }

        return new RedirectView("/admin/" + urlKey + "/edit/" + id);
    }

}
