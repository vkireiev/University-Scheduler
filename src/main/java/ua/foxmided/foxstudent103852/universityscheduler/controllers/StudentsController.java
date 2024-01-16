package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.StudentService;

@Controller
@RequiredArgsConstructor
public class StudentsController implements DefaultControllerAttributes {

    private final StudentService studentService;

    @IsViewer
    @GetMapping("/students")
    public String studentsPage(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("entitiesCount", students.size());
        model.addAttribute("studentComparator",
                Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER));
        return "students/students";
    }

    static String addModuleNameToModel() {
        return "Students";
    }

}
