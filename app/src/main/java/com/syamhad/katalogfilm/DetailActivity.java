package com.syamhad.katalogfilm;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syamhad.katalogfilm.db.FilmHelper;
import com.syamhad.katalogfilm.model.Film;
import com.syamhad.katalogfilm.widget.FavWidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    public Film film;
    public static final String DATA_FILM = "data_film";

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private FilmHelper filmHelper;

    private int cekFav = 0;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvJudul = findViewById(R.id.tv_item_judul);
        TextView tvKet = findViewById(R.id.tv_keterangan);
        TextView tvLang = findViewById(R.id.tvLanguage);
        TextView tvDate = findViewById(R.id.tvRilisDate);
        TextView tvRate = findViewById(R.id.tvRating);
        ImageView poster = findViewById(R.id.img_poster);
        ImageView banner = findViewById(R.id.expandedImage);

        film = getIntent().getParcelableExtra(DATA_FILM);
        tvJudul.setText(film.getJudul());
        tvKet.setText(film.getKeterangan());
        tvLang.setText(getString(R.string.language)+" : "+film.getLanguage());
        tvDate.setText(getString(R.string.release)+" : "+film.getRilisdate());
        tvRate.setText(getString(R.string.rate)+" : "+String.valueOf(film.getVoteavg()));
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+film.getGambar())
                .apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(poster);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500"+film.getBanner())
                .apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(banner);

        filmHelper = FilmHelper.getInstance(getApplicationContext());
        filmHelper.open();

        cekFav = filmHelper.isExist(film.getIdmoviedb());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_detail,menu);
        mMenu = menu;
        if(cekFav == 1){
            setLoveOptions(cekFav);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.addtoFav:
                long result = filmHelper.addFav(film);

                if (result > 0) {
                    film.setId((int) result);
                    setLoveOptions(1);
                    Toast.makeText(DetailActivity.this, getString(R.string.addfav), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }

                updateWidget();

                return true;

            case R.id.delFav:
                long delete = filmHelper.deleteFav(film.getIdmoviedb());

                if (delete > 0) {
                    setLoveOptions(0);
                    Toast.makeText(DetailActivity.this, getString(R.string.delfav), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }

                updateWidget();

                return true;

            case android.R.id.home:
                finish();

                return true;
        }
        return false;
    }

    public void setLoveOptions(int status){
        if(status == 1){
            mMenu.findItem(R.id.addtoFav).setVisible(false);
            mMenu.findItem(R.id.delFav).setVisible(true);
        }
        else{
            mMenu.findItem(R.id.addtoFav).setVisible(true);
            mMenu.findItem(R.id.delFav).setVisible(false);
        }
    }

    private void updateWidget(){
        Intent i = new Intent(this, FavWidget.class);
        i.setAction(FavWidget.UPDATE_WIDGET);
        sendBroadcast(i);
    }
}
