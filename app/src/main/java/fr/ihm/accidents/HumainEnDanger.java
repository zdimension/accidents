package fr.ihm.accidents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HumainEnDanger extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humain_en_danger);

        final TextView humainEnDangerTextView = (TextView) findViewById(R.id.textView2);

        final Button button4 = findViewById(R.id.button4);
        button4.setBackgroundColor(Color.GRAY);
        button4.setTextColor(Color.BLACK);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button button5 = findViewById(R.id.button5);
        button5.setBackgroundColor(Color.MAGENTA);
        button5.setTextColor(Color.BLACK);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button button6 = findViewById(R.id.button6);
        button6.setBackgroundColor(Color.BLUE);
        button6.setTextColor(Color.WHITE);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentVictimeTemoin = new Intent(HumainEnDanger.this, VictimeTemoin.class);
                startActivity(intentVictimeTemoin);
            }
        });
    }
}