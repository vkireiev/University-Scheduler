package ua.foxmided.foxstudent103852.universityscheduler.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.TimeSlot;

@Entity
@Table(name = "lectures", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "employee_id_as_lecturer", "lecture_date", "lecture_time_slot" }),
        @UniqueConstraint(columnNames = { "auditorium_id", "lecture_date", "lecture_time_slot" })
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "groups" })
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "auditorium_id", referencedColumnName = "id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT), nullable = false)
    private Auditorium auditorium;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT), nullable = false)
    private Course course;

    @NotNull
    @ManyToMany
    @JoinTable(name = "lectures_groups", joinColumns = @JoinColumn(name = "lectures_id"), inverseJoinColumns = @JoinColumn(name = "group_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "lectures_id", "group_id" }))
    private Set<Group> groups = new HashSet<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id_as_lecturer", referencedColumnName = "id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))
    private Employee lecturer;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "lecture_date", nullable = false)
    private LocalDate lectureDate;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lecture_time_slot", nullable = false)
    private TimeSlot timeSlot;

    public void addGroup(@NotNull Group group) {
        if (Objects.nonNull(group.getId())) {
            this.groups.add(group);
        }
    }

    public void removeGroup(@NotNull Group group) {
        if (Objects.nonNull(group.getId())) {
            this.groups.remove(group);
        }
    }

}
