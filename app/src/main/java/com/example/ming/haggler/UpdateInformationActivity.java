package com.example.ming.haggler;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import static android.database.DatabaseUtils.dumpCursorToString;

public class UpdateInformationActivity extends AppCompatActivity {

    private  TextView titleTextView;
    private TextView priceTextView;
    private EditText priceText;
    private Button updateButton;
    private String value;
    private String city;
    private SQLiteDatabase db;
    private Float userEnteredPrice;
    private Float enteredTime;
    private Double recommendedPrice;
    private int numInput;
    //converts time from seconds to days
    private static final int TIMEPASSED = 68400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        //sets up the gui
        titleTextView = (TextView) findViewById(R.id.titleText);
        priceTextView = (TextView) findViewById(R.id.priceView);
        priceText = (EditText) findViewById(R.id.priceText);
        updateButton = (Button) findViewById(R.id.updateButton);

        //loads a new instance of the database for access
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        db = myDB.openDatabase();

        //get values from previous activity
        Intent intent = getIntent();
        value = intent.getStringExtra("product");
        city = intent.getStringExtra("city");


    }

    //checks if the price user enters is valid.
    private boolean checkPriceValidity() {
        if (priceText.getText().toString().matches("0")) {
            Toast.makeText(this, "Price cannot be zero", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //algorithm to get the recommended price from the user
    private double getRecommendedPrice(double pandtArray[][], double uEnteredP) {
        double finalPrice = 0;
        long totalweighting = 0;
        double weightedPrice = 0;
        //make the prices have differnt wieghting depending on how recent the user entered the price
        for(int i = 0; i < pandtArray[0].length - 1; i++) {
            weightedPrice += pandtArray[i][0] * (pandtArray[i][1]/TIMEPASSED);
            totalweighting += pandtArray[i][1]/TIMEPASSED;
        }
        weightedPrice += uEnteredP * (enteredTime/TIMEPASSED);
        totalweighting += enteredTime/TIMEPASSED;

        //makes sure the final price is in the right format
        DecimalFormat df = new DecimalFormat("#.##");
        finalPrice= weightedPrice/totalweighting;
        finalPrice = Float.valueOf(df.format(finalPrice));
        return finalPrice;
    }

    private boolean getAndUpdateData () {
        //gets the data from the text input
        userEnteredPrice = Float.valueOf(priceText.getText().toString());
        boolean highPrice = false;
        boolean lowPrice = false;

        //gets the relevant values from the database
        Cursor highPriceCursor = db.rawQuery("SELECT highPrice from CityProduct WHERE ProductKey = " + value + " AND CityKey = "+ city, null );
        Cursor lowPriceCursor = db.rawQuery("SELECT lowPrice from CityProduct WHERE ProductKey = " + value + " AND CityKey = "+ city, null );
        Cursor inputNum = db.rawQuery("SELECT inputnumber from CityProduct WHERE ProductKey = " + value + " AND CityKey = " + city, null );
        highPriceCursor.moveToFirst();
        lowPriceCursor.moveToFirst();
        inputNum.moveToFirst();
        //gets the relevant values from the cursor
        Float cursorHighPriceV = Float.valueOf(highPriceCursor.getString(highPriceCursor.getColumnIndex("highPrice")));
        Float cursorLowPriceV = Float.valueOf(lowPriceCursor.getString(lowPriceCursor.getColumnIndex("lowPrice")));
        int inputNumber = Integer.parseInt(inputNum.getString(inputNum.getColumnIndex("inputnumber")));

        //if the user has entered a new high price or low price, set a flag so that the information is changed
        if (cursorHighPriceV < userEnteredPrice) {
            highPrice = true;
        } else if (cursorLowPriceV > userEnteredPrice) {
            lowPrice = true;
        }


        Cursor historicalPriceAndTime = db.rawQuery("SELECT * FROM ProductTime WHERE ProductKey = " + value + " AND CityKey = " + city, null);

        //gets the current time when the user has entered the price
        enteredTime = (float) Math.round(System.currentTimeMillis() / 1000000);
        historicalPriceAndTime.moveToFirst();
        Log.d("current time", System.currentTimeMillis()+"");
        double[][] priceAndTimeArray = new double[inputNumber][2];

        boolean togglePriceTime = true;

        int count = 0;
                //gets the values from cursor object so the time and price of historical information is passed to 2d array
        while (count < inputNumber) {
            String temp = Integer.toString(count + 1);
            //toggles between storing price and time
            if (togglePriceTime) {
                double tempDouble = historicalPriceAndTime.getDouble(historicalPriceAndTime.getColumnIndex("Price"+temp));
                priceAndTimeArray[count][0] = tempDouble;
                togglePriceTime = false;
            } else {
                togglePriceTime = true;
                priceAndTimeArray[count][1] = historicalPriceAndTime.getDouble(historicalPriceAndTime.getColumnIndex("Time"+temp));
                count += 1;
            }

        }

        recommendedPrice = getRecommendedPrice(priceAndTimeArray, userEnteredPrice);

        //put information is a datastructure so that the database can be updated
        ContentValues priceContent = new ContentValues();

        priceContent.put("price", recommendedPrice);

        //if the user has entered a new high or low price, change the high and low price in database
        if(highPrice) {
            priceContent.put("highPrice", userEnteredPrice);
        }
        if(lowPrice) {
            priceContent.put("lowPrice", userEnteredPrice);
        }

        int newInputNumber = inputNumber + 1;

        priceContent.put("inputnumber", newInputNumber);

        db.update("CityProduct", priceContent, "ProductKey="+value+" AND CityKey="+city, null);

        ContentValues productTime = new ContentValues();
        productTime.put("Price"+newInputNumber, userEnteredPrice);
        productTime.put("Time"+newInputNumber, enteredTime);

        int valueDB = historicalPriceAndTime.getColumnIndex("Price"+newInputNumber);

        //if the columns already exist, don't create new ones
        if (valueDB == -1) {
            db.execSQL("ALTER TABLE ProductTime ADD COLUMN " + "Price"+newInputNumber);
            db.execSQL("ALTER TABLE ProductTime ADD COLUMN " + "Time"+newInputNumber);
        }

        db.update("ProductTime", productTime, "ProductKey="+value+" AND CityKey="+city, null);

        db.close();

        return true;
    }

    //this function runs when the update price is clicked.
    public void updatePrice(View view) {

        if (checkPriceValidity()) {
            if (getAndUpdateData() == true) {
                setResult(RESULT_OK);
                this.finish();
            }

        }
    }
}
