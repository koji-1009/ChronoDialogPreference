package com.dr1009.app.chronodialogpreference;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public abstract class ChronoPreferenceFragment extends PreferenceFragmentCompat {

    public static final String DIALOG_FRAGMENT_TAG = "ChronoPreferenceFragment.DIALOG";

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimeDialogPreference) {
            TimeDialogPreference dialogPreference = (TimeDialogPreference) preference;
            dialogFragment = TimePreferenceDialogFragment
                .newInstance(
                    dialogPreference.getKey(),
                    dialogPreference.isForce12HourPicker(),
                    dialogPreference.isForce24HourPicker(),
                    dialogPreference.getCustomFormat()
                );
        } else if (preference instanceof DateDialogPreference) {
            DateDialogPreference dialogPreference = (DateDialogPreference) preference;
            dialogFragment = DatePreferenceDialogFragment
                .newInstance(
                    dialogPreference.getKey(),
                    dialogPreference.getMinDate(),
                    dialogPreference.getMaxDate(),
                    dialogPreference.getCustomFormat()
                );
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
