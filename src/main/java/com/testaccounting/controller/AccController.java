package com.testaccounting.controller;

import com.testaccounting.dto.EmployeeDto;
import com.testaccounting.dto.ReportDto;
import com.testaccounting.helper.SortType;
import com.testaccounting.service.EmployeeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/employees", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccController {
    private final EmployeeService employeeService;

    public AccController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(path = "salary")
    @ResponseBody
    public ResponseEntity<List<EmployeeDto>> getEmployees(@RequestParam Integer year,
                                                          @RequestParam Integer month,
                                                          @RequestParam Optional<SortType> sort){
        return ResponseEntity.ok(employeeService.
                getEmployeeSalariesForMonth(year, month, sort));
    }
    @GetMapping(path = "salaries-by-months")
    @ResponseBody
    public ResponseEntity<List<ReportDto>> getTotalSalariesByMonths(){
        return ResponseEntity.ok(employeeService.createMonthlyReport());
    }

}
