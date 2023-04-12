package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FeedingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner siteListSpinner, pondListSpinner, feedListSpinner;
    EditText dateTimeEditText, feedQtyEditText;
    Button save, clear;
    ImageButton getTime;
    int cYear, cMonth, cDay, cHour, cMinute;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding);

        siteListSpinner = findViewById(R.id.site_loc_feeding);
        pondListSpinner = findViewById(R.id.pond_no_feeding);
        feedListSpinner = findViewById(R.id.feed_size_feeding);
        dateTimeEditText = findViewById(R.id.date_time_feeding);
        feedQtyEditText = findViewById(R.id.feed_qty_feeding);
        getTime = findViewById(R.id.datetime_feeding);
        save = findViewById(R.id.save_feeding);
        clear = findViewById(R.id.clear_feeding);

        DBHelper dbHelper = new DBHelper(getApplicationContext());


        List<String> siteList =  dbHelper.getSiteList();
        ArrayAdapter<String> siteListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, siteList);
        siteListAdapter.setDropDownViewResource(R.layout.spinner_item);
        siteListSpinner.setAdapter(siteListAdapter);

        List<String> pondList;
        try {
            pondList = dbHelper.getPondList(siteListSpinner.getSelectedItem().toString());
            ArrayAdapter<String> pondListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, pondList);
            pondListAdapter.setDropDownViewResource(R.layout.spinner_item);
            pondListSpinner.setAdapter(pondListAdapter);
        } catch (Exception e){
            pondList = null;
        }

        List<String> feedList;
        try {
            feedList = dbHelper.getFeedList();
            ArrayAdapter<String> feedListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, feedList);
            feedListAdapter.setDropDownViewResource(R.layout.spinner_item);
            feedListSpinner.setAdapter(feedListAdapter);
        } catch (Exception e){
            feedList = null;
        }


        siteListSpinner.setOnItemSelectedListener(this);

        save.setOnClickListener(view -> {

            InsertDataIntoSQLiteDatabase();

            EmptyEditTextAfterDataInsert();

        });

        clear.setOnClickListener(view ->{
            dateTimeEditText.getText().clear();
            feedQtyEditText.getText().clear();
        });

        getTime.setOnClickListener(view -> {

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = df.format(c.getTime());
            dateTimeEditText.setText(date);
        });

        dateTimeEditText.setOnClickListener(view -> {

            Calendar getDate = Calendar.getInstance();
            cDay = getDate.get(Calendar.DAY_OF_MONTH);
            cMonth = getDate.get(Calendar.MONTH);
            cYear = getDate.get(Calendar.YEAR);
            cHour = getDate.get(Calendar.HOUR_OF_DAY);
            cMinute = getDate.get(Calendar.MINUTE);

            DatePickerDialog datePicker =
                    new DatePickerDialog(FeedingActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override

                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            timePickerDialog =
                                    new TimePickerDialog(FeedingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            String min = "";
                                            if(minute < 10)
                                            {
                                                min = "0" + Integer.toString(minute);
                                            }
                                            else{
                                                min = Integer.toString(minute);
                                            }
                                            dateTimeEditText.setText(year+ "-" + month + "-" +dayOfMonth + " " +hourOfDay+":"+min);
                                        }
                                    },cHour,cMinute,false);
                            timePickerDialog.show();
                        }
                    },cYear,cMonth,cDay);
            datePicker.show();
        });

    }

    private void EmptyEditTextAfterDataInsert() {

        feedQtyEditText.getText().clear();

    }

    private void InsertDataIntoSQLiteDatabase() {

        String siteHolder = siteListSpinner.getSelectedItem().toString();
        String pondHolder = pondListSpinner.getSelectedItem().toString();
        String dateTimeHolder = dateTimeEditText.getText().toString() + ":00";
        String feedNoHolder = feedListSpinner.getSelectedItem().toString();
        String feedQtyHolder = feedQtyEditText.getText().toString();


        if(TextUtils.isEmpty(siteHolder) || TextUtils.isEmpty(pondHolder) || TextUtils.isEmpty(dateTimeHolder) || TextUtils.isEmpty(feedNoHolder) || TextUtils.isEmpty(feedQtyHolder)){

            Toast.makeText(FeedingActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();


        }
        else {

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            if(dbHelper.insertFeeding(siteHolder, pondHolder, dateTimeHolder, feedNoHolder, feedQtyHolder)) {

                Toast.makeText(FeedingActivity.this,"Saved Successfully", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(FeedingActivity.this,"Failed to save", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<String> pondList = dbHelper.getPondList(siteListSpinner.getSelectedItem().toString());
        ArrayAdapter<String> pondListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, pondList);
        pondListAdapter.setDropDownViewResource(R.layout.spinner_item);
        pondListSpinner.setAdapter(pondListAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}