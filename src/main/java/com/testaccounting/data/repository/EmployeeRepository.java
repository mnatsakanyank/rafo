package com.testaccounting.data.repository;

import com.testaccounting.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Optional<Employee> findByEmployeeName(String employeeName);
}
