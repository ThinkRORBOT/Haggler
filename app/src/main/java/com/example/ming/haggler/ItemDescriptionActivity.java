package com.example.ming.haggler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ItemDescriptionActivity extends AppCompatActivity {
    private String city;
    private String product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        product = intent.getStringExtra("product");
    }
}
