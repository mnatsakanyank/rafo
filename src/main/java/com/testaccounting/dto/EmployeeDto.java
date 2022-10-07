package com.testaccounting.dto;

import com.testaccounting.data.entity.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
public class EmployeeDto {
    private String name;
    private BigDecimal salary;
    private Currency cur;

    public EmployeeDto(String name, BigDecimal salary, Currency cur) {
        this.name = name;
        this.salary = salary;
        this.cur = cur;
    }
}
