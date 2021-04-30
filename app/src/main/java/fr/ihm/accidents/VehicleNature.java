package fr.ihm.accidents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class VehicleNature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_nature);

        Button restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleNature.this, VictimeTemoin.class);
            startActivity(intent);
        });


        Button previousButton = this.findViewById(R.id.previous2);
        previousButton.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleNature.this, AccidentNature.class);
            startActivity(intent);
        });

        Button carButton = this.findViewById(R.id.car_nature);
        carButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

        Button truckButton = this.findViewById(R.id.truck_nature);
        truckButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

        Button trainButton = this.findViewById(R.id.train_nature);
        trainButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

        Button bikeButton = this.findViewById(R.id.bike_nature);
        bikeButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

        Button motorCycleButton = this.findViewById(R.id.motorcycle_nature);
        motorCycleButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

        Button otherButton = this.findViewById(R.id.other_nature);
        otherButton.setOnClickListener(v -> this.goToMoreDetailsActivity());

    }

    private void goToMoreDetailsActivity() {
        Intent intent = new Intent(VehicleNature.this, MoreDetails.class);
        startActivity(intent);
    }

}