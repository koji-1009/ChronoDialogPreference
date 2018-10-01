package com.app.dr1009.chronodialogpreference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DateDialogPreference extends DialogPreference {

    private DatePicker mDatePicker;

    public DateDialogPreference(Context context) {
        super(context);
    }

    public DateDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DateDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        mDatePicker = new DatePicker(getContext());
        return mDatePicker;
    }
}
