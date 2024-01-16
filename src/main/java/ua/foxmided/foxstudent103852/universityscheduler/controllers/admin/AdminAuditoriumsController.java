package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;
import ua.foxmided.foxstudent103852.universityscheduler.util.GenericCreator;

@Controller
@RequestMapping("/admin/")
public class AdminAuditoriumsController extends AbstractAdminEntityController<Auditorium> {
    private static final String URL_KEY = "auditoriums";
    private static final String ENTITY_KEY = "auditorium";

    private final AuditoriumService auditoriumService;

    protected AdminAuditoriumsController(AuditoriumService auditoriumService,
            CustomEntityValidator<Auditorium> customEntityValidator) {
        super(auditoriumService, customEntityValidator, Auditorium.class);
        this.auditoriumService = auditoriumService;
    }

    @Override
    @GetMapping(URL_KEY + "/new")
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, "new_auditorium", "admin/auditoriums/auditorium_new");
    }

    @Override
    @PostMapping(URL_KEY + "/new")
    public ModelAndView newEntity(@ModelAttribute("new_auditorium") @NotNull Auditorium entity,
            BindingResult result, Model model, RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes,
                "new_auditorium", "admin/auditoriums/auditorium_new", "/auditoriums");
    }

    @Override
    @GetMapping(URL_KEY + "/delete/{id}")
    public RedirectView deleteEntity(@PathVariable(name = "id") @NotNull Long id,
            RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, "/auditoriums");
    }

    @Override
    protected Auditorium getGenericInstance() {
        return new GenericCreator<>(Auditorium::new).getInstance();
    }

    @Override
    protected String getNewModuleName() {
        return "Add new Auditorium";
    }

}
