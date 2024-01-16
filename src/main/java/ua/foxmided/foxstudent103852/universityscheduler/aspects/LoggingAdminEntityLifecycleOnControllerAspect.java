package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAdminEntityLifecycleOnControllerAspect extends AbstractControllerMessageLogger {

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.controllers..newEntity(..))")
    public void onCreateEntity() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.controllers..deleteEntity(..))")
    public void onDeleteEntity() {
    }

    @Before("onCreateEntity() && args(entity,..)")
    public void beforeOnCreateEntity(JoinPoint joinPoint, Object entity) {
        addBeforeLogMessageWithEntity(joinPoint, entity, "Adding");
    }

    @AfterReturning(pointcut = "onCreateEntity() && args(entity, result, model, attributes)", returning = "returning")
    public void afterReturningOnCreateEntity(JoinPoint joinPoint, Object entity,
            BindingResult result, Model model, RedirectAttributes attributes, Object returning) {
        addAfterReturningLogMessage(joinPoint, model, attributes, "Adding", returning);
    }

    @Before("onDeleteEntity() && args(id,..)")
    public void beforeOnDeleteEntity(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageWithEntityId(joinPoint, id, "Deleting");
    }

    @AfterReturning(pointcut = "onDeleteEntity() && args(id, attributes, request)", returning = "returning")
    public void afterReturningOnDeleteEntity(JoinPoint joinPoint, Long id,
            RedirectAttributes attributes, HttpServletRequest request, Object returning) {
        addAfterReturningLogMessage(joinPoint, null, attributes, "Deleting", returning);
    }

}
