package ua.foxmided.foxstudent103852.universityscheduler.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.model.enums.UserRole;

public class SecurityPersonDetails implements UserDetails {
    private Person user;

    public SecurityPersonDetails(Person person) {
        this.user = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<UserRole> personRoles = user.getUserRoles();
        for (UserRole personRole : personRoles) {
            authorities.add(new SimpleGrantedAuthority((personRole.name()).toUpperCase()));
        }
        authorities.add(new SimpleGrantedAuthority(user.getClass().getSimpleName().toUpperCase()));
        authorities.add(new SimpleGrantedAuthority(("ROLE_" + user.getClass().getSimpleName()).toUpperCase()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isLocked();
    }

    public Person getPerson() {
        return user;
    }

}
