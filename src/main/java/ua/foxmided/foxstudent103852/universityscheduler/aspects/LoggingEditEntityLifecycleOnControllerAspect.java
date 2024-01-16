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

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.Student;

@Aspect
@Component
public class LoggingEditEntityLifecycleOnControllerAspect extends AbstractControllerMessageLogger {

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..editEntityPage(..))")
    public void onEditEntityPage() {
    }

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..changeEntity(..))")
    public void onChangeEntity() {
    }

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..edit*Add*(..))"
            + " || execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..edit*Delete*(..))")
    public void onModifyEntity() {
    }

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..change*Entity(..))")
    public void onChangePersonEntity() {
    }

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..changeGroupStudent(..))")
    public void onChangeGroupStudent() {
    }

    @Before("onEditEntityPage() && args(id,..)")
    public void beforeOnEditEntityPage(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageWithEntityId(joinPoint, id, "Getting data about updating");
    }

    @AfterReturning("onEditEntityPage() && args(id,..)")
    public void afterReturningOnEditEntityPage(JoinPoint joinPoint, Long id) {
        addAfterReturningLogMessageWithEntityId(joinPoint, id, "Getting data about updating");
    }

    @Before("onChangeEntity() && args(id,..)")
    public void beforeOnChangeEntity(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageWithEntityId(joinPoint, id, "Updating");
    }

    @AfterReturning(pointcut = "onChangeEntity() && args(id,entity,result,model)", returning = "returning")
    public void afterReturningOnChangeEntity(JoinPoint joinPoint,
            Long id, Object entity, BindingResult result, Model model, Object returning) {
        addAfterReturningLogMessage(joinPoint, model, null, "Updating", returning);
    }

    @Before("onModifyEntity() && args(entityId,..)")
    public void beforeOnModifyEntity(JoinPoint joinPoint, Long entityId) {
        addBeforeLogMessageWithEntityId(joinPoint, entityId, "Updating");
    }

    @AfterReturning(pointcut = "onModifyEntity() && args(entityId,elementId,attributes,..)", returning = "returning")
    public void afterReturningOnModifyEntity(JoinPoint joinPoint,
            Long entityId, Long elementId, RedirectAttributes attributes, Object returning) {
        addAfterReturningLogMessage(joinPoint, null, attributes, "Updating", returning);
    }

    @Before("onChangePersonEntity() && args(id,person,..)")
    public void beforeOnChangePersonEntity(JoinPoint joinPoint, Long id, Person person) {
        addBeforeLogMessageWithEntity(joinPoint, person, "Updating");
    }

    @AfterReturning(pointcut = "onChangePersonEntity() && args(id,person,result,model)", returning = "returning")
    public void afterReturningOnChangePersonEntity(JoinPoint joinPoint,
            Long id, Person person, BindingResult result, Model model, Object returning) {
        addAfterReturningLogMessage(joinPoint, model, null, "Updating", returning);
    }

    @Before("onChangePersonEntity() && args(id,locked,attributes)")
    public void beforeOnChangePersonEntity(JoinPoint joinPoint,
            Long id, boolean locked, RedirectAttributes attributes) {
        logAction(LAYER,
                joinPoint,
                "Updating isLocked-field",
                String.format("for Person (ID = %s)", id));
    }

    @AfterReturning(pointcut = "onChangePersonEntity() && args(id,locked,attributes)", returning = "returning")
    public void afterReturningOnChangePersonEntity(JoinPoint joinPoint,
            Long id, boolean locked, RedirectAttributes attributes, Object returning) {
        addAfterReturningLogMessage(joinPoint, null, attributes, "Updating", returning);
    }

    @Before("onChangeGroupStudent() && args(id,student,..)")
    public void beforeOnChangeGroupStudent(JoinPoint joinPoint, Long id, Student student) {
        logAction(LAYER,
                joinPoint,
                "Changing Group",
                String.format("for Student (ID = %s)", id));
    }

    @AfterReturning(pointcut = "onChangeGroupStudent() && args(id,student,result,model)", returning = "returning")
    public void afterReturningOnChangeGroupStudent(JoinPoint joinPoint,
            Long id, Student student, BindingResult result, Model model, Object returning) {
        addAfterReturningLogMessage(joinPoint, model, null, "Changing Group for Student", returning);
    }

}
