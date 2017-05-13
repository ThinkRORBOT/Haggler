package com.example.ming.haggler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import static com.example.ming.haggler.R.id.loginButton;


//Allows users to choose the mode the application goes in.

public class MainActivity extends AppCompatActivity {
    private Button essentialsButton;
    private Button marketButton;
    private Button loginButton;
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

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
