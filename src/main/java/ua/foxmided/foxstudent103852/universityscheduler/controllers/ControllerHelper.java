package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;

@Log4j2
public abstract class ControllerHelper {

    protected <X> X returnEntityOrThrowEntityNotFoundException(Optional<X> entity,
            String exceptionMessage, String... args) {
        return entity.orElseThrow(() -> {
            String message = (exceptionMessage == null || exceptionMessage.isBlank())
                    ? "Entity not found"
                    : exceptionMessage;
            log.debug(message + Arrays.asList(args).stream()
                    .collect(Collectors.joining(", ", ", ", "")));
            return new EntityNotFoundException(message);
        });
    }

    protected Model addMessageToModelAttributes(Model model, String attributeKey, String message) {
        List<String> attribute = (List<String>) model.getAttribute(attributeKey);
        if (attribute == null) {
            attribute = new ArrayList<>();
            model.addAttribute(attributeKey, attribute);
        }

        attribute.add(message);
        return model;
    }

    protected RedirectAttributes addMessageToFlashAttributes(RedirectAttributes redirectAttributes,
            String attributeKey, String message) {
        List<String> attribute = (List<String>) redirectAttributes.getFlashAttributes().get(attributeKey);
        if (attribute == null) {
            attribute = new ArrayList<>();
            redirectAttributes.addFlashAttribute(attributeKey, attribute);
        }

        attribute.add(message);
        return redirectAttributes;
    }

    protected String getPreviousPageByRequestOrHomePage(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer"))
                .orElse("/");
    }

}
