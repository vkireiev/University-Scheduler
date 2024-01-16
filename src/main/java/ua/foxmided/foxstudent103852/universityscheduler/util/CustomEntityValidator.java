package ua.foxmided.foxstudent103852.universityscheduler.util;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomEntityValidator<T> {
    private final Validator validator;

    public void validateAndSetErrors(T entity, String attribute, BindingResult result) {
        Set<ConstraintViolation<T>> violations = this.validator.validate(entity);
        setErrors(attribute, result, violations);
    }

    public void validateAndSetErrors(T entity, String attribute, BindingResult result, String... fields) {
        for (String field : fields) {
            Set<ConstraintViolation<T>> violations = this.validator.validateProperty(entity, field);
            setErrors(attribute, result, violations);
        }
    }

    private void setErrors(String attribute, BindingResult result, Set<ConstraintViolation<T>> violations) {
        for (ConstraintViolation<T> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            result.addError(new FieldError(attribute, propertyPath, message));
        }
    }

}
