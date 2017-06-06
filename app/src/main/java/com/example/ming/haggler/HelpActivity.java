package com.example.ming.haggler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HelpActivity extends AppCompatActivity {
    private TextView titleText;
    private TextView descriptionText1;
    private TextView descriptionText2;
    private TextView descriptionText3;
    private TextView descriptionText4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        titleText = (TextView) findViewById(R.id.textView);
        descriptionText1 = (TextView) findViewById(R.id.descriptionText1);
        descriptionText2 = (TextView) findViewById(R.id.descriptionText2);
        descriptionText3 = (TextView) findViewById(R.id.descriptionText3);
        descriptionText4 = (TextView) findViewById(R.id.descriptionText4);
    }
}
