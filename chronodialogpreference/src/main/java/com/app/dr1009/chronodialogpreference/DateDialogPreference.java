package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class DateDialogPreference extends DialogPreference {

    private static final String DEFAULT_DATE = "1970.1.1";

    private final String mMaxDate;
    private final String mMinDate;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;

    public DateDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Dialog_Preference_DatePicker, defStyleAttr, defStyleRes);

        mMinDate = a.getString(R.styleable.Dialog_Preference_DatePicker_minDate);
        mMaxDate = a.getString(R.styleable.Dialog_Preference_DatePicker_maxDate);

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

    public String getMinDate() {
        return mMinDate;
    }

    public String getMaxDate() {
        return mMaxDate;
    }

    public String getText() {
        return ChronoUtil.getDateText(mYear, mMonth + 1, mDayOfMonth);
    }

    public void setText(@NonNull final String text) {
        String[] divided = ChronoUtil.getDateFromText(text);
        mYear = Integer.parseInt(divided[0]);
        mMonth = Integer.parseInt(divided[1]) - 1;
        mDayOfMonth = Integer.parseInt(divided[2]);

        final boolean wasBlocking = shouldDisableDependents();

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        setSummary(getSummary());
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
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        if (defaultValue == null) {
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
}
