package fr.ihm.accidents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VictimeTemoin extends AppCompatActivity
{

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

        /*final Button button3 = findViewById(R.id.button3);
        button3.setBackgroundColor(Color.BLUE);
        button3.setTextColor(Color.WHITE);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentMainActivity = new Intent(VictimeTemoin.this, MainActivity.class);
                startActivity(intentMainActivity);
            }
        });*/

        final Button testNotification = findViewById(R.id.test_notification);
        testNotification.setOnClickListener(v ->
        {
            Intent intentMainActivity = new Intent(VictimeTemoin.this,
                NotificationActivity.class);
            startActivity(intentMainActivity);
        });
    }
}