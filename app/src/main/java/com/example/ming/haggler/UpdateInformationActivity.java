package com.example.ming.haggler;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UpdateInformationActivity extends AppCompatActivity {

    private  TextView titleTextView;
    private TextView priceTextView;
    private EditText priceText;
    private Button updateButton;
    private String value;
    private String city;
    private SQLiteDatabase db;
    private Float userEnteredPrice;
    private int enteredTime;
    private Double recommendedPrice;
    private int numInput;
    //converts time from seconds to days
    private static final int TIMEPASSED = 68400;
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

        db = myDB.openDatabase();

        Intent intent = getIntent();
        value = intent.getStringExtra("product");
        city = intent.getStringExtra("city");


    }

    private boolean checkPriceValidity() {
        if (priceText.getText().toString().matches("0")) {
            Toast.makeText(this, "Price cannot be zero", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private double getRecommendedPrice(int pandtArray[][]) {
        double finalPrice = 0;
        long totalweighting = 0;
        double weightedPrice = 0;
        for(int i = 0; i < pandtArray[0].length; i++) {
            weightedPrice += pandtArray[i][0] * (pandtArray[i][1]/TIMEPASSED);
            totalweighting += pandtArray[i][1]/TIMEPASSED;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        finalPrice= weightedPrice/totalweighting;
        finalPrice = Float.valueOf(df.format(finalPrice));
        return finalPrice;
    }
    public void updatePrice(View view) {

        if (checkPriceValidity()) {
            userEnteredPrice = Float.valueOf(priceText.getText().toString());
            boolean highPrice = false;
            boolean lowPrice = false;

            Cursor highPriceCursor = db.rawQuery("SELECT highPrice from CityProduct WHERE ProductKey = " + value + " AND CityKey = "+ city, null );
            Cursor lowPriceCursor = db.rawQuery("SELECT lowPrice from CityProduct WHERE ProductKey = " + value + " AND CityKey = "+ city, null );
            Cursor inputNum = db.rawQuery("SELECT inputnumber from CityProduct WHERE ProductKey = " + value + " AND CityKey = " + city, null );
            highPriceCursor.moveToFirst();
            lowPriceCursor.moveToFirst();
            inputNum.moveToFirst();
            Float cursorHighPriceV = Float.valueOf(highPriceCursor.getString(highPriceCursor.getColumnIndex("highPrice")));
            Float cursorLowPriceV = Float.valueOf(lowPriceCursor.getString(highPriceCursor.getColumnIndex("lowPrice")));
            int inputNumber = Integer.parseInt(inputNum.getString(inputNum.getColumnIndex("inputnumber")));

            Log.d("here", "here");
            if (cursorHighPriceV < userEnteredPrice) {
                highPrice = true;
            } else if (cursorLowPriceV > userEnteredPrice) {
                lowPrice = true;
            }



            Cursor historicalPriceAndTime = db.rawQuery("SELECT * From ProductTime WHERE ProductKey = " + value + " AND CityKey = " + city, null);
            enteredTime = (int) System.currentTimeMillis() / 1000;

            historicalPriceAndTime.moveToFirst();

            historicalPriceAndTime.moveToPosition(2);
            int[][] priceAndTimeArray = new int[inputNumber - 1][inputNumber - 1];

            historicalPriceAndTime.moveToNext();
            historicalPriceAndTime.moveToNext();

            boolean togglePriceTime = true;

            int count = 0;
            while (count < inputNumber) {
                if(count == 3) {
                    count += 1;
                    continue;
                }
                if (togglePriceTime) {
                    priceAndTimeArray[count][0] = historicalPriceAndTime.getInt(count + 2);
                    togglePriceTime = false;
                } else {
                    togglePriceTime = true;
                    priceAndTimeArray[count][1] = enteredTime;
                }
                historicalPriceAndTime.moveToNext();
                count += 1;

            }

            recommendedPrice = getRecommendedPrice(priceAndTimeArray);

            ContentValues priceContent = new ContentValues();

            priceContent.put("price", recommendedPrice);
            if(highPrice) {
                priceContent.put("highPrice", userEnteredPrice);
            }
            if(lowPrice) {
                priceContent.put("lowPrice", userEnteredPrice);
            }
            priceContent.put("inputnumber", inputNumber + 1);

            db.update("CityProduct", priceContent, "ProductKey="+value+" AND CityKey="+city, null);

            ContentValues productTime = new ContentValues();
            productTime.put("price"+inputNumber, userEnteredPrice);
            productTime.put("time"+inputNumber, enteredTime);

            db.execSQL("ALTER TABLE ProductTime ADD COLUMN" + "price"+inputNumber + "FLOAT");
            db.execSQL("ALTER TABLE ProductTime ADD COLUMN" + "time"+inputNumber + "INTEGER");

            db.update("ProductTime", productTime, "ProductKey="+value+" AND CityKey="+city, null);

            db.close();
            this.finish();


        }
    }
}
