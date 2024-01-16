package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.security.SecurityPersonDetails;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.UserService;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
@Controller
public class EditUserProfileController extends AbstractEditControllerHelper<Person> {
    private static final String URL_KEY = "profile";

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public EditUserProfileController(UserService userService, CustomEntityValidator<Person> customEntityValidator,
            BCryptPasswordEncoder passwordEncoder) {
        super(userService, Person.class, customEntityValidator);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize(value = "isAuthenticated()")
    @GetMapping(URL_KEY + "/edit")
    public String userProfilePage(Model model) {
        Person authenticatedUser = ((SecurityPersonDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getPerson();

        Person entityForUpdate = getEntityOrThrowEntityNotFoundException(authenticatedUser.getId());
        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(), getEditModuleName());

        return "profile/profile_edit";
    }

    @PreAuthorize(value = "isAuthenticated()")
    @PostMapping(URL_KEY + "/edit/{id}/profile")
    public String changeUserProfile(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_profile") @NotNull Student entity, BindingResult result,
            @RequestParam(value = "curr_password") String currPassword, Model model) {
        String currentAttribute = getAttributesForEditView().get("profile");

        makeUserIdMatching(id);
        Person entityForUpdate = getEntityOrThrowEntityNotFoundException(id);
        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        if (!makeUserPasswordMatching(currPassword, entityForUpdate.getPassword(), currentAttribute, model)) {
            return "profile/profile_edit";
        }

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result,
                "firstName",
                "lastName",
                "birthday",
                "phoneNumber");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "profile/profile_edit";
        }

        handleUpdate(
                () -> userService.updateProfile(id, entity),
                "User profile updated successfully",
                "Failed to change User profile",
                model,
                currentAttribute);

        return "profile/profile_edit";

    }

    @PreAuthorize(value = "isAuthenticated()")
    @PostMapping(URL_KEY + "/edit/{id}/email")
    public String changeUserEmail(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_email") @NotNull Student entity, BindingResult result,
            @RequestParam(value = "curr_password") String currPassword, Model model) {
        String currentAttribute = getAttributesForEditView().get("email");

        makeUserIdMatching(id);
        Person entityForUpdate = getEntityOrThrowEntityNotFoundException(id);
        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        if (!makeUserPasswordMatching(currPassword, entityForUpdate.getPassword(), currentAttribute, model)) {
            return "profile/profile_edit";
        }

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result, "email");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "profile/profile_edit";
        }

        handleUpdate(
                () -> userService.updateEmail(id, entity.getEmail()),
                "Email changed successfully",
                "Failed to change email",
                model,
                currentAttribute);

        return "profile/profile_edit";
    }

    @PreAuthorize(value = "isAuthenticated()")
    @PostMapping(URL_KEY + "/edit/{id}/password")
    public String changeUserPassword(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_user_pswd") @NotNull Student entity, BindingResult result,
            @RequestParam(value = "curr_password") String currPassword, Model model) {
        String currentAttribute = getAttributesForEditView().get("password");

        makeUserIdMatching(id);
        Person entityForUpdate = getEntityOrThrowEntityNotFoundException(id);
        setModelAttributesForEditEntityView(model, entityForUpdate,
                getMissingAttributesForEditView(currentAttribute), getEditModuleName());

        if (!makeUserPasswordMatching(currPassword, entityForUpdate.getPassword(), currentAttribute, model)) {
            return "profile/profile_edit";
        }

        customEntityValidator.validateAndSetErrors(entity, currentAttribute, result, "password");
        if (result.hasErrors()) {
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    Constants.DEFAULT_VALIDATE_ERROR_MESSAGE);
            return "profile/profile_edit";
        }

        handleUpdate(
                () -> userService.updatePassword(id, entity.getPassword()),
                "Password changed successfully",
                "Failed to change password",
                model,
                currentAttribute);

        return "profile/profile_edit";
    }

    static String addModuleNameToModel() {
        return "Profile";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Profile";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_user_profile",
                "email", "upd_user_email",
                "password", "upd_user_pswd");
    }

    private void makeUserIdMatching(Long enteredId) {
        Person authenticatedUser = ((SecurityPersonDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getPerson();
        if (!authenticatedUser.getId().equals(enteredId)) {
            log.debug("Failed to change data for User (ID = {}). ID not matches with authenticated User (ID = {})",
                    enteredId, authenticatedUser.getId());
            throw new EntityNotFoundException("User not found");
        }
    }

    private boolean makeUserPasswordMatching(String enteredPassword, String encodedPassword,
            String currentAttribute, Model model) {
        Person authenticatedUser = ((SecurityPersonDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getPerson();
        if (!passwordEncoder.matches(enteredPassword, encodedPassword)) {
            log.debug("Failed to change data for User (ID = {}). Entered password is not valid",
                    authenticatedUser.getId());
            addMessageToModelAttributes(model, "err_" + currentAttribute,
                    "Entered password is not valid");
            return false;
        }
        return true;
    }

}
