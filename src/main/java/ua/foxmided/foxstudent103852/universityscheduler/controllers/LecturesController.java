package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.Lecture;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;
import ua.foxmided.foxstudent103852.universityscheduler.security.SecurityPersonDetails;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.LectureService;

@Controller
public class LecturesController extends GenericControllerHelper<Lecture> {
    private static final long MAX_SEARCH_PERIOD_IN_DAYS = 30;

    private final LectureService lectureService;

    public LecturesController(LectureService lectureService) {
        super(Lecture.class, lectureService);
        this.lectureService = lectureService;
    }

    @IsViewer
    @GetMapping("/lectures")
    public String lecturesPage(Model model,
            @RequestParam(name = "date1", required = false) Optional<LocalDate> date1,
            @RequestParam(name = "date2", required = false) Optional<LocalDate> date2,
            @RequestParam(name = "show_all", required = false) Optional<Boolean> showAll) {
        LocalDate startDate = (date1.isEmpty()) ? LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1) : date1.get();
        LocalDate endDate = (date2.isEmpty()) ? LocalDate.now().with(ChronoField.DAY_OF_WEEK, 7) : date2.get();
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_SEARCH_PERIOD_IN_DAYS) {
            endDate = startDate.plusDays(MAX_SEARCH_PERIOD_IN_DAYS);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person authenticatedUser = ((SecurityPersonDetails) authentication.getPrincipal()).getPerson();
        boolean grantedToGetAll = authentication.getAuthorities().contains(new SimpleGrantedAuthority("EDITOR"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
        boolean isShowAll = grantedToGetAll;
        if (grantedToGetAll && showAll.isPresent()) {
            isShowAll = showAll.get();
        }
        List<Lecture> lectures = new ArrayList<>();
        if (isShowAll) {
            lectures = lectureService.findAllByLectureDateBetween(startDate, endDate);
        } else {
            if (authenticatedUser instanceof Student user) {
                lectures = lectureService
                        .findAllByGroupsContainingAndLectureDateBetween(user.getGroup(), startDate, endDate);
            }
            if (authenticatedUser instanceof Employee user) {
                lectures = lectureService
                        .findAllByLecturerAndLectureDateBetween(user, startDate, endDate);
            }
        }
        Map<LocalDate, Map<TimeSlot, Set<Lecture>>> scheduledLectures = getScheduledLectures(lectures);
        Map<Integer, Map<Integer, Map<DayOfWeek, LocalDate>>> calendar = getCalendar(startDate, endDate);

        model.addAttribute("date1", startDate);
        model.addAttribute("date2", endDate);
        model.addAttribute("showAll", isShowAll);
        model.addAttribute("calendar", calendar);
        model.addAttribute("scheduledLectures", scheduledLectures);
        model.addAttribute("entitiesCount", lectures.size());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("timeSlots", TimeSlot.values());

        return "lectures/lectures";
    }

    @IsViewer
    @GetMapping("/lectures/view/{id}")
    public String lectureViewPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        Lecture lectureEntity = getEntityOrThrowEntityNotFoundException(id);
        model.addAttribute("moduleName", "Lecture view");
        model.addAttribute("lecture_view", lectureEntity);
        return "lectures/lecture_view";
    }

    private Map<LocalDate, Map<TimeSlot, Set<Lecture>>> getScheduledLectures(List<Lecture> lectures) {
        Map<LocalDate, Map<TimeSlot, Set<Lecture>>> scheduledLectures = new HashMap<>();

        for (Lecture lecture : lectures) {
            if (!scheduledLectures.containsKey(lecture.getLectureDate())) {
                scheduledLectures.put(lecture.getLectureDate(), new HashMap<>());
            }
            Map<TimeSlot, Set<Lecture>> scheduledDay = scheduledLectures.get(lecture.getLectureDate());
            if (!scheduledDay.containsKey(lecture.getTimeSlot())) {
                scheduledDay.put(lecture.getTimeSlot(), new HashSet<>());
            }
            Set<Lecture> scheduledTimeSlot = scheduledDay.get(lecture.getTimeSlot());
            scheduledTimeSlot.add(lecture);
        }

        return scheduledLectures;
    }

    private Map<Integer, Map<Integer, Map<DayOfWeek, LocalDate>>> getCalendar(
            LocalDate startDate, LocalDate endDate) {
        Map<Integer, Map<Integer, Map<DayOfWeek, LocalDate>>> calendar = new TreeMap<>();
        LocalDate startFirstWeek = startDate;
        LocalDate endLastWeek = endDate;

        startFirstWeek = startFirstWeek.with(ChronoField.DAY_OF_WEEK, 1);
        endLastWeek = endLastWeek.with(ChronoField.DAY_OF_WEEK, 7);
        LocalDate calendarDate = startFirstWeek;
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        while (calendarDate.compareTo(endLastWeek) <= 0) {
            if (!calendar.containsKey(calendarDate.getYear())) {
                calendar.put(calendarDate.getYear(), new TreeMap<>());
            }
            Map<Integer, Map<DayOfWeek, LocalDate>> year = calendar
                    .get(calendarDate.getYear());
            if (!year.containsKey(calendarDate.get(weekFields.weekOfYear()))) {
                year.put(calendarDate.get(weekFields.weekOfYear()), new TreeMap<>());
            }
            Map<DayOfWeek, LocalDate> weekOfYear = year
                    .get(calendarDate.get(weekFields.weekOfYear()));
            weekOfYear.put(calendarDate.getDayOfWeek(), calendarDate);

            calendarDate = calendarDate.plusDays(1);
        }

        return calendar;
    }

    static String addModuleNameToModel() {
        return "My Schedule";
    }

}
