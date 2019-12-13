package com.app.dr1009.chronodialogpreference;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatePreferenceDialogFragment extends ChronoPreferenceDialogFragmentCompat {

    private static final String ARG_MIN_DATE = "min_date";
    private static final String ARG_MAX_DATE = "max_date";
    private static final String ARG_CUSTOM_FORMAT = "custom_format";
    private static final String SAVE_STATE_DATE = "save_state_time";

    protected DatePicker mDatePicker;

    public static DatePreferenceDialogFragment newInstance(@NonNull final String key,
                                                           @Nullable final String minDate,
                                                           @Nullable final String maxDate,
                                                           @Nullable final String customFormat) {
        final DatePreferenceDialogFragment
            fragment = new DatePreferenceDialogFragment();
        final Bundle b = new Bundle(4);
        b.putString(ARG_KEY, key);
        b.putString(ARG_MIN_DATE, minDate);
        b.putString(ARG_MAX_DATE, maxDate);
        b.putString(ARG_CUSTOM_FORMAT, customFormat);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String minDate = getArguments().getString(ARG_MIN_DATE);
        String maxDate = getArguments().getString(ARG_MAX_DATE);

        String text;
        if (savedInstanceState == null) {
            text = getDateDialogPreference().getSerializedValue();
        } else {
            text = savedInstanceState.getString(SAVE_STATE_DATE);
        }

        mDatePicker = new DatePicker(getActivity());
        final Calendar calendar;
        try {
            calendar = ChronoUtil.dateToCalendar(ChronoUtil.DATE_FORMATTER.parse(text));
        } catch (ParseException e) {
            throw new AssertionError("Date format is always known and parsable", e);
        }

        mDatePicker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );

        if (minDate != null) {
            Date date;
            try {
                date = ChronoUtil.DATE_FORMATTER.parse(minDate);
            } catch (ParseException e) {
                throw new IllegalArgumentException("minDate is not in the correct format", e);
            }
            mDatePicker.setMinDate(date.getTime());
        }
        if (maxDate != null) {
            Date date;
            try {
                date = ChronoUtil.DATE_FORMATTER.parse(maxDate);
            } catch (ParseException e) {
                throw new IllegalArgumentException("maxDate is not in the correct format", e);
            }
            mDatePicker.setMaxDate(date.getTime());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(
            SAVE_STATE_DATE,
            ChronoUtil.DATE_FORMATTER.format(getCalendarFromDatePicker().getTime()));
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = ChronoUtil.DATE_FORMATTER.format(getCalendarFromDatePicker().getTime());
            if (getDateDialogPreference().callChangeListener(value)) {
                getDateDialogPreference().setSerializedValue(value);
            }
        }
    }

    View getPickerView() {
        return mDatePicker;
    }

    private DateDialogPreference getDateDialogPreference() {
        return (DateDialogPreference) getPreference();
    }

    private Calendar getCalendarFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mDatePicker.getYear());
        calendar.set(Calendar.MONTH, mDatePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
        return calendar;
    }

}
