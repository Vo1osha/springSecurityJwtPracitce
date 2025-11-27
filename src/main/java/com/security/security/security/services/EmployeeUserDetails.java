package com.security.security.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.security.entity.Employee;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class EmployeeUserDetails implements UserDetails {

    private final Long employeeId;

    private final String login;
    private final String fullName;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public EmployeeUserDetails(Long employeeId, String login, String fullName, String password,
                               Collection<? extends GrantedAuthority> authorities) {
        this.employeeId = employeeId;
        this.login = login;
        this.fullName = fullName;
        this.password = password;
        this.authorities = authorities;
    }

    public static EmployeeUserDetails build(Employee employee) {
        List<GrantedAuthority> authorities = employee.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new EmployeeUserDetails(
                employee.getId(),
                employee.getLogin(),
                employee.getFullName(),
                employee.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    @JsonIgnore
    public @Nullable String getPassword() {
        return password;
    }


    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }
}
