package fr.ihm.accidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_GPS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            this.goToVictimeTemoinActivity();

        final TextView geolocalisationTextView = (TextView) findViewById(R.id.text_view_id);

        final Button button = findViewById(R.id.button_id);
        button.setBackgroundColor(Color.GREEN);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
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

    private void goToVictimeTemoinActivity() {
        Intent intentVictimeTemoin = new Intent(MainActivity.this, VictimeTemoin.class);
        startActivity(intentVictimeTemoin);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GPS) {
            if (permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast toast = Toast.makeText(this, "La géocalisation permet de savoir plus rapidement où vous êtes et ainsi d'aider les ambulanciers.", Toast.LENGTH_LONG); //TODO vraiment ambulanciers ?
                        toast.show();
                    }
                }
                this.goToVictimeTemoinActivity();
            }
        }
    }
}