package com.dr1009.app.chronodialogpreferencedemo

import android.os.Bundle
import com.dr1009.app.chronodialogpreference.ChronoPreferenceFragment

class SettingsFragment : ChronoPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_chrono, rootKey)
    }
}
