package ua.foxmided.foxstudent103852.universityscheduler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "auditoriums", uniqueConstraints = @UniqueConstraint(columnNames = { "number", "available" }))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 10)
    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @NotNull
    @Min(value = 10)
    @Max(value = 300)
    @Column(name = "capacity", nullable = false)
    private Short capacity;

    @NotNull
    @Column(name = "available", nullable = false)
    private boolean available = true;

}
