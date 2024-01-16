package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Course;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.CourseService;

@Controller
public class CoursesController extends GenericControllerHelper<Course> {

    private final CourseService courseService;

    public CoursesController(CourseService courseService) {
        super(Course.class, courseService);
        this.courseService = courseService;
    }

    @IsViewer
    @GetMapping("/courses")
    public String coursesPage(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        model.addAttribute("entitiesCount", courses.size());
        return "courses/courses";
    }

    @IsViewer
    @GetMapping("/courses/view/{id}")
    public String courseViewPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        Course courseEntity = getEntityOrThrowEntityNotFoundException(id);
        model.addAttribute("moduleName", "Course view");
        model.addAttribute("course_view", courseEntity);
        return "courses/course_view";
    }

    static String addModuleNameToModel() {
        return "Courses";
    }

}
