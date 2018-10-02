package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateDialogPreference extends DialogPreference {

    private static final String DEFAULT_DATE = "1970.1.1";

    private DatePicker mDatePicker;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;

    private final String mMaxDate;
    private final String mMinDate;

    public DateDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_DatePicker, 0, 0);
        mMaxDate = a.getString(R.styleable.Dialog_Preference_DatePicker_maxDate);
        mMinDate = a.getString(R.styleable.Dialog_Preference_DatePicker_minDate);
        a.recycle();
    }

    @Override
    protected View onCreateDialogView() {
        mDatePicker = new DatePicker(getContext());

        Calendar calendar = Calendar.getInstance();
        if (mMaxDate != null) {
            String[] divided = mMaxDate.split("\\.");
            int year = Integer.parseInt(divided[0]);
            int month = Integer.parseInt(divided[1]) - 1;
            int dayOfMonth = Integer.parseInt(divided[2]);

            calendar.set(year, month, dayOfMonth);
            mDatePicker.setMaxDate(calendar.getTimeInMillis());
        }
        if (mMinDate != null) {
            String[] divided = mMinDate.split("\\.");
            int year = Integer.parseInt(divided[0]);
            int month = Integer.parseInt(divided[1]) - 1;
            int dayOfMonth = Integer.parseInt(divided[2]);

            calendar.set(year, month, dayOfMonth);
            mDatePicker.setMinDate(calendar.getTimeInMillis());
        }

        return mDatePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker.updateDate(mYear, mMonth, mDayOfMonth);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            mYear = mDatePicker.getYear();
            mMonth = mDatePicker.getMonth();
            mDayOfMonth = mDatePicker.getDayOfMonth();

            setSummary(getSummary());
            String text = getText();
            if (callChangeListener(text)) {
                persistString(text);
                notifyChanged();
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        return getText();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            setText(getPersistedString(DEFAULT_DATE));
        } else {
            setText((String) defaultValue);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.text = getText();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }

    private String getText() {
        return String.format("%s.%s.%s", mYear, mMonth + 1, mDayOfMonth);
    }

    private void setText(String text) {
        String[] divided = text.split("\\.");
        mYear = Integer.parseInt(divided[0]);
        mMonth = Integer.parseInt(divided[1]) - 1;
        mDayOfMonth = Integer.parseInt(divided[2]);
    }
}
