package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Employee;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.EmployeeService;

@Controller
@RequiredArgsConstructor
public class LecturersController implements DefaultControllerAttributes {
    private final EmployeeService employeeService;

    @IsViewer
    @GetMapping("/lecturers")
    public String lecturersPage(Model model) {
        List<Employee> lecturers = employeeService.findAllByEmployeeType(EmployeeType.LECTURER);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("entitiesCount", lecturers.size());
        model.addAttribute("employeeComparator",
                Comparator.comparing(Employee::getLastName, String.CASE_INSENSITIVE_ORDER));
        return "lecturers/lecturers";
    }

    static String addModuleNameToModel() {
        return "Lecturers";
    }

}
