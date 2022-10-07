package com.testaccounting.data.entity;

import com.testaccounting.helper.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
        @Index(name = "employee_salary_unique", columnList = "yyyy, mm, employee_id", unique = true)
})
public class Salary {
    private static final String KEY_SEQ_NAME = "id_salary_seq";
    @Id
    @SequenceGenerator(name = KEY_SEQ_NAME,
            sequenceName = KEY_SEQ_NAME,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = KEY_SEQ_NAME)
    @Access(AccessType.PROPERTY)
    private Long id;


    private Integer yyyy;
    private Integer mm;

    @Column(precision=10, scale= Constant.SALARY_SCALE)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Salary() {
    }

    public Salary(Integer year, Integer month, BigDecimal amount, Currency currency, Employee employee) {
        this.yyyy = year;
        this.mm = month;
        this.amount = amount;
        this.currency = currency;
        this.employee = employee;
    }
}
