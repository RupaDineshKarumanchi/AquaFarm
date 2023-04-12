package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StockMenuActivity extends AppCompatActivity {

    Button addStock, stockAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_menu);

        addStock = findViewById(R.id.add_site_button);
        stockAvailable = findViewById(R.id.add_pond_button);

        addStock.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddStockActivity.class);
            startActivity(i);
        });

        stockAvailable.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), StockActivity.class);
            startActivity(i);
        });

    }
}