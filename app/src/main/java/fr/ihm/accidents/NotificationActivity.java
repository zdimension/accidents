package fr.ihm.accidents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static fr.ihm.accidents.DemarageAplication.*;

public class NotificationActivity extends AppCompatActivity {
    private int notificationNumber =0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation);

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView)findViewById(R.id.seekBarValue);

        final Button notificationButton = findViewById(R.id.send_notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int progress = seekBar.getProgress()*3;
                if(progress<50)
                {
                    String title = "Un accdient très proche de vous!";
                    String text = "Un accdient à été repoté a une distance de " + progress + " m de vous";
                    sendNotificationOnChannel(title, text, CHANNEL_ID_HIGH, NotificationCompat.PRIORITY_HIGH);
                }
                else if(progress<100){
                    String title = "Un accdient près de vous!";
                    String text = "Un accdient à été repoté a une distance de " + progress + " m de vous";
                    sendNotificationOnChannel(title, text, CHANNEL_ID_DEFAULT, NotificationCompat.PRIORITY_DEFAULT);
                }
                else if(progress<250){
                    String title = "Un accdient proche de vous";
                    String text = "Un accdient à été repoté a une distance de " + progress + " m de vous";
                    sendNotificationOnChannel(title, text, CHANNEL_ID_LOW, NotificationCompat.PRIORITY_LOW);
                }
            }
        });

        final Button PreviousButton = findViewById(R.id.previous_button);
        PreviousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentMainActivity = new Intent(NotificationActivity.this, VictimeTemoin.class);
                startActivity(intentMainActivity);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String textset = "Distance = "+ progress*3 +" m";
                seekBarValue.setText(textset);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void sendNotificationOnChannel(String title, String description, String channelID, int priority){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), channelID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(priority);
        NotificationManagerCompat.from(this).notify(notificationNumber++ , notification.build());
    }
}