package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView feedStockRecyclerView;
    FeedStockAdapter adapter;
    Spinner siteListSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        feedStockRecyclerView = findViewById(R.id.feed_stock_recycler_view);
        siteListSpinner = findViewById(R.id.stock_site_loc);

        setSiteSpinner();

        setRecyclerView();

    }

    private void setSiteSpinner() {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<String> siteList =  dbHelper.getSiteList();
        ArrayAdapter<String> siteListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, siteList);
        siteListAdapter.setDropDownViewResource(R.layout.spinner_item);
        siteListSpinner.setAdapter(siteListAdapter);
        siteListSpinner.setOnItemSelectedListener(this);

    }

    private void setRecyclerView() {

        try {
            feedStockRecyclerView.setHasFixedSize(true);
            feedStockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new FeedStockAdapter(this, getList());
            feedStockRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            return;
        }

    }

    private List<FeedStockModel> getList() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<FeedStockModel> feedStockList = dbHelper.getStock(siteListSpinner.getSelectedItem().toString());
        return feedStockList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        setRecyclerView();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
