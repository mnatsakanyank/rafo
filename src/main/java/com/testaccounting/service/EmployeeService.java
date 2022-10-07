package com.testaccounting.service;

import com.testaccounting.data.entity.Currency;
import com.testaccounting.data.entity.Employee;
import com.testaccounting.data.entity.ExchangeRate;
import com.testaccounting.data.entity.Salary;
import com.testaccounting.data.repository.EmployeeRepository;
import com.testaccounting.data.repository.SalaryRepository;
import com.testaccounting.dto.EmployeeDto;
import com.testaccounting.dto.ReportDto;
import com.testaccounting.helper.Constant;
import com.testaccounting.helper.SortType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final RatesService ratesService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           SalaryRepository salaryRepository,
                           RatesService ratesService) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
        this.ratesService = ratesService;
    }


    public Map<Long, Employee> getEmployeesByIdsMap(Collection<Long> employeeIds){
        return employeeRepository.findAllById(employeeIds).
                stream().collect(Collectors.toMap(Employee::getId, e->e));
    }
    public void saveEmployeeInfoAndSalary(String employeeName, Integer year, Integer month, BigDecimal amount, Currency currency){
        Employee employee = this.saveEmployeeIfNotExistsAndGet(employeeName);
        saveSalaryIfNotExistsAndGet(employee, year, month, amount, currency);
    }
    private Employee saveEmployeeIfNotExistsAndGet(String employeeName){
        Employee employee = new Employee(employeeName);
        try{
            return employeeRepository.save(employee);
        }catch (DataIntegrityViolationException e){
            return employeeRepository.
                    findByEmployeeName(employeeName).
                    orElseThrow(() -> new IllegalArgumentException("Can not find and create Employee by name "+employeeName));
        }
    }
    private Salary saveSalaryIfNotExistsAndGet(Employee employee, Integer year, Integer month,
                                                BigDecimal amount, Currency currency){
        try{
            return salaryRepository.save(new Salary(year, month, amount, currency, employee));
        }catch (DataIntegrityViolationException e){
            return salaryRepository.
                    getSalariesByYyyyAndMmAndEmployee_Id(year, month,
                            employee.getId()).orElseThrow(() ->
                            new IllegalStateException(
                                    "Can not find or create salary for employee with id = "+employee.getId()
                                            +" year  = "+year+
                                            ", month = "+month));
        }
    }
    public List<EmployeeDto> getEmployeeSalariesForMonth(Integer year, Integer month,
                                                         Optional<SortType> sortType){
        List<Salary> salaries4Month = salaryRepository.getSalariesByYyyyAndMm(year, month);
        List<Long> employeeIds = salaries4Month.stream().
                map(salary -> salary.getEmployee().getId()).toList();
        Map<Long, Employee> employeesById = this.getEmployeesByIdsMap(employeeIds);
        LocalDate salaryDay = LocalDate.of(year, month, Constant.SALARY_DAY_OF_MONTH);
        ExchangeRate closestRate = ratesService.findClosestExchangeRate(salaryDay,
                Currency.USD, Currency.GEL).
                orElseThrow(() -> new IllegalStateException("Can not find rate for date "));
        List<EmployeeDto> employeeDtoList = salaries4Month.stream().map(salary -> {
            Employee employee = employeesById.get(salary.getEmployee().getId());
            BigDecimal salaryGel = salary.getAmount().multiply(closestRate.getRateSum()).
                    setScale(2, RoundingMode.HALF_UP);
            return new EmployeeDto(employee.getEmployeeName(), salaryGel, Currency.GEL);
        }).toList();
        sortType.ifPresent(sort -> Collections.sort(employeeDtoList,
                (e1, e2) -> sort==SortType.ASC?e1.getSalary().compareTo(e2.getSalary()):
                    e2.getSalary().compareTo(e1.getSalary())
        ));
        return employeeDtoList;
    }
    public List<ReportDto> createMonthlyReport(){
        List<Object[]> yearsAndMonths = salaryRepository.getDistinctYearsAndMonths();
        if(yearsAndMonths==null){
            return Collections.emptyList();
        }
        return yearsAndMonths.stream().map(objects -> {
            Integer yyyy = (Integer)objects[0];
            Integer mm   = (Integer)objects[1];
            BigDecimal totalSum4Month = getEmployeeSalariesForMonth(yyyy, mm, Optional.empty()).
                    stream().map(EmployeeDto::getSalary).
                    reduce(BigDecimal.ZERO, BigDecimal::add).
                    setScale(2, RoundingMode.HALF_UP);
            return new ReportDto(yyyy, Month.of(mm), totalSum4Month, Currency.GEL);
        }).toList();
    }
}
