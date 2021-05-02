package fr.ihm.accidents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifcation);

        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView seekBarValue = findViewById(R.id.seekBarValue);

        final Button notificationButton = findViewById(R.id.send_notification_button);
        notificationButton.setOnClickListener(v ->
        {
            int progress = seekBar.getProgress() * 3;
            NotificationHelper.sendAccidentNotif(this, progress, "pas d'informations");
        });

        final Button PreviousButton = findViewById(R.id.previous_button);
        PreviousButton.setOnClickListener(v ->
        {
            Intent intentMainActivity = new Intent(NotificationActivity.this,
                VictimeTemoin.class);
            startActivity(intentMainActivity);
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                String textset = "Distance = " + progress * 3 + " m";
                seekBarValue.setText(textset);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }
}