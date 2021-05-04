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

public class HumainEnDanger extends AppCompatActivity implements LocationListener
{

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humain_en_danger);

        final TextView humainEnDangerTextView = findViewById(R.id.textView2);

        final Button button4 = findViewById(R.id.button4);
        button4.setBackgroundColor(Color.RED);
        button4.setTextColor(Color.BLACK);
        button4.setOnClickListener(v ->
        {
            Intent intentWitnessYesCallActivity = new Intent(HumainEnDanger.this,
                WitnessYesCallActivity.class);

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

            startActivity(intentWitnessYesCallActivity);
        });

        final Button button5 = findViewById(R.id.button5);
        button5.setBackgroundColor(Color.GREEN);
        button5.setTextColor(Color.BLACK);
        button5.setOnClickListener(v ->
        {
            Intent natureAccident = new Intent(HumainEnDanger.this, AccidentNature.class);
            HumainEnDanger.this.startActivity(natureAccident);
        });

        final Button button6 = findViewById(R.id.button6);
        button6.setBackgroundColor(Color.BLUE);
        button6.setTextColor(Color.WHITE);
        button6.setOnClickListener(v ->
        {
            Intent intentVictimeTemoin = new Intent(HumainEnDanger.this, VictimeTemoin.class);
            startActivity(intentVictimeTemoin);
        });
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