package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingEntityLifecycleOnServiceAspect extends AbstractServiceMessageLogger {

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.add(..))")
    public void onAdd() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.update(..))")
    public void onUpdate() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.delete(..))")
    public void onDelete() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.deleteById(..))")
    public void onDeleteById() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateCourses(..))")
    public void onUpdateCourses() {
    }

    @Pointcut("execution(public * ua.foxmided.foxstudent103852.universityscheduler.service.*.updateGroups(..))")
    public void onUpdateGroups() {
    }

    @Before("onAdd() && args(entity)")
    public void beforeOnAdd(JoinPoint joinPoint, Object entity) {
        addBeforeLogMessageOnSimpleCrud(joinPoint, entity, "Adding");
    }

    @AfterReturning(pointcut = "onAdd() && args(entity)", returning = "result")
    public void afterReturningOnAdd(JoinPoint joinPoint, Object entity, Object result) {
        addAfterLogMessageOnSimpleCrud(joinPoint, entity, "Adding", result);
    }

    @Before("onUpdate() && args(entity)")
    public void beforeOnUpdate(JoinPoint joinPoint, Object entity) {
        addBeforeLogMessageOnSimpleCrud(joinPoint, entity, "Updating");
    }

    @AfterReturning(pointcut = "onUpdate() && args(entity)", returning = "result")
    public void afterReturningOnUpdate(JoinPoint joinPoint, Object entity, Object result) {
        addAfterLogMessageOnSimpleCrud(joinPoint, entity, "Updating", result);
    }

    @Before("onDelete() && args(entity)")
    public void beforeOnDelete(JoinPoint joinPoint, Object entity) {
        addBeforeLogMessageOnSimpleCrud(joinPoint, entity, "Deleting");
    }

    @AfterReturning(pointcut = "onDelete() && args(entity)", returning = "result")
    public void afterReturningOnDelete(JoinPoint joinPoint, Object entity, Object result) {
        if (entity != null) {
            logAction(LAYER,
                    joinPoint,
                    "Deleting",
                    String.format("%s-entity finished with '%s'", entity.getClass().getSimpleName(), result));
        } else {
            logAction(LAYER,
                    joinPoint,
                    "Deleting",
                    String.format("'null' as entity finished with '%s'", result));
        }
    }

    @Before("onDeleteById() && args(id)")
    public void beforeOnDeleteById(JoinPoint joinPoint, Long id) {
        logAction(LAYER,
                joinPoint,
                "Deleting",
                String.format("entity (ID = %s)", id));
    }

    @AfterReturning(pointcut = "onDeleteById() && args(id)", returning = "result")
    public void afterReturningOnDeleteById(JoinPoint joinPoint, Long id, Object result) {
        logAction(LAYER,
                joinPoint,
                "Deleting",
                String.format("entity (ID = %s) finished with '%s'", id, result));
    }

    @Before("onUpdateCourses() && args(id,..)")
    public void beforeOnUpdateCourses(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Courses-list");
    }

    @AfterReturning(pointcut = "onUpdateCourses() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateCourses(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Courses-list", result);
    }

    @Before("onUpdateGroups() && args(id,..)")
    public void beforeOnUpdateGroups(JoinPoint joinPoint, Long id) {
        addBeforeLogMessageOnUpdatingElement(joinPoint, id, "Groups-list");
    }

    @AfterReturning(pointcut = "onUpdateGroups() && args(id,..)", returning = "result")
    public void afterReturningOnUpdateGroups(JoinPoint joinPoint, Long id, boolean result) {
        addAfterLogMessageOnUpdatingElement(joinPoint, id, "Groups-list", result);
    }

}
