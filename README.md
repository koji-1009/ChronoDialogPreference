# ChronoDialogPreference

[![](https://jitpack.io/v/koji-1009/ChronoDialogPreference.svg)](https://jitpack.io/#koji-1009/ChronoDialogPreference)

Time picker dialog preference and Date picker dialog preference.

## Gradle

NOTE: 2.x only supports Jetpack. If you use appcompat 1.x which is almost stable is the way to go.

Step1. Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' } // Add this line
    }
}
```

Step2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.koji-1009:ChronoDialogPreference:x.y.z'
}
```

## How to use

Add `TimeDialogPreference` or `DateDialogPreference` to your preference's xml. See demo app.

```xml
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <com.dr1009.app.chronodialogpreference.TimeDialogPreference
        android:defaultValue="01:10"
        android:key="test1"
        android:title="Time Test"
        app:customFormat="HH-MM"       // (optional) custom SimpleDateFormat pattern instead of system default
        app:force12HourMode="true"     // (optional) force 12 hour mode (AM/PM) instead of system default
        app:force24HourMode="false" /> // (optional) force 24 hour mode (not AM/PM) instead of system default

    <com.dr1009.app.chronodialogpreference.DateDialogPreference
        android:defaultValue="2000.1.1"
        android:key="test2"
        android:title="Date Test"
        app:customFormat="dd.MM.yyyy" // (optional) custom SimpleDateFormat pattern instead of system default
        app:maxDate="2020-1-1"        // (optional) set Max Date to select on Calendar
        app:minDate="1970-1-1" />     // (optional) set Min Date to select on Calendar
</PreferenceScreen>
```
Then, extend 'ChronoPreferenceFragment' instead of 'PreferenceFragment'. 

```
class SettingsFragment : ChronoPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_chrono, rootKey)
    }
}
```

## License

```
MIT License

Copyright (c) 2018-2019 Koji Wakamiya, Serhii Yolkin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
