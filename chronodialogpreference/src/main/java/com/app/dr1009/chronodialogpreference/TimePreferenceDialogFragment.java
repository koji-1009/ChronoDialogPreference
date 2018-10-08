package com.app.dr1009.chronodialogpreference;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragment;

public class TimePreferenceDialogFragment extends PreferenceDialogFragment {

    private static final String ARG_24_HOUR = "24_hour";
    private static final String SAVE_STATE_TIME = "save_state_time";

    private TimePicker mTimePicker;

    public static TimePreferenceDialogFragment newInstance(@NonNull final String key,
                                                           boolean is24HourMode) {
        final TimePreferenceDialogFragment fragment = new TimePreferenceDialogFragment();
        final Bundle b = new Bundle(2);
        b.putString(ARG_KEY, key);
        b.putBoolean(ARG_24_HOUR, is24HourMode);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String text;
        if (savedInstanceState == null) {
            text = getTimeDialogPreference().getText();
        } else {
            text = savedInstanceState.getString(SAVE_STATE_TIME);
        }
        boolean is24Hour = getArguments().getBoolean(ARG_24_HOUR, false);

        mTimePicker = new TimePicker(getActivity());
        mTimePicker.setIs24HourView(is24Hour);

        String[] divided = ChronoUtil.getTimeFromText(text);
        int hour = Integer.parseInt(divided[0]);
        int minute = Integer.parseInt(divided[1]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setView(mTimePicker);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                value = ChronoUtil.getTimeText(mTimePicker.getHour(), mTimePicker.getMinute());
            } else {
                value = ChronoUtil.getTimeText(mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
            }
            if (getTimeDialogPreference().callChangeListener(value)) {
                getTimeDialogPreference().setText(value);
            }
        }
    }

    private TimeDialogPreference getTimeDialogPreference() {
        return (TimeDialogPreference) getPreference();
    }
}
