package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
public interface CrudService<T, U> extends ReadService<T, U> {

    T add(@NotNull @Valid T t);

    List<T> getAll();

    T update(@NotNull @Valid T t);

    boolean delete(@NotNull T t);

    boolean deleteById(@NotNull U id);

}
