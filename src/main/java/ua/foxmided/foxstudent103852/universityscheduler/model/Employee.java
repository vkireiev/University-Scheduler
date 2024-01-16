package ua.foxmided.foxstudent103852.universityscheduler.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.EmployeeType;

@Entity
@Table(name = "employees")
@DiscriminatorValue("2")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = { "courses" })
public class Employee extends Person {

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "employee_type", nullable = false)
    private EmployeeType employeeType = EmployeeType.EMPLOYEE;

    @NotNull
    @ManyToMany
    @JoinTable(name = "employees_courses", joinColumns = @JoinColumn(name = "employee_id_as_lecturer"), inverseJoinColumns = @JoinColumn(name = "course_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "employee_id_as_lecturer", "course_id" }))
    private Set<Course> courses = new HashSet<>();

    @PreRemove
    private void preRemoveEmployee() {
        if (!this.courses.isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Deleting a " + this.getClass().getSimpleName() + "-entity (ID = " + this.getId()
                            + ") violates data integrity");
        }
    }

    public void addCourse(@NotNull Course course) {
        if (Objects.nonNull(course.getId())) {
            course.getLecturers().add(this);
            this.courses.add(course);
        }
    }

    public void removeCourse(@NotNull Course course) {
        if (Objects.nonNull(course.getId())) {
            course.getLecturers().remove(this);
            this.courses.remove(course);
        }
    }

}
