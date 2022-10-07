package com.testaccounting.data.entity;

import com.testaccounting.helper.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
        @Index(name = "currencies",
                columnList = "rateDate, currencyFrom, currencyTo",
                unique = true)
})
public class ExchangeRate {
    private static final String KEY_SEQ_NAME = "id_rates_seq";
    @Id
    @SequenceGenerator(name = KEY_SEQ_NAME,
            sequenceName = KEY_SEQ_NAME,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = KEY_SEQ_NAME)
    @Access(AccessType.PROPERTY)
    private Long id;

    private LocalDate rateDate;


    @Column(precision = 10, scale = Constant.RATE_SCALE)
    private BigDecimal rateSum;
    @Enumerated(EnumType.STRING)
    private Currency currencyFrom;
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDate rateDate, BigDecimal rateSum, Currency currencyFrom, Currency currencyTo) {
        this.rateDate = rateDate;
        this.rateSum = rateSum;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
    }
}
