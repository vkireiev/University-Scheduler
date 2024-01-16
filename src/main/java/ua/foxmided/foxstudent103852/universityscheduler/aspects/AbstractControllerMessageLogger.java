package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public abstract class AbstractControllerMessageLogger extends AbstractMessageLogger {
    protected static final String LAYER = "Controller";

    protected void addBeforeLogMessageWithEntity(JoinPoint joinPoint, Object entity, String action) {
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

    protected void addBeforeLogMessageWithEntityId(JoinPoint joinPoint, Long id, String action) {
        logAction(LAYER,
                joinPoint,
                action,
                String.format("entity (ID = %s)", id));
    }

    protected void addAfterReturningLogMessageWithEntityId(JoinPoint joinPoint, Long id, String action) {
        logAction(LAYER,
                joinPoint,
                action,
                String.format("entity (ID = %s) finished successfully", id));
    }

    protected void addAfterReturningLogMessage(JoinPoint joinPoint, Model model, RedirectAttributes attributes,
            String action, Object returning) {
        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        if (model != null) {
            for (String checkKey : model.asMap().keySet()) {
                if (checkKey.contains("msg_")) {
                    messages.addAll((List<String>) model.getAttribute(checkKey));
                }
                if (checkKey.contains("err_")) {
                    errors.addAll((List<String>) model.getAttribute(checkKey));
                }
            }
            if (model.containsAttribute("gMessages")) {
                messages.addAll((List<String>) model.getAttribute("gMessages"));
            }
            if (model.containsAttribute("gErrors")) {
                errors.addAll((List<String>) model.getAttribute("gErrors"));
            }
        }

        if (attributes != null) {
            for (String checkKey : attributes.getFlashAttributes().keySet()) {
                if (checkKey.contains("msg_")) {
                    messages.addAll((List<String>) attributes.getFlashAttributes().get(checkKey));
                }
                if (checkKey.contains("err_")) {
                    errors.addAll((List<String>) attributes.getFlashAttributes().get(checkKey));
                }
            }
            if (attributes.getFlashAttributes().containsKey("gMessages")) {
                messages.addAll((List<String>) attributes.getFlashAttributes().get("gMessages"));
            }
            if (attributes.getFlashAttributes().containsKey("gErrors")) {
                errors.addAll((List<String>) attributes.getFlashAttributes().get("gErrors"));
            }
        }

        logAction(LAYER,
                joinPoint,
                action,
                String.format("entity process finished with messages [msg=%s, err=%s]",
                        messages.stream().collect(Collectors.joining(", ", "[", "]")),
                        errors.stream().collect(Collectors.joining(", ", "[", "]"))));
    }

}
