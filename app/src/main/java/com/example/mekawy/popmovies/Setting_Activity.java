package com.example.mekawy.popmovies;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Setting_Activity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.movies_setting);
        bind_summary_to_view(findPreference(getString(R.string.setting_sort_key)));

    }

    public void bind_summary_to_view(Preference pref){
        pref.setOnPreferenceChangeListener(this);
        onPreferenceChange(pref,
                PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getString(pref.getKey(), ""));
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String summary=o.toString();

        if(preference instanceof ListPreference){
            ListPreference check_pref=(ListPreference) preference;
            int index=check_pref.findIndexOfValue(summary);
            preference.setSummary(((ListPreference) preference).getEntries()[index]);
        }
        else preference.setSummary(summary);
        return true;
    }
    //to kkep the last instance when back from setting
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }



}
