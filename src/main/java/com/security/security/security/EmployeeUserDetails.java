package com.security.security.security;

import com.security.security.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Data
public class EmployeeUserDetails implements UserDetails {

    private Employee employee;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return employee.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }


    @Override
    public @Nullable String getPassword() {
        return this.employee.getPassword();
    }

    @Override
    public String getUsername() {
        return this.employee.getLogin();
    }
}
