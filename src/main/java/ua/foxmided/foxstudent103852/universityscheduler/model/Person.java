package ua.foxmided.foxstudent103852.universityscheduler.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }))
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.INTEGER)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id", "username", "firstName", "lastName", "userRoles", "locked" })
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(min = 8, max = 50)
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotBlank
    @Email
    @Size(min = 5, max = 75)
    @Column(name = "email", nullable = false, length = 75)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "firstname", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "lastname", nullable = false, length = 100)
    private String lastName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @NotBlank
    @Size(min = 3, max = 16)
    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "persons_user_roles")
    @Column(name = "user_role", nullable = false)
    private Set<UserRole> userRoles = new HashSet<>(Arrays.asList(UserRole.VIEWER));

    @NotNull
    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @NotNull
    @Column(name = "locked", nullable = false)
    private boolean locked = false;

}
