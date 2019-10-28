package com.syamhad.katalogfilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.syamhad.katalogfilm.adapter.FilmAdapter;
import com.syamhad.katalogfilm.model.Film;
import com.syamhad.katalogfilm.model.MainViewModel;

import java.util.ArrayList;

public class TodayActivity extends AppCompatActivity {
    private RecyclerView rvFilm;
    private FilmAdapter adapter;
    private ProgressBar progressBar;
    private MainViewModel mainViewModel;
    private TextView tvNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        tvNodata = findViewById(R.id.tvnodata);

        adapter = new FilmAdapter();
        adapter.notifyDataSetChanged();

        rvFilm = findViewById(R.id.rv_film);
        rvFilm.setHasFixedSize(true);
        rvFilm.setLayoutManager(new LinearLayoutManager(this));
        rvFilm.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FilmAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Film data) {
                showSelectedItem(data);
            }
        });

        progressBar = findViewById(R.id.progressBar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFilm().observe(this, getFilmData);
        mainViewModel.setFilmToday();
        showLoading(true);
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
        Intent openDetail = new Intent(this, DetailActivity.class);
        openDetail.putExtra(DetailActivity.DATA_FILM, film);
        startActivity(openDetail);
    }
}
