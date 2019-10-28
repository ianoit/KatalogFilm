package com.syamhad.katalogfilm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.syamhad.katalogfilm.fragment.MyPreferenceFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new MyPreferenceFragment()).commit();
    }
}
