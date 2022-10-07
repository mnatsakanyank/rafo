package com.testaccounting.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.testaccounting.data.entity.Currency;
import com.testaccounting.helper.Constant;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@Service
public class CsvService {
    private final EmployeeService employeeService;
    private final RatesService ratesService;

    public CsvService(EmployeeService employeeService, RatesService ratesService) {
        this.employeeService = employeeService;
        this.ratesService = ratesService;
    }

    public void parseCsvAndSaveEmployeesAndSalaries(String fileName, CSVReader csvReader)throws IOException, CsvValidationException {
        Pair<Month, Integer> my = this.parseEmployeeSalaryFileNameAndGetYearAndMonth(fileName);
        Month m = my.getFirst();
        Integer y = my.getSecond();
        int lineNum = 0;
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            if(lineNum++==0){
                continue;
            }
            String employeeName = line[0];
            BigDecimal amount = new BigDecimal(line[1]).
                    setScale(Constant.SALARY_SCALE);
            employeeService.saveEmployeeInfoAndSalary(employeeName, y, m.getValue(), amount, Currency.USD);
        }
    }
    private Pair<Month, Integer> parseEmployeeSalaryFileNameAndGetYearAndMonth(String fileName){
        final String FILE_NAME_PREFIX = "EmployeeSalary";
        final int indExt = fileName.indexOf('.');
        if(indExt<0||!fileName.startsWith(FILE_NAME_PREFIX)){
            throw new IllegalArgumentException("Unexpected name of csv file "+fileName);
        }
        String monthYear = fileName.substring(FILE_NAME_PREFIX.length(), indExt);
        String monthStr  = monthYear.substring(0, monthYear.length()-4);
        String yearStr   = monthYear.substring(monthYear.length()-4);
        Month m = Month.valueOf(monthStr.toUpperCase());
        Integer y = Integer.parseInt(yearStr);
        return Pair.of(m, y);
    }
    public void parseCsvAndSaveCurrencyRates(CSVReader reader)throws IOException,
            CsvValidationException{
        int lineNum = 0;
        String[] line;
        while ((line = reader.readNext()) != null) {
            if(lineNum++==0){
                continue;
            }
            String rateDateStr = line[0];
            BigDecimal rate = new BigDecimal(line[1]).
                    setScale(Constant.RATE_SCALE);
            LocalDate rateDate = LocalDate.parse(rateDateStr, Constant.RATE_DATE_FORMATTER);
            Currency currencyFrom = Currency.valueOf(line[2]);
            Currency currencyTo   = Currency.valueOf(line[3]);
            ratesService.saveExchangeRatesIfNotExistsAndGet(rateDate, rate, currencyFrom, currencyTo);
        }
    }
}
