package com.app.dr1009.chronodialogpreference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragment;

public class DatePreferenceDialogFragment extends PreferenceDialogFragment {

    private static final String ARG_MIN_DATE = "min_date";
    private static final String ARG_MAX_DATE = "max_date";
    private static final String SAVE_STATE_DATE = "save_state_time";

    private DatePicker mDatePicker;

    public static DatePreferenceDialogFragment newInstance(@NonNull final String key,
                                                           @Nullable final String minDate,
                                                           @Nullable final String maxDate) {
        final DatePreferenceDialogFragment
                fragment = new DatePreferenceDialogFragment();
        final Bundle b = new Bundle(3);
        b.putString(ARG_KEY, key);
        b.putString(ARG_MIN_DATE, minDate);
        b.putString(ARG_MAX_DATE, maxDate);
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
            text = getDateDialogPreference().getText();
        } else {
            text = savedInstanceState.getString(SAVE_STATE_DATE);
        }

        mDatePicker = new DatePicker(getActivity());

        String[] divided = ChronoUtil.getDateFromText(text);
        int year = Integer.parseInt(divided[0]);
        int month = Integer.parseInt(divided[1]) - 1;
        int dayOfMonth = Integer.parseInt(divided[2]);
        mDatePicker.updateDate(year, month, dayOfMonth);

        if (minDate != null) {
            Calendar calender = ChronoUtil.getCalenderFromText(minDate);
            mDatePicker.setMinDate(calender.getTimeInMillis());
        }
        if (maxDate != null) {
            Calendar calender = ChronoUtil.getCalenderFromText(maxDate);
            mDatePicker.setMaxDate(calender.getTimeInMillis());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        DialogPreference preference = getPreference();
        builder.setTitle(preference.getDialogTitle())
                .setPositiveButton(preference.getPositiveButtonText(), this)
                .setNegativeButton(preference.getNegativeButtonText(), this)
                .setView(mDatePicker);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_STATE_DATE,
                ChronoUtil.getDateText(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()));
    }

    private DateDialogPreference getDateDialogPreference() {
        return (DateDialogPreference) getPreference();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = ChronoUtil.getDateText(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
            if (getDateDialogPreference().callChangeListener(value)) {
                getDateDialogPreference().setText(value);
            }
        }
    }
}
