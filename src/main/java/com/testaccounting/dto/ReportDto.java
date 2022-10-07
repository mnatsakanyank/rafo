package com.testaccounting.dto;

import com.testaccounting.data.entity.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Setter
@ToString
public class ReportDto {
    private int year;
    private Month month;
    private BigDecimal sum;
    private Currency currency;

    public ReportDto(int year, Month month, BigDecimal sum, Currency currency) {
        this.year = year;
        this.month = month;
        this.sum = sum;
        this.currency = currency;
    }
}
