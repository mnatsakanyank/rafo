package com.testaccounting.data.repository;

import com.testaccounting.data.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Optional<Salary> getSalariesByYyyyAndMmAndEmployee_Id(Integer yyyy, Integer mm, Long employeeId);
    List<Salary> getSalariesByYyyyAndMm(Integer yyyy, Integer mm);

    @Query("select distinct s.yyyy, s.mm from Salary s order by s.yyyy, s.mm")
    List<Object[]> getDistinctYearsAndMonths();
}
