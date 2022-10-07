package com.testaccounting.service;

import com.testaccounting.data.entity.Currency;
import com.testaccounting.data.entity.ExchangeRate;
import com.testaccounting.data.repository.ExchangeRatesRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class RatesService {
    private final ExchangeRatesRepository exchangeRatesRepository;


    public RatesService(ExchangeRatesRepository exchangeRatesRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
    }

    public ExchangeRate saveExchangeRatesIfNotExistsAndGet(LocalDate rateDate,
                                                            BigDecimal rate,
                                                            Currency currencyFrom,
                                                            Currency currencyTo){
        try{
            return exchangeRatesRepository.
                    save(new ExchangeRate(rateDate, rate, currencyFrom, currencyTo));
        }catch (DataIntegrityViolationException e){
            return exchangeRatesRepository.findByRateDate_AndCurrencyFromAndCurrencyTo(rateDate,
                    currencyFrom, currencyTo).orElseThrow(() ->
                    new IllegalStateException("Currency rate not found by "+rateDate+", "+currencyFrom+", "+currencyTo));
        }
    }
    public Optional<ExchangeRate> findClosestExchangeRate(LocalDate rateDate, Currency currencyFrom, Currency currencyTo){
        return exchangeRatesRepository.
                findFirstByCurrencyFromAndCurrencyToAndRateDateIsLessThanEqualOrderByRateDateDesc(currencyFrom, currencyTo, rateDate);
    }
}
