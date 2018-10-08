package com.app.dr1009.chronodialogpreference;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;

class ChronoUtil {

    static String[] getTimeFromText(@NonNull final String text) {
        return text.split(":");
    }

    static String getTimeText(final int hour, final int minute) {
        return String.format(Locale.US, "%02d:%02d", hour, minute);
    }

    static String get24TimeText(boolean is24Hour, final int hour, final int minute) {
        if (is24Hour) {
            return String.format(Locale.US, "%02d:%02d", hour, minute);
        } else {
            if (hour >= 12) {
                return String.format(Locale.US, "PM %02d:%02d", hour - 12, minute);
            } else {
                return String.format(Locale.US, "AM %02d:%02d", hour, minute);
            }
        }
    }

    static String[] getDateFromText(@NonNull final String text) {
        return text.split("\\.");
    }

    static Calendar getCalenderFromText(@NonNull final String text) {
        String[] divided = ChronoUtil.getDateFromText(text);
        int year = Integer.parseInt(divided[0]);
        int month = Integer.parseInt(divided[1]) - 1;
        int dayOfMonth = Integer.parseInt(divided[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        return calendar;
    }

    static String getDateText(final int year, final int month, final int dayOfMonth) {
        return String.format(Locale.US, "%04d.%02d.%02d", year, month + 1, dayOfMonth);
    }
}
