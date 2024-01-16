package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Validated
public interface ReadService<T, U> {

    Optional<T> get(@NotNull U id);

}
