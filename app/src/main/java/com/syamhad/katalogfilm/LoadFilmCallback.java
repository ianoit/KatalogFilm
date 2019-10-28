package com.syamhad.katalogfilm;

import com.syamhad.katalogfilm.model.Film;

import java.util.ArrayList;

public interface LoadFilmCallback {
    void preExecute();
    void postExecute(ArrayList<Film> film);
}
