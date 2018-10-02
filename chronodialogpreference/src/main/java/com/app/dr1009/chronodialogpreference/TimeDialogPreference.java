package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimeDialogPreference extends DialogPreference {

    private TimePicker mTimePicker;
    private int mHour = 0;
    private int mMinute = 0;

    private boolean mIs24Hour;

    public TimeDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_TimePicker, 0, 0);
        mIs24Hour = a.getBoolean(R.styleable.Dialog_Preference_TimePicker_is24HourMode, false);

        a.recycle();
    }

    @Override
    protected View onCreateDialogView() {
        mTimePicker = new TimePicker(getContext());
        mTimePicker.setIs24HourView(mIs24Hour);

        return mTimePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(mHour);
            mTimePicker.setMinute(mMinute);
        } else {
            mTimePicker.setCurrentHour(mHour);
            mTimePicker.setCurrentMinute(mMinute);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mHour = mTimePicker.getHour();
                mMinute = mTimePicker.getMinute();
            } else {
                mHour = mTimePicker.getCurrentHour();
                mMinute = mTimePicker.getCurrentMinute();
            }

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
        if (mIs24Hour) {
            return String.format("%s:%s", mHour, mMinute);
        } else {
            if (mHour >= 12) {
                return String.format("PM %s:%s", mHour - 12, mMinute);
            } else {
                return String.format("AM %s:%s", mHour, mMinute);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            setText(getPersistedString("00:00"));
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
        return String.format("%s:%s", mHour, mMinute);
    }

    private void setText(String text) {
        String[] divided = text.split(":");
        mHour = Integer.parseInt(divided[0]);
        mMinute = Integer.parseInt(divided[1]);
    }

}
