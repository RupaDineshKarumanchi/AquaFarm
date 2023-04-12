package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
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

import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton timedate;
    Spinner siteListSpinner;
    EditText pondno,breed,pondDate;
    Button savePond;
    Boolean EditTextEmptyHold;
    String siteHolder,pondHolder,pondDateHolder,breedTypeHolder;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pond);

        timedate = findViewById(R.id.datetime);
        pondDate = findViewById(R.id.pond_date);
        siteListSpinner = findViewById(R.id.site_loc);
        pondno = findViewById(R.id.pond_no);
        breed = findViewById(R.id.breed);
        savePond = findViewById(R.id.save_pond);

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        List<String> siteList =  dbHelper.getSiteList();

        ArrayAdapter<String> siteListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_selected_item, siteList);

        siteListAdapter.setDropDownViewResource(R.layout.spinner_item);

        siteListSpinner.setAdapter(siteListAdapter);
        siteListSpinner.setOnItemSelectedListener(this);

        timedate.setOnClickListener(view -> {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(c.getTime());
            pondDate.setText(date);

        });

        pondDate.setOnClickListener(view -> {
            Calendar getDate = Calendar.getInstance();
            int cDay = getDate.get(Calendar.DAY_OF_MONTH);
            int cMonth = getDate.get(Calendar.MONTH);
            int cYear = getDate.get(Calendar.YEAR);

            DatePickerDialog datePicker =
                    new DatePickerDialog(AddPondActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override

                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            pondDate.setText(year+ "-" + month + "-" +dayOfMonth);

                        }
                    },cYear,cMonth, cDay);
            datePicker.show();
        });



        savePond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextStatus();

                InsertDataIntoSQLiteDatabase();

                EmptyEditTextAfterDataInsert();

            }
        });
    }

    public void CheckEditTextStatus(){

        siteHolder = siteListSpinner.getSelectedItem().toString();
        pondHolder = pondno.getText().toString();
        pondDateHolder = pondDate.getText().toString();
        breedTypeHolder = breed.getText().toString();

        if(TextUtils.isEmpty(siteHolder) || TextUtils.isEmpty(pondHolder) || TextUtils.isEmpty(pondDateHolder) || TextUtils.isEmpty(breedTypeHolder) ){
            EditTextEmptyHold = false ;
        }
        else {
            EditTextEmptyHold = true;
        }
    }

    public void InsertDataIntoSQLiteDatabase(){

        if(EditTextEmptyHold == true)
        {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            if(dbHelper.insertPond(siteHolder, pondHolder, pondDateHolder, breedTypeHolder)) {

                Toast.makeText(AddPondActivity.this,"Saved Successfully", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(AddPondActivity.this,"Failed to save", Toast.LENGTH_LONG).show();
            }
        }

        else {

            Toast.makeText(AddPondActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    public void EmptyEditTextAfterDataInsert(){

        pondno.getText().clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedSite = siteListSpinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}