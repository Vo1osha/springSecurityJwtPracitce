package com.security.security.security;

import com.security.security.entity.Employee;
import com.security.security.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
@Setter
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employee employee = this.employeeRepository.findUserByLogin(login)
                .orElseThrow(()->{
                   log.error("User not found by login: " + login);
                   return new UsernameNotFoundException("User not found by login: " + login);
                });
        log.info("user found: {} - {}",login,employee.getFullName());
        return new EmployeeUserDetails(employee);
    }
}
