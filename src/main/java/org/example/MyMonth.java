package org.example;

import java.time.Month;

public enum MyMonth {
    Январь(Month.JANUARY), Февраль(Month.FEBRUARY), Март(Month.MARCH), Апрель(Month.APRIL), Май(Month.MAY), Июнь(Month.JUNE), Июль(Month.JULY), Август(Month.AUGUST), Сентябрь(Month.SEPTEMBER), Октябрь(Month.OCTOBER), Ноябрь(Month.NOVEMBER), Декабрь(Month.DECEMBER);
    final Month month;
    MyMonth(Month month) { this.month = month; }
    public Month getMonth() { return month; }

}
