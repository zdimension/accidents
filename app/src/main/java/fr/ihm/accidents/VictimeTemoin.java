package fr.ihm.accidents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VictimeTemoin extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victime_temoin);

        final TextView victimeTemoinTextView = (TextView) findViewById(R.id.textView);

        final Button button = findViewById(R.id.button);
        button.setBackgroundColor(Color.GRAY);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button button2 = findViewById(R.id.button2);
        button2.setBackgroundColor(Color.MAGENTA);
        button2.setTextColor(Color.BLACK);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentHumainEnDanger = new Intent(VictimeTemoin.this, HumainEnDanger.class);
                startActivity(intentHumainEnDanger);
            }
        });

        final Button button3 = findViewById(R.id.button3);
        button3.setBackgroundColor(Color.BLUE);
        button3.setTextColor(Color.WHITE);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentMainActivity = new Intent(VictimeTemoin.this, MainActivity.class);
                startActivity(intentMainActivity);
            }
        });
    }
}