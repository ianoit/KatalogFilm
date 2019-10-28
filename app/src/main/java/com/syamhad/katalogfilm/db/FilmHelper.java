package com.syamhad.katalogfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.syamhad.katalogfilm.model.Film;
import com.syamhad.katalogfilm.widget.FavWidget;
import com.syamhad.katalogfilm.widget.StackRemoteViewsFactory;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.JENIS;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.RATING;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.BANNER;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.RELEASE;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.LANGUAGE;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.DESCRIPTION;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.IMAGE;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.IDMOVIEDB;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.TITLE;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.TABLE_FAV;

public class FilmHelper {
    public static final String DATABASE_TABLE = TABLE_FAV;
    private static DatabaseHelper dataBaseHelper;
    private static FilmHelper INSTANCE;
    private static SQLiteDatabase database;

    private FilmHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FilmHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FilmHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }
    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Film> getAllFav(String jenis) {
        ArrayList<Film> arrayList = new ArrayList<>();
        String whereClause = JENIS + " = ?";
        String[] whereArgs = new String[] {
                jenis
        };
        Cursor cursor = database.query(DATABASE_TABLE, null,
                whereClause,
                whereArgs,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Film film;
        if (cursor.getCount() > 0) {
            do {
                film = new Film();
                film.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                film.setIdmoviedb(cursor.getInt(cursor.getColumnIndexOrThrow(IDMOVIEDB)));
                film.setJudul(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                film.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                film.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                film.setRilisdate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                film.setGambar(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE)));
                film.setBanner(cursor.getString(cursor.getColumnIndexOrThrow(BANNER)));
                film.setVoteavg(cursor.getFloat(cursor.getColumnIndexOrThrow(RATING)));
                film.setJenis(cursor.getString(cursor.getColumnIndexOrThrow(JENIS)));
                arrayList.add(film);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public int isExist(int idmoviedb) {
        int result = 0;
        String whereClause = IDMOVIEDB + " = ?";
        String[] whereArgs = new String[] {
                Integer.toString(idmoviedb)
        };
        Cursor cursor = database.query(DATABASE_TABLE, null,
                whereClause,
                whereArgs,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                result = 1;
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return result;
    }

    public long addFav(Film film) {
        ContentValues args = new ContentValues();
        args.put(IDMOVIEDB, film.getIdmoviedb());
        args.put(TITLE, film.getJudul());
        args.put(DESCRIPTION, film.getKeterangan());
        args.put(LANGUAGE, film.getLanguage());
        args.put(RELEASE, film.getRilisdate());
        args.put(IMAGE, film.getGambar());
        args.put(BANNER, film.getBanner());
        args.put(RATING, film.getVoteavg());
        args.put(JENIS, film.getJenis());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFav(int id) {
        return database.delete(TABLE_FAV, IDMOVIEDB + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
