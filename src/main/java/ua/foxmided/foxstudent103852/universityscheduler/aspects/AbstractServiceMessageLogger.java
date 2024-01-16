package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractServiceMessageLogger extends AbstractMessageLogger {
    protected static final String LAYER = "Service";

    protected void addBeforeLogMessageOnSimpleCrud(JoinPoint joinPoint, Object entity, String action) {
        if (entity != null) {
            logAction(LAYER,
                    joinPoint,
                    action,
                    String.format("%s-entity: %s", entity.getClass().getSimpleName(), entity));
        } else {
            logAction(LAYER,
                    joinPoint,
                    action,
                    "'null' as entity");
        }
    }

    protected void addAfterLogMessageOnSimpleCrud(JoinPoint joinPoint, Object entity, String action, Object result) {
        if (result != null) {
            logAction(LAYER,
                    joinPoint,
                    action,
                    String.format("%s-entity finished successfully", result.getClass().getSimpleName()));
        } else {
            logAction(LAYER,
                    joinPoint,
                    action,
                    String.format("%s-entity returned 'null'", entity.getClass().getSimpleName()));
        }
    }

    protected void addBeforeLogMessageOnUpdatingElement(JoinPoint joinPoint, Long id, String action) {
        logAction(LAYER,
                joinPoint,
                action,
                String.format("updating for entity (ID = %s)", id));
    }

    protected void addAfterLogMessageOnUpdatingElement(JoinPoint joinPoint, Long id, String action, Object result) {
        logAction(LAYER,
                joinPoint,
                action,
                String.format("updating for entity (ID = %s) finished with '%s'", id, result));
    }

}
