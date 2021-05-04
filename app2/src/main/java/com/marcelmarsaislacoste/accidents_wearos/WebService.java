package com.marcelmarsaislacoste.accidents_wearos;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebService extends Service
{

    public static int last = 0;
    private HandlerThread handlerThread;
    private Handler handler;
    private int notificationNumber;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        NotificationCompat.Builder notification =
            new NotificationCompat.Builder(getApplicationContext(), "channel 3")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("SERVICE")
                .setContentText("Web service")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notif = notification.build();
        // Start the foreground service immediately.
        startForeground((int) System.currentTimeMillis(), notif);

        handlerThread = new HandlerThread("MyLocationThread");
        handlerThread.setDaemon(true);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        // Every other call is up to you. You can update the location,
        // do whatever you want after this part.

        // Sample code (which should call handler.postDelayed()
        // in the function as well to create the repetitive task.)
        handler.postDelayed(this::myFuncToUpdateLocation, 1);

        return START_STICKY;
    }

    private void myFuncToUpdateLocation()
    {
        try
        {
            URL Url = new URL("https://domino.zdimension.fr/web/ihm.php?after=" + last);
            URLConnection urlConnection = Url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = bufferedReader.readLine();
            JSONArray obj = new JSONArray(str);
            last += obj.length();
            //Log.i("SERVICE", obj.toString());
            for (int i = 0; i < obj.length(); i++)
            {
                JSONObject item = obj.getJSONObject(i);
                Init.accidents.add(item);
                Init.accidentsNotications.add(item);
                // Log.d("1111", "In for: " + item.getDouble("latitude") + "" + item.getDouble("longitude") + "");
            }
            // Log.d("2222", "Not in for");
            //return str;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        handler.postDelayed(this::myFuncToUpdateLocation, 1);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        handlerThread.quit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }
}