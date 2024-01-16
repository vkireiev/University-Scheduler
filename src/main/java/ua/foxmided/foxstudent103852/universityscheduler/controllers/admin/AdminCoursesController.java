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
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;
import ua.foxmided.foxstudent103852.universityscheduler.util.GenericCreator;

@Controller
@RequestMapping("/admin/")
public class AdminCoursesController extends AbstractAdminEntityController<Course> {
    private static final String URL_KEY = "courses";
    private static final String ENTITY_KEY = "course";

    private final CourseService courseService;

    public AdminCoursesController(CourseService courseService, CustomEntityValidator<Course> customEntityValidator) {
        super(courseService, customEntityValidator, Course.class);
        this.courseService = courseService;
    }

    @Override
    @GetMapping(URL_KEY + "/new")
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, "new_course", "admin/courses/course_new");
    }

    @Override
    @PostMapping(URL_KEY + "/new")
    public ModelAndView newEntity(@ModelAttribute("new_course") @NotNull Course entity,
            BindingResult result, Model model, RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes,
                "new_course", "admin/courses/course_new", "/courses");
    }

    @Override
    @GetMapping(URL_KEY + "/delete/{id}")
    public RedirectView deleteEntity(@PathVariable(name = "id") @NotNull Long id,
            RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, "/courses");
    }

    @Override
    protected Course getGenericInstance() {
        return new GenericCreator<>(Course::new).getInstance();
    }

    @Override
    protected String getNewModuleName() {
        return "Add new Course";
    }

}
