package fr.nelfdesign.go4lunch.settings.activity.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import fr.nelfdesign.go4lunch.R;

/**
 * Created by Nelfdesign at 20/01/2020
 * fr.nelfdesign.go4lunch.settings.activity.fragments
 */
public class SettingsHeaders extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_headers, rootKey);
    }
}
