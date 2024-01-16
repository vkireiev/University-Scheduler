package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.annotations.IsViewer;
import ua.foxmided.foxstudent103852.universityscheduler.model.Group;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.GroupService;

@Controller
public class GroupsController extends GenericControllerHelper<Group> {

    private final GroupService groupService;

    public GroupsController(GroupService groupService) {
        super(Group.class, groupService);
        this.groupService = groupService;
    }

    @IsViewer
    @GetMapping("/groups")
    public String groupsPage(Model model) {
        List<Group> groups = groupService.getAll();
        model.addAttribute("groups", groups);
        model.addAttribute("entitiesCount", groups.size());
        return "groups/groups";
    }

    @IsViewer
    @GetMapping("/groups/view/{id}")
    public String groupViewPage(@PathVariable(name = "id") @NotNull Long id, Model model) {
        Group groupEntity = getEntityOrThrowEntityNotFoundException(id);
        model.addAttribute("moduleName", "Group view");
        model.addAttribute("group_view", groupEntity);
        return "groups/group_view";
    }

    static String addModuleNameToModel() {
        return "Groups";
    }

}
