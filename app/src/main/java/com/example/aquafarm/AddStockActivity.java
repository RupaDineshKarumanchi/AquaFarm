package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddStockActivity extends AppCompatActivity {

    Spinner siteListSpinner, feedListSpinner;
    EditText feedQtyEditText;
    Button save, clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        siteListSpinner = findViewById(R.id.add_stock_site_list);
        feedListSpinner = findViewById(R.id.add_stock_feed_number);
        feedQtyEditText = findViewById(R.id.add_stock_feed_qty);
        save = findViewById(R.id.add_stock_save_button);
        clear = findViewById(R.id.add_stock_clear_button);

        DBHelper dbHelper = new DBHelper(getApplicationContext());


        List<String> siteList =  dbHelper.getSiteList();
        ArrayAdapter<String> siteListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, siteList);
        siteListAdapter.setDropDownViewResource(R.layout.spinner_item);
        siteListSpinner.setAdapter(siteListAdapter);

        List<String> feedList;
        try {
            feedList = dbHelper.getFeedList();
            ArrayAdapter<String> feedListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, feedList);
            feedListAdapter.setDropDownViewResource(R.layout.spinner_item);
            feedListSpinner.setAdapter(feedListAdapter);
        } catch (Exception e){
            feedList = null;
        }

        save.setOnClickListener(view -> {

            InsertDataIntoSQLiteDatabase();

            EmptyEditTextAfterDataInsert();

        });

        clear.setOnClickListener(view ->{

        });


    }

    private void EmptyEditTextAfterDataInsert() {

        feedQtyEditText.getText().clear();

    }

    private void InsertDataIntoSQLiteDatabase() {
        String siteHolder = siteListSpinner.getSelectedItem().toString();
        String feedNoHolder = feedListSpinner.getSelectedItem().toString();
        String feedQtyHolder = feedQtyEditText.getText().toString();

        if(TextUtils.isEmpty(siteHolder) || TextUtils.isEmpty(feedNoHolder) || TextUtils.isEmpty(feedQtyHolder)){

            Toast.makeText(AddStockActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();


        }
        else {

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            dbHelper.insertStock(siteHolder,feedNoHolder, feedQtyHolder);
            Toast.makeText(AddStockActivity.this,"Saved Successfully", Toast.LENGTH_LONG).show();
        }

    }
}