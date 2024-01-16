package ua.foxmided.foxstudent103852.universityscheduler.service.interfaces;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

@Validated
public interface AdminPersonService<T extends Person, U>
        extends EditPersonService<T, U>, GenericPersonService<T, U>, CrudService<T, U> {

    boolean updateLocked(@NotNull Long id, boolean locked);

    boolean updateUserRoles(@NotNull U id, @NotEmpty Set<UserRole> userRoles);

}
