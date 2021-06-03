package com.boone.mddriven;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


        Button vitalSignsBtn;
        Button messagesBtn;
        Button profileBtn;
        Button recordBtn;
        Button settingsBtn;
        Button activityLogBtn;
        Button btnLogout;
        TextView userTextView;



    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            btnLogout = findViewById(R.id.logout);
            userTextView = findViewById(R.id.loggedInTextView);

            userTextView.setText("Welcome " + getIntent().getStringExtra("name"));

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        recordBtn = findViewById(R.id.recordBtn);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent4);
            }
        });

        vitalSignsBtn = findViewById(R.id.vitalSignsBtn);

        vitalSignsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VitalSignsActivity.class);
                startActivity(intent);
            }
        });

        messagesBtn = findViewById(R.id.messagesBtn);

        messagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, MessagesActivity.class);
                startActivity(intent1);
            }
        });


        profileBtn = findViewById(R.id.profileBtn);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent2);
            }
        });

        settingsBtn = findViewById(R.id.settingsBtn);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent3);
            }
        });

        activityLogBtn = findViewById(R.id.activityLogBtn);

        activityLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this, ActivityLogActivity.class);
                startActivity(intent4);
            }
        });

    }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }








    }
