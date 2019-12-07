package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class DateDialogPreference extends DialogPreference {
    private static final String DEFAULT_DATE = "1970-1-1";

    private final String mMaxDate;
    private final String mMinDate;
    private final String mCustomFormat;
    private final SimpleDateFormat mCustomSimpleDateFormat;
    private Calendar mCalendar = Calendar.getInstance();

    public DateDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_DatePicker, defStyleAttr, defStyleRes);

        mMinDate = a.getString(R.styleable.Dialog_Preference_DatePicker_minDate);
        mMaxDate = a.getString(R.styleable.Dialog_Preference_DatePicker_maxDate);
        mCustomFormat = a.getString(R.styleable.Dialog_Preference_DatePicker_customFormat);
        if (mCustomFormat != null && !mCustomFormat.isEmpty()) {
            mCustomSimpleDateFormat = new SimpleDateFormat(mCustomFormat, Locale.getDefault());
        } else {
            mCustomSimpleDateFormat = null;
        }

        a.recycle();
    }

    public DateDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DateDialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.preference.R.attr.dialogPreferenceStyle);
    }

    public DateDialogPreference(Context context) {
        this(context, null);
    }

    public Calendar getDate() {
        return mCalendar;
    }

    public String getMinDate() {
        return mMinDate;
    }

    public String getMaxDate() {
        return mMaxDate;
    }

    public String getCustomFormat() {
        return mCustomFormat;
    }

    public String getSerializedValue() {
        return ChronoUtil.DATE_FORMATTER.format(getDate().getTime());
    }

    public void setSerializedValue(@NonNull final String serializedDate) {
        try {
            mCalendar = ChronoUtil.dateToCalendar(ChronoUtil.DATE_FORMATTER.parse(serializedDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        final boolean wasBlocking = shouldDisableDependents();

        persistString(serializedDate);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        if (mCustomSimpleDateFormat != null)
            return mCustomSimpleDateFormat.format(getDate().getTime());

        return DateUtils.formatDateTime(getContext(), getDate().getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        if (defaultValue == null) {
            setSerializedValue(getPersistedString(DEFAULT_DATE));
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
        myState.text = getSerializedValue();
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
