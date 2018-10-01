package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimeDialogPreference extends DialogPreference {

    private TimePicker mTimePicker;
    private int mHour = 0;
    private int mMinute = 0;

    private boolean mIs24Hour = false;

    public TimeDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimeDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeDialogPreference(Context context) {
        super(context);
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

    private static class SavedState extends BaseSavedState {
        String text;

        SavedState(Parcel source) {
            super(source);
            text = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(text);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
