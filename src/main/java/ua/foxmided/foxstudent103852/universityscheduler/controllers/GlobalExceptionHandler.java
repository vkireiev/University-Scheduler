package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityDataIntegrityViolationException;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.util.Constants;

@ControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(Exception ex) {
        log.debug(ex.getMessage(), ex);
        return new ModelAndView("errors/blank_page",
                createErrorModel("Error", ex.getMessage()));
    }

    @ExceptionHandler(EntityDataIntegrityViolationException.class)
    public RedirectView handleEntityDataIntegrityViolationException(Exception ex,
            RedirectAttributes attributes, HttpServletRequest request) {
        log.debug(ex.getMessage(), ex);
        attributes.addFlashAttribute("gErrors",
                new ArrayList<>(Arrays.asList(ex.getMessage())));
        return new RedirectView(Optional.ofNullable(request.getHeader("Referer")).orElse("/"));
    }

    @ExceptionHandler(value = { AccessDeniedException.class, AuthenticationException.class })
    public ModelAndView handleAccessDeniedAndAuthenticationExceptions(Exception ex) {
        log.debug(ex.getMessage(), ex);
        return new ModelAndView("errors/access_denied_page",
                createErrorModel("Forbidden", Constants.DEFAULT_ERROR_MESSAGE),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ DataProcessingException.class, ConstraintViolationException.class })
    public ModelAndView handleUnexpectedException(Exception ex) {
        log.debug(ex.getMessage(), ex);
        return new ModelAndView("errors/blank_page",
                createErrorModel("Error", Constants.DEFAULT_ERROR_MESSAGE),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorModel(String applicationName, String errorMessage) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("applicationName", applicationName);
        modelMap.put("datetime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        modelMap.put("gErrors", new ArrayList<>(Arrays.asList(errorMessage)));
        return modelMap;
    }

}
