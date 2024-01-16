package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Validated
public interface EditPersonService<T extends Person, U> extends GenericPersonService<T, U> {

    boolean updateEmail(@NotNull U id, @NotBlank String email);

    boolean updatePassword(@NotNull U id, @NotBlank String password);

    boolean updateProfile(@NotNull U id, @NotNull T t);

}
