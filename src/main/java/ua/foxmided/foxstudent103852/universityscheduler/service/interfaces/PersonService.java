package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Validated
public interface PersonService extends AbstractGenericPersonService<Person, Long> {

    Optional<Person> get(@NotNull Long id);

    List<Person> findAll();

    long count();

}
