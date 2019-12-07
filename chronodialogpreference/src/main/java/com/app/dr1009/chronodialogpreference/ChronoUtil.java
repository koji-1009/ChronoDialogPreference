package com.app.dr1009.chronodialogpreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class ChronoUtil {
    public final static SimpleDateFormat TIME_FORMATTER =
        new SimpleDateFormat("HH:mm", Locale.ROOT);

    public final static SimpleDateFormat DATE_FORMATTER =
        new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    public static Calendar dateToCalendar(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
