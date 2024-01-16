package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public abstract class AbstractMessageLogger {

    protected void logAction(String layer, JoinPoint joinPoint, String action, Object... params) {
        String message = buildLogMessage(layer, joinPoint, action, params);
        log.debug(message);
    }

    private String buildLogMessage(String layer, JoinPoint joinPoint, String action, Object... params) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("[" + String.format("%15s", layer) + "] ")
                .append(action);

        for (Object param : params) {
            messageBuilder.append(" ").append(param);
        }

        messageBuilder.append(". Called from [")
                .append(joinPoint.getSignature().getDeclaringType().getSimpleName())
                .append(".")
                .append(joinPoint.getSignature().getName())
                .append("(...)]");

        return messageBuilder.toString();
    }

}
