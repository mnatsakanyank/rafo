package com.testaccounting.helper;

import java.time.format.DateTimeFormatter;

public class Constant {
    private Constant(){}
    public static final int RATE_SCALE = 5;
    public static final int SALARY_SCALE = 2;

    public static final DateTimeFormatter RATE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("M/d/yyyy EEEE");

    public static final int SALARY_DAY_OF_MONTH = 7;
}
