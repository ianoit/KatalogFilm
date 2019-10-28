package com.syamhad.katalogfilm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.syamhad.katalogfilm.db.DatabaseContract.FavColumns;

import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.TABLE_FAV;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbfavorit";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_FAV,
            FavColumns._ID,
            FavColumns.IDMOVIEDB,
            FavColumns.TITLE,
            FavColumns.DESCRIPTION,
            FavColumns.LANGUAGE,
            FavColumns.RELEASE,
            FavColumns.IMAGE,
            FavColumns.BANNER,
            FavColumns.RATING,
            FavColumns.JENIS
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        onCreate(db);
    }
}
