package com.syamhad.katalogfilm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.syamhad.katalogfilm.fragment.FragMovies;
import com.syamhad.katalogfilm.fragment.FragTvshow;
import com.syamhad.katalogfilm.fragment.MainFragmentPagerAdapter;

public class MainTabActivity extends AppCompatActivity {
    public static final String All = "all";
    public static final String Movies = "movie";
    public static final String Tvshows = "tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        initViews();
    }

    private void initViews() {
        // setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting view pager
        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        // setting tabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mainFragmentPagerAdapter.addFragment(new FragMovies(), getString(R.string.str_movies));
        mainFragmentPagerAdapter.addFragment(new FragTvshow(), getString(R.string.str_tvshow));
        viewPager.setAdapter(mainFragmentPagerAdapter);
    }
}