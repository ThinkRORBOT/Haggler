package com.example.ming.haggler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.ming.haggler.R.id.loginButton;


//Allows users to choose the mode the application goes in.

public class MainActivity extends AppCompatActivity {
    private Button essentialsButton;
    private Button marketButton;
    private Button loginButton;
    private Button helpButton;
    public static boolean emailSuccess = false;
    public static boolean accountcreate = false;
    public static boolean essential = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);

        //initialises the content of the screen
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        essentialsButton = (Button) findViewById(R.id.essentialsButton);
        marketButton = (Button) findViewById(R.id.marketButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        helpButton = (Button) findViewById(R.id.helpButton);

        if (emailSuccess) {
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_LONG).show();
            emailSuccess = false;
        }
        if (accountcreate) {
            Toast.makeText(this, "Successfully created account", Toast.LENGTH_LONG).show();
            accountcreate = false;
        }

    }

    public void launchEssential(View view) {
        //starts essential items cities activity
        essential = true;
        Intent intent = new Intent(this, EssentialCitiesActivity.class);
        startActivity(intent);
    }

    public void launchMarket(View view) {
        //starts market items cities activity
        essential = false;
        Intent intent = new Intent(this, MarketCitiesActivity.class);
        startActivity(intent);
    }

    public boolean getState(){
        return this.essential;
    }
    //starts login activity
    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    //starts help activity
    public void help(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
