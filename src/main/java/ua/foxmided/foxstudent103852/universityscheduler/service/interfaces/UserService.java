package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import org.springframework.validation.annotation.Validated;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;

@Validated
public interface UserService extends EditPersonService<Person, Long> {

}
