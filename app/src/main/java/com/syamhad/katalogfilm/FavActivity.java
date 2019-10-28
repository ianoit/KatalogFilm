package com.syamhad.katalogfilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.syamhad.katalogfilm.adapter.FilmAdapter;
import com.syamhad.katalogfilm.db.FilmHelper;
import com.syamhad.katalogfilm.model.Film;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavActivity extends AppCompatActivity implements LoadFilmCallback {
    private RecyclerView rvFilm;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    public static String JENIS = "movie";
    private FilmAdapter adapter;
    private FilmHelper filmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        JENIS = getIntent().getStringExtra(JENIS);
        String titleactbar = "";
        if(JENIS == "tv"){
            titleactbar = getString(R.string.str_tvshow);
        }
        else{
            titleactbar = getString(R.string.str_movies);
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(titleactbar);

        rvFilm = findViewById(R.id.rv_film);
        rvFilm.setLayoutManager(new LinearLayoutManager(this));
        rvFilm.setHasFixedSize(true);

        filmHelper = FilmHelper.getInstance(getApplicationContext());
        filmHelper.open();

        progressBar = findViewById(R.id.progressbar);

        adapter = new FilmAdapter();
        rvFilm.setAdapter(adapter);
        adapter.setOnItemClickCallback(new FilmAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Film data) {
                showSelectedItem(data);
            }
        });

        if (savedInstanceState == null) {
            new LoadFilmAsync(filmHelper, this).execute();
        } else {
            ArrayList<Film> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setData(list);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListFilm());
    }

    @Override
    public void onResume(){
        super.onResume();
        new LoadFilmAsync(filmHelper, this).execute();
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Film> film) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setData(film);
    }

    private static class LoadFilmAsync extends AsyncTask<Void, Void, ArrayList<Film>> {

        private final WeakReference<FilmHelper> weakFilmHelper;
        private final WeakReference<LoadFilmCallback> weakCallback;

        private LoadFilmAsync(FilmHelper filmHelper, LoadFilmCallback callback) {
            weakFilmHelper = new WeakReference<>(filmHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Film> doInBackground(Void... voids) {

            return weakFilmHelper.get().getAllFav(JENIS);
        }

        @Override
        protected void onPostExecute(ArrayList<Film> film) {
            super.onPostExecute(film);

            weakCallback.get().postExecute(film);

        }
    }

    private void showSelectedItem(Film film) {
        Intent openDetail = new Intent(this, DetailActivity.class);
        openDetail.putExtra(DetailActivity.DATA_FILM, film);
        startActivity(openDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filmHelper.close();
    }
}
