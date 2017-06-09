package com.example.ming.haggler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewMarketItemActivity extends AppCompatActivity {
    private TextView titleText;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Button updateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_market_item);

        titleText = (TextView) findViewById(R.id.newItemTitle);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        updateButton = (Button) findViewById(R.id.updateButton);

    }
}
