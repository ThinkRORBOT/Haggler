package com.example.ming.haggler;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.id.content;

public class NewMarketItemActivity extends AppCompatActivity {
    private TextView titleText;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private TextInputLayout tilTitle;
    private TextInputLayout tilDescription;
    private TextInputLayout tilPrice;
    private Button updateButton;
    private String value;
    private SQLiteDatabase db;
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
        tilDescription = (TextInputLayout) findViewById(R.id.tileDescription);
        tilTitle = (TextInputLayout) findViewById(R.id.tilTitle);
        tilPrice = (TextInputLayout) findViewById(R.id.tilePrice);

        Intent intent = getIntent();
        value = intent.getStringExtra("city");

        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        db = myDB.openDatabase();
    }

    private boolean checkValidity() {
        if (nameEditText.getText().toString().matches("")) {
            tilTitle.setError("Need something in this field");
            return false;
        } else if (descriptionEditText.getText().toString().matches("")) {
            tilDescription.setError("Need something in this field");
            return false;
        } else if (priceEditText.getText().toString().matches("")) {
            tilPrice.setError("Need something in this field");
            return false;
        }

        if (nameEditText.getText().toString().length() > 255) {
            tilTitle.setError("Character limit is 255");
        } else if (descriptionEditText.getText().toString().length() > 2000) {
            tilDescription.setError("Character limit is 2000");
        }

        return true;
    }

    public void createItem(View view) {
        if (checkValidity()) {
            Log.d("item", "in");
            String title = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            Float price = Float.valueOf(priceEditText.getText().toString());
            int productid = marketProducts.i;
            productid += 1;
            ContentValues content = new ContentValues();
            content.put("Title", title);
            content.put("Description", description);
            content.put("productid", productid);

            long newRowId = db.insert("Product", null, content);

            ContentValues cityProduct = new ContentValues();
            cityProduct.put("id", productid);
            cityProduct.put("CityKey", value);
            cityProduct.put("ProductKey", productid);
            cityProduct.put("inputnumber", 1);
            cityProduct.put("lowPrice", price);
            cityProduct.put("highPrice", price);
            cityProduct.put("Price", price);

            long newRowId1 = db.insert("CityProduct", null, cityProduct);

            this.finish();
        }
    }
}
