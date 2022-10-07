package com.testaccounting.data.repository;

import com.testaccounting.data.entity.Currency;
import com.testaccounting.data.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRatesRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByRateDate_AndCurrencyFromAndCurrencyTo(LocalDate rateDate,
                                                                        Currency currencyFrom,
                                                                        Currency currencyTo);
    Optional<ExchangeRate>
        findFirstByCurrencyFromAndCurrencyToAndRateDateIsLessThanEqualOrderByRateDateDesc(Currency currencyFrom,
                                                                                            Currency currencyTo,
                                                                                            LocalDate rateDate);
}
