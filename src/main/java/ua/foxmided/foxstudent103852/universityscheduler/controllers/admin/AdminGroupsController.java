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
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.universityscheduler.util.CustomEntityValidator;
import ua.foxmided.foxstudent103852.universityscheduler.util.GenericCreator;

@Controller
@RequestMapping("/admin/")
public class AdminGroupsController extends AbstractAdminEntityController<Group> {
    private static final String URL_KEY = "groups";
    private static final String ENTITY_KEY = "group";

    private final GroupService groupService;

    protected AdminGroupsController(GroupService groupService, CustomEntityValidator<Group> customEntityValidator) {
        super(groupService, customEntityValidator, Group.class);
        this.groupService = groupService;
    }

    @Override
    @GetMapping(URL_KEY + "/new")
    public String newEntityPage(Model model) {
        return newEntityPageImpl(model, "new_group", "admin/groups/group_new");
    }

    @Override
    @PostMapping(URL_KEY + "/new")
    public ModelAndView newEntity(@ModelAttribute("new_group") @NotNull Group entity,
            BindingResult result, Model model, RedirectAttributes attributes) {
        return newEntityImpl(entity, result, model, attributes,
                "new_group", "admin/groups/group_new", "/groups");
    }

    @Override
    @GetMapping(URL_KEY + "/delete/{id}")
    public RedirectView deleteEntity(@PathVariable(name = "id") @NotNull Long id,
            RedirectAttributes attributes, HttpServletRequest request) {
        return deleteEntityImpl(id, attributes, "/groups");
    }

    @Override
    protected Group getGenericInstance() {
        return new GenericCreator<>(Group::new).getInstance();
    }

    @Override
    protected String getNewModuleName() {
        return "Add new Group";
    }

}
