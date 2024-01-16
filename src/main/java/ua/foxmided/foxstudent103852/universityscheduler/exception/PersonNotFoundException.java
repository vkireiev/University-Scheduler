package ua.foxmided.foxstudent103852.universityscheduler.exception;

public class PersonNotFoundException extends EntityNotFoundException {

    public PersonNotFoundException() {
        super("Person not found");
    }

    public PersonNotFoundException(String message) {
        super(message);
    }

    public PersonNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
