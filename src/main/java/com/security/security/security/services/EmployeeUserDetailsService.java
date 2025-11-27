package com.security.security.security.services;

import com.security.security.entity.Employee;
import com.security.security.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor

public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employee employee = this.employeeRepository.findUserByLogin(login)
                .orElseThrow(()->{
                   log.error("User not found by login: " + login);
                   return new UsernameNotFoundException("User not found by login: " + login);
                });
        log.info("user found: {} - {}",login,employee.getFullName());
        return EmployeeUserDetails.build(employee);
    }
}
