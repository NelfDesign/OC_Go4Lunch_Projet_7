package fr.nelfdesign.go4lunch.settings.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import butterknife.BindView;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.settings.activity.fragments.SettingsHeaders;

public class SettingsActivity extends BaseActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getActivityLayout() {
        return R.layout.settings_activity;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsHeaders())
                .commit();


        this.configureToolBar(getResources().getString(R.string.settings));
        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();

        this.configureToolBar(pref.getTitle().toString());
        return true;
    }
}