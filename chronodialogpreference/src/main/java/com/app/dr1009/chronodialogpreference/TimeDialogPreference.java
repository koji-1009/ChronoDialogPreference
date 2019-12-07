package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.preference.DialogPreference;

public class TimeDialogPreference extends DialogPreference {

    private static final String DEFAULT_TIME = "00:00";
    private final boolean mIsForce12HourModePicker;
    private final boolean mIsForce24HourModePicker;
    private final String mCustomFormat;
    private final SimpleDateFormat mCustomSimpleDateFormat;
    private Date mTime;

    public TimeDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_TimePicker, 0, 0);

        mIsForce12HourModePicker = a.getBoolean(R.styleable.Dialog_Preference_TimePicker_force12HourModePicker, false);
        mIsForce24HourModePicker = a.getBoolean(R.styleable.Dialog_Preference_TimePicker_force24HourModePicker, false);

        mCustomFormat = a.getString(R.styleable.Dialog_Preference_TimePicker_customFormat);
        if (mCustomFormat != null && !mCustomFormat.isEmpty()) {
            mCustomSimpleDateFormat = new SimpleDateFormat(mCustomFormat, Locale.getDefault());
        } else {
            mCustomSimpleDateFormat = null;
        }

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

    public Date getTime() {
        return mTime;
    }

    public boolean isForce12HourPicker() {
        return mIsForce12HourModePicker;
    }

    public boolean isForce24HourPicker() {
        return mIsForce24HourModePicker;
    }

    public String getCustomFormat() {
        return mCustomFormat;
    }

    @Override
    public CharSequence getSummary() {
        if (mCustomSimpleDateFormat != null)
            return mCustomSimpleDateFormat.format(getTime());

        return DateUtils.formatDateTime(getContext(), getTime().getTime(), DateUtils.FORMAT_SHOW_TIME);
    }

    public String getSerializedValue() {
        return ChronoUtil.TIME_FORMATTER.format(getTime());
    }

    public void setSerializedValue(@NonNull final String serializedTime) {
        try {
            mTime = ChronoUtil.TIME_FORMATTER.parse(serializedTime);
        } catch (ParseException e) {
            throw new AssertionError(e);
        }

        final boolean wasBlocking = shouldDisableDependents();

        persistString(serializedTime);

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
            setSerializedValue(getPersistedString(DEFAULT_TIME));
        } else {
            setSerializedValue((String) defaultValue);
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
        myState.text = DateUtils.formatDateTime(getContext(), mTime.getTime(), DateUtils.FORMAT_SHOW_TIME);
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
        setSerializedValue(myState.text);
    }
}
