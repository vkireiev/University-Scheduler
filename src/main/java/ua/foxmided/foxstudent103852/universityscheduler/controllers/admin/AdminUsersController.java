package ua.foxmided.foxstudent103852.universityscheduler.controllers.admin;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsAdmin;
import ua.foxmided.foxstudent103852.universityscheduler.controllers.DefaultControllerAttributes;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;

@Controller
@RequiredArgsConstructor
public class AdminUsersController implements DefaultControllerAttributes {
    private final PersonService personService;

    @IsAdmin
    @GetMapping("/admin/users")
    public String usersPage(Model model) {
        List<Person> users = personService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("entitiesCount", users.size());
        model.addAttribute("userComparator",
                Comparator.comparing(Person::getLastName, String.CASE_INSENSITIVE_ORDER));
        return "admin/users/users";
    }

    static String addModuleNameToModel() {
        return "Users";
    }

}
