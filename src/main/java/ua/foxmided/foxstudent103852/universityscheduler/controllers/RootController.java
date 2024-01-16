package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RootController implements DefaultControllerAttributes {

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("gMessages", new ArrayList<>(Arrays.asList("test global Message")));
        model.addAttribute("gErrors", new ArrayList<>(Arrays.asList("test global Error")));
        return "index";
    }

    @GetMapping("/forbidden")
    public ModelAndView accessDeniedPage() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("datetime", LocalDateTime.now());
        return new ModelAndView("/errors/access_denied_page",
                modelMap,
                HttpStatus.FORBIDDEN);
    }

    static String addModuleNameToModel() {
        return "Home";
    }

}
