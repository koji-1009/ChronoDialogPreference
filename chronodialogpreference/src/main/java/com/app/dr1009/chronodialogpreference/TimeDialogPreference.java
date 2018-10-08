package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.preference.DialogPreference;

public class TimeDialogPreference extends DialogPreference {

    private static final String DEFAULT_TIME = "00:00";

    private final boolean mIs24Hour;
    private TimePicker mTimePicker;
    private int mHour = 0;
    private int mMinute = 0;

    public TimeDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_TimePicker, 0, 0);

        mIs24Hour = a.getBoolean(R.styleable.Dialog_Preference_TimePicker_is24HourMode, false);

        a.recycle();
    }

    public TimeDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeDialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.preference.R.attr.dialogPreferenceStyle);
    }

    public TimeDialogPreference(Context context) {
        this(context, null);
    }

    @Override
    public CharSequence getSummary() {
        return ChronoUtil.get24TimeText(mIs24Hour, mHour, mMinute);
    }

    public boolean is24Hour() {
        return mIs24Hour;
    }

    public String getText() {
        return ChronoUtil.getTimeText(mHour, mMinute);
    }

    public void setText(@NonNull final String text) {
        String[] divided = ChronoUtil.getTimeFromText(text);
        mHour = Integer.parseInt(divided[0]);
        mMinute = Integer.parseInt(divided[1]);

        final boolean wasBlocking = shouldDisableDependents();

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        setSummary(getSummary());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        if (defaultValue == null) {
            setText(getPersistedString(DEFAULT_TIME));
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
        myState.text = ChronoUtil.getTimeText(mHour, mMinute);
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
}
