package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingPersonLifecycleOnServiceAspect extends AbstractServiceMessageLogger {

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateEmail(..))")
    public void onUpdateEmail() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updatePassword(..))")
    public void onUpdatePassword() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateLocked(..))")
    public void onUpdateLocked() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateProfile(..))")
    public void onUpdateProfile() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateUserRoles(..))")
    public void onUpdateUserRoles() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateGroup(..))")
    public void onUpdateGroup() {
    }

    @Before("onUpdateEmail() && args(id,..)")
    public void beforeOnUpdateEmail(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Email");
    }

    @AfterReturning(pointcut = "onUpdateEmail() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateEmail(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Email", result);
    }

    @Before("onUpdatePassword() && args(id,..)")
    public void beforeOnUpdatePassword(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Password");
    }

    @AfterReturning(pointcut = "onUpdatePassword() && args(id,..)", returning = "result")
    public void afterReturningOnUpdatePassword(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Password", result);
    }

    @Before("onUpdateLocked() && args(id,..)")
    public void beforeOnUpdateLocked(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Locked-field");
    }

    @AfterReturning(pointcut = "onUpdateLocked() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateLocked(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Locked-field", result);
    }

    @Before("onUpdateProfile() && args(id,..)")
    public void beforeOnUpdateProfile(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Profile");
    }

    @AfterReturning(pointcut = "onUpdateProfile() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateProfile(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Profile", result);
    }

    @Before("onUpdateUserRoles() && args(id,..)")
    public void beforeOnUpdateUserRoles(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Access-roles");
    }

    @AfterReturning(pointcut = "onUpdateUserRoles() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateUserRoles(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Access-roles", result);
    }

    @Before("onUpdateGroup() && args(id,..)")
    public void beforeOnUpdateGroup(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Group");
    }

    @AfterReturning(pointcut = "onUpdateGroup() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateGroup(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Group", result);
    }

}
