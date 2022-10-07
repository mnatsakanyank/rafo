package com.testaccounting.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
    @Index(name = "employee_name", columnList = "employeeName", unique = true)
})
public class Employee {
    public Employee(String employeeName) {
        this.employeeName = employeeName;
    }

    public Employee() {
    }

    private static final String KEY_SEQ_NAME = "id_employee_seq";
    @Id
    @SequenceGenerator(name = KEY_SEQ_NAME,
            sequenceName = KEY_SEQ_NAME,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = KEY_SEQ_NAME)
    @Access(AccessType.PROPERTY)
    private Long id;


    private String employeeName;


}
