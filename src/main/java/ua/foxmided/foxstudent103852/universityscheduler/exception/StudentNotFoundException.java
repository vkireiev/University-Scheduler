package ua.foxmided.foxstudent103852.universityscheduler.exception;

public class StudentNotFoundException extends PersonNotFoundException {

    public StudentNotFoundException() {
        super("Student not found");
    }

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
