package com.app.dr1009.chronodialogpreference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

abstract class ChronoPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    @Override
    protected View onCreateDialogView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.chrono_preference, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        LinearLayout linearLayout = (LinearLayout) onCreateDialogView(context);
        linearLayout.addView(getPickerView());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        DialogPreference preference = getPreference();
        builder.setTitle(preference.getDialogTitle())
            .setPositiveButton(preference.getPositiveButtonText(), this)
            .setNegativeButton(preference.getNegativeButtonText(), this)
            .setView(linearLayout);

        return builder.create();
    }

    abstract View getPickerView();
}
