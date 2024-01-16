package ua.foxmided.foxstudent103852.universityscheduler.exception;

public class EmployeeNotFoundException extends PersonNotFoundException {

    public EmployeeNotFoundException() {
        super("Employee not found");
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
