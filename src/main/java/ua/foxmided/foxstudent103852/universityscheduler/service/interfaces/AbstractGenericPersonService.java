package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Validated
public interface AbstractGenericPersonService<T extends Person, U> {

    boolean existsById(@NotNull U id);

    boolean existsByUsername(@NotBlank String username);

}
