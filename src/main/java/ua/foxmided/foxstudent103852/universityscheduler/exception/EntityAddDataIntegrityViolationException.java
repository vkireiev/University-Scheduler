package ua.foxmided.foxstudent103852.universityscheduler.exception;

public class EntityAddDataIntegrityViolationException extends EntityDataIntegrityViolationException {

    public EntityAddDataIntegrityViolationException(String message) {
        super(message);
    }

    public EntityAddDataIntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
