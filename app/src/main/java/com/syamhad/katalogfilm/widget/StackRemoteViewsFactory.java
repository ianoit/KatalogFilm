package com.syamhad.katalogfilm.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.syamhad.katalogfilm.R;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.BANNER;
import static com.syamhad.katalogfilm.db.DatabaseContract.FavColumns.CONTENT_URI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
        Bundle extras = new Bundle();
        extras.putInt(FavWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        Cursor cursor;

        final long identityToken = Binder.clearCallingIdentity();

        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                URL image = null;
                try {
                    image = new URL("https://image.tmdb.org/t/p/w500"+cursor.getString(cursor.getColumnIndexOrThrow(BANNER)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    mWidgetItems.add(BitmapFactory.decodeStream(image.openConnection().getInputStream()));
                } catch (IOException e) {
                    mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.noimage));
                    e.printStackTrace();
                }
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();

        Binder.restoreCallingIdentity(identityToken);
    }
}
