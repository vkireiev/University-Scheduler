package ua.foxmided.foxstudent103852.universityscheduler.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "groups", "lecturers" })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @NotNull
    @ManyToMany(mappedBy = "courses")
    private Set<Employee> lecturers = new HashSet<>();

    @NotNull
    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups = new HashSet<>();

    @PreRemove
    private void preRemoveCourse() {
        if (!this.lecturers.isEmpty() || !this.groups.isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Deleting a " + this.getClass().getSimpleName() + "-entity (ID = " + this.id
                            + ") violates data integrity");
        }
    }

}
