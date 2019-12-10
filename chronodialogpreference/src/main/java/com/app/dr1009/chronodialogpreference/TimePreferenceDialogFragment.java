package com.app.dr1009.chronodialogpreference;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TimePreferenceDialogFragment extends ChronoPreferenceDialogFragmentCompat {
    private static final String ARG_FORCE_12_HOUR_PICKER = "force_12_hour_picker";
    private static final String ARG_FORCE_24_HOUR_PICKER = "force_24_hour_picker";
    private static final String ARG_CUSTOM_FORMAT = "custom_format";
    private static final String SAVE_STATE_TIME = "save_state_time";

    protected TimePicker mTimePicker;

    public static TimePreferenceDialogFragment newInstance(
        @NonNull final String key,
        final boolean force12HourPicker,
        final boolean force24HourPicker,
        @Nullable final String customFormat
    ) {
        final TimePreferenceDialogFragment fragment = new TimePreferenceDialogFragment();
        final Bundle b = new Bundle(4);
        b.putString(ARG_KEY, key);
        b.putBoolean(ARG_FORCE_12_HOUR_PICKER, force12HourPicker);
        b.putBoolean(ARG_FORCE_24_HOUR_PICKER, force24HourPicker);
        b.putString(ARG_CUSTOM_FORMAT, customFormat);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String text;
        if (savedInstanceState == null) {
            text = getTimeDialogPreference().getSerializedValue();
        } else {
            text = savedInstanceState.getString(SAVE_STATE_TIME);
        }

        final boolean force12HourPicker = getArguments().getBoolean(ARG_FORCE_12_HOUR_PICKER, false);
        final boolean force24HourPicker = getArguments().getBoolean(ARG_FORCE_24_HOUR_PICKER, false);

        mTimePicker = new TimePicker(getActivity());

        boolean is24HourView;
        if (force12HourPicker) {
            is24HourView = false;
        } else if (force24HourPicker) {
            is24HourView = true;
        } else {
            is24HourView = DateFormat.is24HourFormat(getContext());
        }

        mTimePicker.setIs24HourView(is24HourView);

        final Calendar parsed;
        try {
            parsed = ChronoUtil.dateToCalendar(ChronoUtil.TIME_FORMATTER.parse(text));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int hour = parsed.get(Calendar.HOUR_OF_DAY);
        int minute = parsed.get(Calendar.MINUTE);

        setHourAndMinute(hour, minute);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value;
            Calendar calendar = Calendar.getInstance();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                calendar.set(Calendar.MINUTE, mTimePicker.getMinute());
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
            }

            value = ChronoUtil.TIME_FORMATTER.format(calendar.getTime());
            if (getTimeDialogPreference().callChangeListener(value)) {
                getTimeDialogPreference().setSerializedValue(value);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void setHourAndMinute(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
    }

    View getPickerView() {
        return mTimePicker;
    }

    private TimeDialogPreference getTimeDialogPreference() {
        return (TimeDialogPreference) getPreference();
    }
}
