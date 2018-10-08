package com.app.dr1009.chronodialogpreferencedemo

import android.os.Bundle
import com.app.dr1009.chronodialogpreference.ChronoPreferenceFragment

class SettingsFragment : ChronoPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_chrono, rootKey)
    }
}
