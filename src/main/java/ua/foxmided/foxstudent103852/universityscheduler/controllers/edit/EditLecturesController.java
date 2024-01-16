package ua.foxmided.foxstudent103852.universityscheduler.controllers.edit;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsEditor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityUpdateDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;

@Log4j2
@Controller
public class EditLecturesController extends AbstractEditEntityController<Lecture> {
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

    public EditLecturesController(LectureService lectureService,
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

    @Override
    @GetMapping(URL_KEY + "/edit/{id}")
    public ModelAndView editEntityPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        return editEntityPageImpl(id, model, "lectures/lecture_edit");
    }

    @Override
    @PostMapping(URL_KEY + "/edit/{id}")
    public ModelAndView changeEntity(@PathVariable(name = "id") @NotNull Long id,
            @ModelAttribute("upd_lecture_profile") @NotNull Lecture entity, BindingResult result, Model model) {
        return changeEntityImpl(id, entity, result, model,
                "upd_lecture_profile", "lectures/lecture_edit", "lectures/lecture_edit");
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_lecture}/groups/delete/{id_group}")
    public RedirectView editLectureDeleteGroup(
            @PathVariable(name = "id_lecture") @NotNull Long entityId,
            @PathVariable(name = "id_group") @NotNull Long groupId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyLectureGroups(entityId, groupId,
                getAttributesForEditView().get("groups"), attributes, request, false);
    }

    @IsEditor
    @GetMapping(URL_KEY + "/edit/{id_lecture}/groups/add/{id_group}")
    public RedirectView editLectureAddGroup(
            @PathVariable(name = "id_lecture") @NotNull Long entityId,
            @PathVariable(name = "id_group") @NotNull Long groupId,
            RedirectAttributes attributes, HttpServletRequest request) {
        return modifyLectureGroups(entityId, groupId,
                getAttributesForEditView().get("groups"), attributes, request, true);
    }

    @Override
    protected Lecture setRequiredFieldsBeforeUpdateEntity(Lecture entity, Lecture source) {
        entity.setGroups(new HashSet<>(source.getGroups()));
        return entity;
    }

    @Override
    protected @NotNull Map<@NotNull String, @NotNull Object> setAdditionalModelAttributesForEditEntityView(
            Lecture entity) {
        return Map.of(
                "availableAuditoriums", auditoriumService.findAllByAvailable(true),
                "allCourses", courseService.getAll(),
                "addGroupsList", groupService.getAll().stream()
                        .filter(group -> !entity.getGroups().contains(group))
                        .toList(),
                "allLecturers", employeeService.findAllByEmployeeType(EmployeeType.LECTURER),
                "timeSlots", TIME_SLOTS.stream()
                        .sorted(Comparator.comparing(TimeSlot::ordinal))
                        .toList());
    }

    static String addModuleNameToModel() {
        return "My Schedule";
    }

    @Override
    protected String getEditModuleName() {
        return "Edit Lecture";
    }

    @Override
    protected String getControllerUrlKey() {
        return URL_KEY;
    }

    @Override
    protected Map<String, String> getAttributesForEditView() {
        return Map.of(
                "profile", "upd_lecture_profile",
                "groups", "upd_lecture_groups");
    }

    private RedirectView modifyLectureGroups(Long lectureId, Long groupId,
            String currentAttribute, RedirectAttributes attributes, HttpServletRequest request, boolean isAssign) {
        Lecture lecture = getEntityOrThrowEntityNotFoundException(lectureId);
        Group group = returnEntityOrThrowEntityNotFoundException(groupService.get(groupId),
                "Group not found", "ID = " + groupId);

        boolean elementAlreadyAdded = lecture.getGroups().contains(group);

        if (isAssign == elementAlreadyAdded) {
            String error = isAssign
                    ? "Trying to add a Group that is already assigned to a Lecture"
                    : "Trying to remove a Group not assigned to a Lecture";
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute, error);
            return new RedirectView(getRedirectEditEntityViewUrl(request, lectureId));
        }

        if (isAssign) {
            lecture.addGroup(group);
        } else {
            lecture.removeGroup(group);
        }

        try {
            updateLectureGroups(lecture, isAssign, currentAttribute, attributes);
        } catch (EntityUpdateDataIntegrityViolationException e) {
            addMessageToFlashAttributes(attributes, "err_" + currentAttribute,
                    e.getMessage());
            log.debug(e.getMessage());
        }
        return new RedirectView(getRedirectEditEntityViewUrl(request, lectureId));

    }

    private void updateLectureGroups(Lecture lecture, boolean isAssign,
            String currentAttribute, RedirectAttributes attributes) {
        handleUpdate(
                () -> lectureService.updateGroups(lecture.getId(), lecture.getGroups()),
                "Group has been successfully " + (isAssign ? "assigned to" : "unassigned from") + " the Lecture",
                "Failed to " + (isAssign ? "assign" : "unassign") + " Group "
                        + (isAssign ? "to" : "for") + " the Lecture",
                attributes,
                currentAttribute);
    }

}
