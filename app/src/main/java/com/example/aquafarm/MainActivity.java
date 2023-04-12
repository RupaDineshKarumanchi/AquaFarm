package com.example.aquafarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button addNew, feeding, feedStock, feedUsage, logout;
    TextView username;
    private FirebaseAuth mAuth;
    SQLiteDatabase sqLiteDatabaseObj;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNew = findViewById(R.id.add_new);
        feeding = findViewById(R.id.feeding);
        feedStock = findViewById(R.id.feed_stock);
        feedUsage = findViewById(R.id.growth);
        logout = findViewById(R.id.logout);
        username = findViewById(R.id.username);


        mAuth = FirebaseAuth.getInstance();



        addNew.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddNewActivity.class);
            startActivity(i);
        });

        feeding.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), FeedingActivity.class);
            startActivity(i);
        });

        feedStock.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), StockMenuActivity.class);
            startActivity(i);
        });

        feedUsage.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), FeedUsageActivity.class);
            startActivity(i);
        });

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();


            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
            googleSignInClient.signOut();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(currentUser == null && account == null){
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            String uid = mAuth.getUid();
            String str="";
            if(account != null){
                str = account.getDisplayName();
            } else if(currentUser != null) {
                str = currentUser.getEmail();
                str = str.substring(0, str.indexOf('@'));
            }
            username.setText(str);

            SQLiteDataBaseBuild(uid);

        }
    }

    public void SQLiteDataBaseBuild(String uid){

        DBHelper dbHelper = new DBHelper(getApplicationContext(), uid);

    }


}