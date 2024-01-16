package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.AuditoriumService;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;

@Controller
public class AuditoriumsController extends GenericControllerHelper<Auditorium> {
    private static final int PAGE_LIMIT = 10;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, PAGE_LIMIT, Sort.by("lectureDate").ascending());

    private final AuditoriumService auditoriumService;
    private final LectureService lectureService;

    public AuditoriumsController(AuditoriumService auditoriumService, LectureService lectureService) {
        super(Auditorium.class, auditoriumService);
        this.auditoriumService = auditoriumService;
        this.lectureService = lectureService;
    }

    @IsViewer
    @GetMapping("/auditoriums")
    public String auditoriumsPage(Model model) {
        List<Auditorium> auditoriums = auditoriumService.getAll();
        model.addAttribute("auditoriums", auditoriums);
        model.addAttribute("entitiesCount", auditoriums.size());
        return "auditoriums/auditoriums";
    }

    @IsViewer
    @GetMapping("/auditoriums/view/{id}")
    public String auditoriumViewPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        Auditorium auditoriumEntity = getEntityOrThrowEntityNotFoundException(id);
        model.addAttribute("moduleName", "Auditorium view");
        model.addAttribute("auditorium_view", auditoriumEntity);
        model.addAttribute("upcomingLectures", lectureService
                .findPageByAuditoriumAndLectureDateGreaterThanEqual(auditoriumEntity, LocalDate.now(), PAGE_REQUEST));
        return "auditoriums/auditorium_view";
    }

    static String addModuleNameToModel() {
        return "Auditoriums";
    }

}
