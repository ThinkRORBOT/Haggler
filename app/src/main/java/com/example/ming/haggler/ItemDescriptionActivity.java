package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDescriptionActivity extends AppCompatActivity {
    private String city;
    private String product;
    private String price;
    private String lowPrice;
    private String highPrice;
    private String productDescription;
    private String productTitle;
    private String productImage;
    private ImageView productImageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private Button updateButton;
    private boolean essentialActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        //gets values passed from previous activities and converts them to ind
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        product = intent.getStringExtra("product");

        //loads a new instance of the database for access
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        SQLiteDatabase db = myDB.openDatabase();
        //get all the data from the database
        Cursor cPrice = db.rawQuery("SELECT Price FROM CityProduct WHERE CityKey = " + city + " AND ProductKey = " + product, null);
        cPrice.moveToFirst();
        price = cPrice.getString(cPrice.getColumnIndex("Price"));

        Cursor cLowPrice = db.rawQuery("SELECT lowPrice FROM CityProduct WHERE CityKey = " + city + " AND ProductKey = " + product, null);
        cLowPrice.moveToFirst();
        lowPrice = cLowPrice.getString(cLowPrice.getColumnIndex("lowPrice"));

        Cursor cHighPrice = db.rawQuery("SELECT highPrice FROM CityProduct WHERE CityKey = " + city + " AND ProductKey = " + product, null);
        cHighPrice.moveToFirst();
        highPrice = cHighPrice.getString(cHighPrice.getColumnIndex("highPrice"));

        Cursor cDescription = db.rawQuery("SELECT Description FROM Product WHERE ProductKey = " + product, null);
        cDescription.moveToFirst();
        productDescription = cDescription.getString(cDescription.getColumnIndex("Description"));

        Cursor cTitle = db.rawQuery("SELECT Title FROM Product WHERE ProductKey = " + product, null);
        cTitle.moveToFirst();
        productTitle = cTitle.getString(cTitle.getColumnIndex("Title"));
        //initialises the gui and the the data

        Cursor cProductImage = db.rawQuery("SELECT PicPath FROM Product WHERE ProductKey = " + product, null);
        if (cProductImage.toString() != "") {
            cProductImage.moveToFirst();
            productImage = cProductImage.getString(cProductImage.getColumnIndex("PicPath"));

            productImageView = (ImageView) findViewById(R.id.productImage);
            productImageView.setImageResource(getResources().getIdentifier(productImage, "drawable", getPackageName()));
        }
        titleTextView = (TextView) findViewById(R.id.titleView);
        titleTextView.setText(productTitle);

        descriptionTextView = (TextView) findViewById(R.id.descriptionView);
        descriptionTextView.setText(productDescription);

        priceTextView = (TextView) findViewById(R.id.priceView);
        priceTextView.setText(" High price: " + highPrice + " Low Price: " + lowPrice + " Recommended Price: " + price);


        //hides the button as you can't update price on essential items
        essentialActivity = MainActivity.essential ? true : false;
        updateButton = (Button) findViewById(R.id.button);
        db.close();
        if (!essentialActivity) {

            updateButton.setText("Update");
            updateButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent updateIntent = new Intent(ItemDescriptionActivity.this, UpdateInformationActivity.class);
                    updateIntent.putExtra("product", product);
                    updateIntent.putExtra("city", city);
                    startActivityForResult(updateIntent, 1);

                    }
            });
        } else {
            updateButton.setVisibility(View.GONE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent pIntent = getIntent();
            this.finish();
            startActivity(pIntent);
        }
    }

}
