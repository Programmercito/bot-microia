package org.osbo.core.date;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateFormatter {
    // java.util.Date a string
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
