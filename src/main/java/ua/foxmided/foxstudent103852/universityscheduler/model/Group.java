package ua.foxmided.foxstudent103852.universityscheduler.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "students", "courses" })
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 8, max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 30)
    @Column(name = "capacity", nullable = false)
    private Short capacity;

    @NotNull
    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @NotNull
    @ManyToMany
    @JoinTable(name = "groups_courses", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "course_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "group_id", "course_id" }))
    private Set<Course> courses = new HashSet<>();

    @PreRemove
    private void preRemoveGroup() {
        if (!this.students.isEmpty() || !this.courses.isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Deleting a " + this.getClass().getSimpleName() + "-entity (ID = " + this.id
                            + ") violates data integrity");
        }
    }

    public void addStudent(@NotNull Student student) {
        if (Objects.nonNull(student.getId())) {
            student.setGroup(this);
            this.students.add(student);
        }
    }

    public void removeStudent(@NotNull Student student) {
        if (Objects.nonNull(student.getId())) {
            student.setGroup(null);
            this.students.remove(student);
        }
    }

    public void addCourse(@NotNull Course course) {
        if (Objects.nonNull(course.getId())) {
            course.getGroups().add(this);
            this.courses.add(course);
        }
    }

    public void removeCourse(@NotNull Course course) {
        if (Objects.nonNull(course.getId())) {
            course.getGroups().remove(this);
            this.courses.remove(course);
        }
    }

}
