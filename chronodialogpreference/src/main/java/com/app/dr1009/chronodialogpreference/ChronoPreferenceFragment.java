package com.app.dr1009.chronodialogpreference;

import android.app.DialogFragment;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

public abstract class ChronoPreferenceFragment extends PreferenceFragment {

    private static final String DIALOG_FRAGMENT_TAG =
            "ChronoPreferenceFragment.DIALOG";

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment f = null;
        if (preference instanceof TimeDialogPreference) {
            TimeDialogPreference dialogPreference = (TimeDialogPreference) preference;
            f = TimePreferenceDialogFragment
                    .newInstance(dialogPreference.getKey(), dialogPreference.is24Hour());
        } else if (preference instanceof DateDialogPreference) {
            DateDialogPreference dialogPreference = (DateDialogPreference) preference;
            f = DatePreferenceDialogFragment
                    .newInstance(dialogPreference.getKey(), dialogPreference.getMinDate(), dialogPreference.getMaxDate());
        }


        if (f != null) {
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
