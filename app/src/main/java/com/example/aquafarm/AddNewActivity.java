package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AddNewActivity extends AppCompatActivity {

    Button addSite, addPond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        addSite = findViewById(R.id.add_site_button);
        addPond = findViewById(R.id.add_pond_button);

        addSite.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddSiteActivity.class);
            startActivity(i);
        });

        addPond.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddPondActivity.class);
            startActivity(i);
        });

    }
}