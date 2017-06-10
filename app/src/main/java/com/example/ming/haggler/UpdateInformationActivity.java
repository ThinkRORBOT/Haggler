package com.example.ming.haggler;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateInformationActivity extends AppCompatActivity {

    private  TextView titleTextView;
    private TextView priceTextView;
    private EditText priceText;
    private Button updateButton;
    private String value;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        titleTextView = (TextView) findViewById(R.id.titleText);
        priceTextView = (TextView) findViewById(R.id.priceView);
        priceText = (EditText) findViewById(R.id.priceText);
        updateButton = (Button) findViewById(R.id.updateButton);

        //loads a new instance of the database for access
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        SQLiteDatabase db = myDB.openDatabase();

        Intent intent = getIntent();
        value = intent.getStringExtra("product");
        city = intent.getStringExtra("city");

    }
}
