package com.syamhad.katalogfilm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syamhad.katalogfilm.DetailActivity;
import com.syamhad.katalogfilm.FavActivity;
import com.syamhad.katalogfilm.MainTabActivity;
import com.syamhad.katalogfilm.R;
import com.syamhad.katalogfilm.SettingActivity;
import com.syamhad.katalogfilm.TodayActivity;
import com.syamhad.katalogfilm.adapter.FilmAdapter;
import com.syamhad.katalogfilm.model.Film;
import com.syamhad.katalogfilm.model.MainViewModel;

import java.util.ArrayList;

public class FragTvshow extends Fragment {
    private RecyclerView rvFilm;
    private FilmAdapter adapter;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;
    private TextView tvNodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tvshow, container, false);

        setHasOptionsMenu(true);

        tvNodata = view.findViewById(R.id.tvnodata);

        adapter = new FilmAdapter();
        adapter.notifyDataSetChanged();

        rvFilm = view.findViewById(R.id.rv_film);
        rvFilm.setHasFixedSize(true);
        rvFilm.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilm.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FilmAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Film data) {
                showSelectedItem(data);
            }
        });

        progressBar = view.findViewById(R.id.progressBar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFilm().observe(this, getFilmData);
        mainViewModel.setFilm(MainTabActivity.Tvshows, MainTabActivity.All);
        showLoading(true);

        return view;
    }

    private Observer<ArrayList<Film>> getFilmData = new Observer<ArrayList<Film>>() {
        @Override
        public void onChanged(ArrayList<Film> movieItems) {
            if (movieItems != null) {
                tvNodata.setVisibility(View.GONE);
                adapter.setData(movieItems);
                showLoading(false);
            }
            else{
                tvNodata.setVisibility(View.VISIBLE);
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showSelectedItem(Film film) {
        Intent openDetail = new Intent(getActivity(), DetailActivity.class);
        openDetail.putExtra(DetailActivity.DATA_FILM, film);
        startActivity(openDetail);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainTabActivity) getActivity()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.setFilm(MainTabActivity.Tvshows, query);
                showLoading(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equalsIgnoreCase("")){
                    mainViewModel.setFilm(MainTabActivity.Tvshows, MainTabActivity.All);
                    showLoading(true);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.release_today:
                Intent tdyIntent = new Intent(getContext(), TodayActivity.class);
                startActivity(tdyIntent);

                return true;

            case R.id.action_notif_setting:
                Intent sIntent = new Intent(getContext(), SettingActivity.class);
                startActivity(sIntent);

                return true;

            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);

                return true;

            case R.id.favFilm:
                Intent mFav = new Intent(getActivity(), FavActivity.class);
                mFav.putExtra(FavActivity.JENIS, "tv");
                startActivity(mFav);

                return true;
        }
        return false;
    }
}