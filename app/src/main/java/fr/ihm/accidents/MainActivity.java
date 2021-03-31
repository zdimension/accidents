package fr.ihm.accidents;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView geolocalisationTextView = (TextView) findViewById(R.id.text_view_id);
        geolocalisationTextView.setText("Autorisez-vous la géolocalisation ?");

        final Button button = findViewById(R.id.button_id);
        button.setBackgroundColor(Color.GREEN);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });

        final Button button2 = findViewById(R.id.button_id2);
        button2.setBackgroundColor(Color.RED);
        button2.setTextColor(Color.BLACK);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });
    }
}