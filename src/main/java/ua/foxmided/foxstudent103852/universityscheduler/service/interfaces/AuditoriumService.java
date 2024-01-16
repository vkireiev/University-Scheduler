package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.List;

import ua.foxmided.foxstudent103852.universityscheduler.model.Auditorium;

public interface AuditoriumService extends CrudService<Auditorium, Long> {

    long count();

    List<Auditorium> findAllByAvailable(boolean available);

}
