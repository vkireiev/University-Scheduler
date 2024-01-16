package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;

public interface DefaultControllerAttributes {

    @ModelAttribute("applicationName")
    static String addApplicationNameToModel() {
        return "University-Scheduler";
    }

    @ModelAttribute("moduleName")
    static String addModuleNameToModel() {
        return "default ModuleName";
    }

}
