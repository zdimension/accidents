package fr.ihm.accidents;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements LocationListener
{

    public static final int REQUEST_GPS = 0;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            this.goToVictimeTemoinActivity();
        }

        final TextView geolocalisationTextView = findViewById(R.id.text_view_id);

        final Button button = findViewById(R.id.button_id);
        button.setBackgroundColor(Color.GREEN);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(v ->
        {
            ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_GPS);
            /*Intent intentVictimeTemoin = new Intent(MainActivity.this, VictimeTemoin.class);
            startActivity(intentVictimeTemoin);*/
        });

        /*final Button button2 = findViewById(R.id.button_id2);
        button2.setBackgroundColor(Color.RED);
        button2.setTextColor(Color.BLACK);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentVictimeTemoin = new Intent(MainActivity.this, VictimeTemoin.class);
                startActivity(intentVictimeTemoin);
            }
        });
    }*/


        startForegroundService(new Intent(this, WebService.class));
    }

    private void goToVictimeTemoinActivity()
    {
        Intent intentVictimeTemoin = new Intent(MainActivity.this, VictimeTemoin.class);

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
                    wr.write("accident=0&distance=50&longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude() + "&id=");
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
        WebService.last++;

        startActivity(intentVictimeTemoin);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_GPS)
        {
            if (permissions.length > 0)
            {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        Toast toast = Toast.makeText(this, "La géocalisation permet de savoir " +
                            "plus rapidement où vous êtes et ainsi d'aider les ambulanciers.",
                            Toast.LENGTH_LONG); //TODO vraiment ambulanciers ?
                        toast.show();
                    }
                }
                this.goToVictimeTemoinActivity();
            }
        }
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