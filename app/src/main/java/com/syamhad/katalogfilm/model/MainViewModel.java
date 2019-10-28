package com.syamhad.katalogfilm.model;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.syamhad.katalogfilm.MainTabActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    public static final String API_KEY = "24b8b9b4ebf2f2e438b7e65d5d1498c5";
    private MutableLiveData<ArrayList<Film>> listFilm = new MutableLiveData<>();

    public LiveData<ArrayList<Film>> getFilm() {
        return listFilm;
    }

    public void setFilm(final String cat, final String query) {
        String url = "https://api.themoviedb.org/3/discover/" + cat + "?api_key=" + API_KEY;
        if(!query.equalsIgnoreCase(MainTabActivity.All)){
            url = "https://api.themoviedb.org/3/search/" + cat + "?api_key=" + API_KEY + "&language=en-US&query=" + query;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Film> daftarFilm = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject film = list.getJSONObject(i);
                        Film filmitem = new Film(film, cat);
                        daftarFilm.add(filmitem);
                    }
                    listFilm.postValue(daftarFilm);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void setFilmToday() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + date + "&primary_release_date.lte=" + date;
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Film> daftarFilm = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject film = list.getJSONObject(i);
                        Film filmitem = new Film(film, "movie");
                        daftarFilm.add(filmitem);
                    }
                    listFilm.postValue(daftarFilm);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
}
