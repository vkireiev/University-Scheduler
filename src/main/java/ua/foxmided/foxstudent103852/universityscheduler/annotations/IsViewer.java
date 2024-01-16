package ua.foxmided.foxstudent103852.universityscheduler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(value = "hasAnyAuthority('VIEWER', 'EDITOR', 'ADMIN')")
public @interface IsViewer {

}