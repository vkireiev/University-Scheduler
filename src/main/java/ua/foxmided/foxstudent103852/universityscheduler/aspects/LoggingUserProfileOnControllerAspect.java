package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Aspect
@Component
public class LoggingUserProfileOnControllerAspect extends AbstractControllerMessageLogger {

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..userProfilePage(..))")
    public void onUserProfilePage() {
    }

    @Pointcut("execution(* ua.foxmided.foxstudent103852.universityscheduler.controllers..changeUser*(..))")
    public void onChangeUser() {
    }

    @Before("onUserProfilePage() && args(model)")
    public void beforeOnUserProfilePage(JoinPoint joinPoint, Model model) {
        logAction(LAYER,
                joinPoint,
                "Getting authenticated User-profile data");
    }

    @AfterReturning(pointcut = "onUserProfilePage() && args(model)", returning = "returning")
    public void afterReturningOnUserProfilePage(JoinPoint joinPoint, Model model, Object returning) {
        logAction(LAYER,
                joinPoint,
                "Getting authenticated User-profile data process finished successfully");
    }

    @Before("onChangeUser() && args(id,entity,..)")
    public void beforeOnChangeUser(JoinPoint joinPoint, Long id, Person entity) {
        addBeforeLogMessageOnChangeUser(joinPoint, id, entity, "Changing Profile/Password/Email/Roles");
    }

    @AfterReturning(pointcut = "onChangeUser() && args(id,entity,result,changeField,model)", returning = "returning")
    public void afterReturningOnChangeUser(JoinPoint joinPoint,
            Long id, Person entity, BindingResult result, Object changeField, Model model, Object returning) {
        addAfterReturningLogMessage(joinPoint, model, null, "Changing Profile/Password/Email/Roles", returning);
    }

    private void addBeforeLogMessageOnChangeUser(JoinPoint joinPoint, Long id, Object entity, String action) {
        if (entity != null) {
            logAction(LAYER,
                    joinPoint,
                    action,
                    String.format("for User (ID = %s)", id));
        } else {
            logAction(LAYER,
                    joinPoint,
                    action,
                    "'null' as User-entity");
        }
    }

}
