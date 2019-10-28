package com.syamhad.katalogfilm.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.syamhad.katalogfilm.R;
import com.syamhad.katalogfilm.services.AlarmReceiver;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String DAILY;
    private String RELEASE;

    private CheckBoxPreference isDaily;
    private CheckBoxPreference isRelease;

    private AlarmReceiver alarmReceiver;
    private Context context;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        context = getContext();
        alarmReceiver = new AlarmReceiver();
        init();
        setSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DAILY)) {
            isDaily.setChecked(sharedPreferences.getBoolean(DAILY, false));
            if(sharedPreferences.getBoolean(DAILY, false)){
                alarmReceiver.setRepeatingAlarm(context);
            }
            else{
                stopAlarm(AlarmReceiver.ID_REPEATING);
            }
        }
        if (key.equals(RELEASE)) {
            isRelease.setChecked(sharedPreferences.getBoolean(RELEASE, false));
            if(sharedPreferences.getBoolean(RELEASE, false)){
                alarmReceiver.setReleaseAlarm(context);
            }
            else{
                stopAlarm(AlarmReceiver.ID_RELEASE);
            }
        }
    }

    private void init() {
        DAILY = getResources().getString(R.string.key_daily);
        RELEASE = getResources().getString(R.string.key_release);
        isDaily = (CheckBoxPreference) findPreference(DAILY);
        isRelease = (CheckBoxPreference) findPreference(RELEASE);
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        isDaily.setChecked(sh.getBoolean(DAILY, false));
        isRelease.setChecked(sh.getBoolean(RELEASE, false));
    }

    private void stopAlarm(int type){
        alarmReceiver.cancelAlarm(context, type);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
