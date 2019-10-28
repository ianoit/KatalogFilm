package com.syamhad.katalogfilm.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.syamhad.katalogfilm.widget.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
