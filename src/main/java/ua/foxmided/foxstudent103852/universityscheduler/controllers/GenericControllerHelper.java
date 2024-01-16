package ua.foxmided.foxstudent103852.universityscheduler.controllers;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.EntityNotFoundException;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.ReadService;

@RequiredArgsConstructor
public abstract class GenericControllerHelper<T> extends ControllerHelper
        implements DefaultControllerAttributes {

    protected final Class<T> genericType;
    private final ReadService<T, Long> entityService;

    protected T getEntityOrThrowEntityNotFoundException(Long id) {
        if (id != null) {
            return returnEntityOrThrowEntityNotFoundException(entityService.get(id),
                    genericType.getSimpleName() + " not found", "ID = " + id);
        }
        throw new EntityNotFoundException(genericType.getSimpleName() + " not found");
    }

}
