package com.syamhad.katalogfilm.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.syamhad.katalogfilm.db.FilmHelper;

import static com.syamhad.katalogfilm.db.DatabaseContract.AUTHORITY;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.TABLE_FAV;

public class FilmProvider extends ContentProvider {
    private static final int FILM = 1;
    private static final int FILM_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FilmHelper filmHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV, FILM);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV + "/#", FILM_ID);
    }

    @Override
    public boolean onCreate() {
        filmHelper = FilmHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        filmHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FILM:
                cursor = filmHelper.queryProvider();
                break;
            case FILM_ID:
                cursor = filmHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
