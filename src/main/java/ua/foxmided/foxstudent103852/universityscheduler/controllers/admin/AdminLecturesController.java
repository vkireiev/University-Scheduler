package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;
import ua.foxmided.foxstudent103852.universityscheduler.util.GenericCreator;

@Controller
@RequestMapping("/admin/")
public class AdminLecturesController extends AbstractAdminEntityController<Lecture> {
    private static final String URL_KEY = "lectures";
    private static final String ENTITY_KEY = "lecture";
    private static final Set<TimeSlot> TIME_SLOTS = Set.of(
            TimeSlot.LECTURE0,
            TimeSlot.LECTURE1,
            TimeSlot.LECTURE2,
            TimeSlot.LECTURE3,
            TimeSlot.LECTURE4,
            TimeSlot.LECTURE5,
            TimeSlot.LECTURE6);

    private final LectureService lectureService;
    private final AuditoriumService auditoriumService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final EmployeeService employeeService;

    public AdminLecturesController(LectureService lectureService,
            CustomEntityValidator<Lecture> customEntityValidator,
            AuditoriumService auditoriumService, CourseService courseService,
            GroupService groupService, EmployeeService employeeService) {
        super(lectureService, customEntityValidator, Lecture.class);
        this.lectureService = lectureService;
        this.auditoriumService = auditoriumService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.employeeService = employeeService;
    }

    @IsAdmin
    @GetMapping(URL_KEY + "/new")
    public String newEntityPage(Model model,
            @RequestParam(name = "date", required = false) Optional<LocalDate> date,
            @RequestParam(name = "slot", required = false) Optional<TimeSlot> slot) {
        String currentAttribute = "new_lecture";

        setModelAttributesForNewEntityView(model, getNewModuleName());

        if (!model.containsAttribute(currentAttribute)) {
            Lecture entity = getGenericInstance();
            if (date.isPresent()) {
                entity.setLectureDate(date.get());
            }
            if (slot.isPresent()) {
                entity.setTimeSlot(slot.get());
            }
            model.addAttribute(currentAttribute, entity);
        }

        return "admin/lectures/lecture_new";
    }

    @Override
    @PostMapping(URL_KEY + "/new")
    public ModelAndView newEntity(@ModelAttribute("new_lecture") @NotNull Lecture entity,
            BindingResult result, Model model, RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes,
                "new_lecture", "admin/lectures/lecture_new", "/lectures");
    }

    @Override
    @GetMapping(URL_KEY + "/delete/{id}")
    public RedirectView deleteEntity(@PathVariable(name = "id") @NotNull Long id,
            RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, getPreviousPageByRequestOrHomePage(request));
    }

    @Override
    protected Map<String, Object> setAdditionalModelAttributesForNewEntityView() {
        return Map.of(
                "availableAuditoriums", auditoriumService.findAllByAvailable(true),
                "allCourses", courseService.getAll(),
                "allGroups", groupService.getAll(),
                "allLecturers", employeeService.findAllByEmployeeType(EmployeeType.LECTURER),
                "timeSlots", TIME_SLOTS.stream()
                        .sorted(Comparator.comparing(TimeSlot::ordinal))
                        .toList());
    }

    @Override
    protected Lecture getGenericInstance() {
        return new GenericCreator<>(Lecture::new).getInstance();
    }

    @Override
    protected String getNewModuleName() {
        return "Add new Lecture";
    }

}
