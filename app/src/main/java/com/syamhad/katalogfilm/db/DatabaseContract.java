package com.syamhad.katalogfilm.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.syamhad.katalogfilm";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class FavColumns implements BaseColumns {
        public static String TABLE_FAV = "favorit";
        static String IDMOVIEDB = "idmoviedb";
        static String TITLE = "title";
        static String DESCRIPTION = "description";
        static String LANGUAGE = "language";
        static String RELEASE = "release";
        static String IMAGE = "image";
        public static String BANNER = "banner";
        static String RATING = "rating";
        static String JENIS = "jenis";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAV)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
