package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import org.springframework.validation.annotation.Validated;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Validated
public interface GenericPersonService<T extends Person, U>
        extends AbstractGenericPersonService<T, U>, ReadService<T, U> {

}
