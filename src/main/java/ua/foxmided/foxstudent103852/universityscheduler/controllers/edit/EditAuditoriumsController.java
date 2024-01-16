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
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Controller
public class EditAuditoriumsController extends AbstractEditEntityController<Auditorium> {
    private static final String URL_KEY = "auditoriums";
    private static final String ENTITY_KEY = "auditorium";

    private final AuditoriumService auditoriumService;

    protected EditAuditoriumsController(AuditoriumService auditoriumService,
            CustomEntityValidator<Auditorium> customEntityValidator) {
        super(auditoriumService, customEntityValidator, Auditorium.class);
        this.auditoriumService = auditoriumService;
    }

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "auditoriums/auditorium_edit");
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}")
    public ModelAndView changeEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_auditorium_profile") @NotNull Auditorium entity, BindingResult result, Model model) {
        return changeEntityImpl(id, entity, result, model,
                "upd_auditorium_profile", "auditoriums/auditorium_edit", "auditoriums/auditorium_edit");
    }

    static String addModuleName() {
        return "Auditoriums";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Auditorium";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_auditorium_profile");
    }

}
