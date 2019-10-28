package com.syamhad.katalogfilm.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.syamhad.katalogfilm.MainTabActivity;
import com.syamhad.katalogfilm.R;
import com.syamhad.katalogfilm.model.MainViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    public static final int ID_REPEATING = 101;
    public static final int ID_RELEASE = 102;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(EXTRA_TYPE, 0);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = "Favorite FIlm";

        Log.v("jajal1", "yaaaaaa"+message);

        if(type == ID_REPEATING){
            showAlarmNotification(context, title, message, ID_REPEATING);
        }
        else if(type == ID_RELEASE){
            getReleaseToday(context, message);
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        Intent intent = new Intent(context, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_film)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setRepeatingAlarm(Context context) {
        String title = "Favorite FIlm";
        String contenttext = "See ur fav film again";

        long alarmtime;
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, 7);
        target.set(Calendar.MINUTE, 0);
        target.set(Calendar.SECOND, 0);

        if(target.getTimeInMillis() <= now.getTimeInMillis()) {
            alarmtime = target.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
        }
        else {
            alarmtime = target.getTimeInMillis();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, contenttext);
        intent.putExtra(EXTRA_TYPE, ID_REPEATING);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmtime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm set up for "+title, Toast.LENGTH_SHORT).show();
    }

    public void setReleaseAlarm(Context context) {
        String title = "Release Today";
        String contenttext = "Release Today";

        long alarmtime;
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, 8);
        target.set(Calendar.MINUTE, 0);
        target.set(Calendar.SECOND, 0);

        if(target.getTimeInMillis() <= now.getTimeInMillis()) {
            alarmtime = target.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
        }
        else {
            alarmtime = target.getTimeInMillis();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, contenttext);
        intent.putExtra(EXTRA_TYPE, ID_RELEASE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmtime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, "Repeating alarm set up for "+title, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, int type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = (type == ID_REPEATING) ? ID_REPEATING : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public void getReleaseToday(Context context, final String message){
        final Context mContext = context;
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + MainViewModel.API_KEY + "&primary_release_date.gte=" + date + "&primary_release_date.lte=" + date;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String strresult = "";
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    if(list.length() > 2){
                        for (int i = 0; i < 2; i++) {
                            JSONObject film = list.getJSONObject(i);
                            strresult = strresult+film.getString("original_title")+", ";
                        }
                        strresult = strresult+" and "+(list.length()-2)+" more films";
                    }
                    else{
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject film = list.getJSONObject(i);
                            strresult = strresult+film.getString("original_title")+", ";
                        }
                    }

                    showAlarmNotification(mContext, message, strresult, ID_RELEASE);
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
