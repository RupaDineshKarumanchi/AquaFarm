package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Context;
import android.os.Bundle;

public class AddSiteActivity extends AppCompatActivity {
    Spinner spinner;
    SQLiteDatabase sqLiteDatabaseObj;
    EditText siteLocation;
    Button save;
    Boolean EditTextEmptyHold;
    String siteHolder,SQLiteDataBaseQueryHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        save = findViewById(R.id.add_site_save_button);
        siteLocation = findViewById(R.id.add_site_loc);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String siteLoc = "error";

                CheckEditTextStatus();
                InsertDataIntoSQLiteDatabase();
                EmptyEditTextAfterDataInsert();

            }
        });


    }


    public void CheckEditTextStatus(){

        siteHolder = siteLocation.getText().toString() ;


        if(TextUtils.isEmpty(siteHolder)){

            EditTextEmptyHold = false ;

        }
        else {

            EditTextEmptyHold = true ;
        }
    }

    public void InsertDataIntoSQLiteDatabase(){

        if(EditTextEmptyHold == true)
        {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            if(dbHelper.insertSite(siteHolder)) {

                Toast.makeText(AddSiteActivity.this,"Saved Successfully", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(AddSiteActivity.this,"Failed to save", Toast.LENGTH_LONG).show();
            }
        }

        else {

            Toast.makeText(AddSiteActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    public void EmptyEditTextAfterDataInsert(){

        siteLocation.getText().clear();


    }





}