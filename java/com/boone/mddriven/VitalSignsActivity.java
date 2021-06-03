package com.boone.mddriven;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VitalSignsActivity extends AppCompatActivity {

    Button heartSoundsBtn;
    Button lungsSoundsBtn;
    Button EKGBtn;
    Button bloodPressureBtn;
    Button pulseRateBtn;
    Button oxygenSaturationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);


        heartSoundsBtn = findViewById(R.id.heartSoundsBtn);

        heartSoundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this,HeartSoundsActivity.class);
                startActivity(intent);
            }
        });

        lungsSoundsBtn = findViewById(R.id.lungsSoundsBtn);

        lungsSoundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this, LungsSoundActivity.class);
                startActivity(intent);
            }
        });

        EKGBtn = findViewById(R.id.EKGBtn);

        EKGBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this, EKGActivity.class);
                startActivity(intent);
            }
        });

        bloodPressureBtn = findViewById(R.id.bloodPressureBtn);

        bloodPressureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this, BloodPressureActivity.class);
                startActivity(intent);
            }
        });

        pulseRateBtn = findViewById(R.id.pulseRateBtn);

        pulseRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this, PulseRateActivity.class);
                startActivity(intent);
            }
        });

        oxygenSaturationBtn = findViewById(R.id.oxygenSaturationBtn);

        oxygenSaturationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VitalSignsActivity.this, OxygenSaturationActivity.class);
                startActivity(intent);
            }
        });
    }
}

