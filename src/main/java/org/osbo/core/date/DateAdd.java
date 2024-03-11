package org.osbo.core.date;

import java.util.Date;
import java.util.Calendar;

public class DateAdd {
    public static Date agregarMeses(Date fecha, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
}
