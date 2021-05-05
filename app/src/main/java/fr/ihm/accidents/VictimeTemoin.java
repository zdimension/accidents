package fr.ihm.accidents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class VictimeTemoin extends AppCompatActivity implements LocationListener
{

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victime_temoin);

        final TextView victimeTemoinTextView = findViewById(R.id.textView);

        final Button button = findViewById(R.id.button);
        button.setBackgroundColor(Color.GRAY);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(v ->
        {
            Intent intentVictimCallActivity = new Intent(VictimeTemoin.this,
                VictimCallActivity.class);

            @SuppressLint("StaticFieldLeak") AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>()
            {
                @Override
                protected Object doInBackground(Object[] objects)
                {
                    BufferedReader reader = null;
                    String text = "";

                    // Send data
                    try
                    {
                        // Defined URL  where to send data
                        URL url = new URL("https://domino.zdimension.fr/web/ihm.php");

                        // Send POST data request

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        // OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                        // locationTemp = new LatLng(location.getLatitude(), location.getLongitude());
                        wr.write("accident=0&distance=50&longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude() + "&id=" + DemarageAplication.ANDROID_ID);
                        wr.flush();

                        // Get the server response

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        // Read Server Response
                        while((line = reader.readLine()) != null)
                        {
                            // Append server response in string
                            sb.append(line + "\n");
                        }
                        text = sb.toString();
                        Log.d("SERV", text);
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            reader.close();
                        }
                        catch(Exception ex) {}
                    }
                    return null;
                }
            };

            task.execute();
            // WebService.last++;

            startActivity(intentVictimCallActivity);
        });

        final Button button2 = findViewById(R.id.button2);
        button2.setBackgroundColor(Color.MAGENTA);
        button2.setTextColor(Color.BLACK);
        button2.setOnClickListener(v ->
        {
            Intent intentHumainEnDanger = new Intent(VictimeTemoin.this, HumainEnDanger.class);
            startActivity(intentHumainEnDanger);
        });

        final Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            Intent intentMainActivity = new Intent(VictimeTemoin.this, MapsActivity.class);
            startActivity(intentMainActivity);
        });

        /*final Button button3 = findViewById(R.id.button3);
        button3.setBackgroundColor(Color.BLUE);
        button3.setTextColor(Color.WHITE);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentMainActivity = new Intent(VictimeTemoin.this, MainActivity.class);
                startActivity(intentMainActivity);
            }
        });*/

        /*final Button testNotification = findViewById(R.id.test_notification);
        testNotification.setOnClickListener(v ->
        {
            Intent intentMainActivity = new Intent(VictimeTemoin.this,
                NotificationActivity.class);
            startActivity(intentMainActivity);
        });*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        DemarageAplication.checkPermissions(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (DemarageAplication.lm != null) {
            DemarageAplication.lm.removeUpdates(this);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
        try
        {
            DemarageAplication.locationChanged(this, location);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}