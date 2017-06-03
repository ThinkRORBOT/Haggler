package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDescriptionActivity extends AppCompatActivity {
    private String city;
    private String product;
    private int cityId;
    private int productId;
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
        cityId = Integer.parseInt(city);
        productId = Integer.parseInt(product);

        //loads a new instance of the database for access
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        SQLiteDatabase db = myDB.openDatabase();
        //get all the data from the database
        Cursor cPrice = db.rawQuery("SELECT price FROM cityproduct WHERE cityid = " + cityId + "AND WHERE productid = " + productId, null);
        cPrice.moveToFirst();
        price = cPrice.getString(cPrice.getColumnIndex("price"));

        Cursor cLowPrice = db.rawQuery("SELECT lowprice FROM cityproduct WHERE cityid = " + cityId + "AND WHERE productid = " + productId, null);
        cLowPrice.moveToFirst();
        lowPrice = cLowPrice.getString(cPrice.getColumnIndex("lowprice"));

        Cursor cHighPrice = db.rawQuery("SELECT highprice FROM cityproduct WHERE cityid = " + cityId + "AND WHERE productid = " + productId, null);
        cHighPrice.moveToFirst();
        highPrice = cHighPrice.getString(cHighPrice.getColumnIndex("highprice"));

        Cursor cDescription = db.rawQuery("SELECT description FROM products WHERE id = " + productId, null);
        cDescription.moveToFirst();
        productDescription = cDescription.getString(cDescription.getColumnIndex("description"));

        Cursor cTitle = db.rawQuery("SELECT title FROM products WHERE id = " + productId, null);
        cTitle.moveToFirst();
        productTitle = cTitle.getString(cTitle.getColumnIndex("title"));


        //initialises the gui and the the data
        Cursor cProductImage = db.rawQuery("SELECT productimage FROM products WHERE id = " + productId, null);
        cProductImage.moveToFirst();
        productImage = cProductImage.getString(cProductImage.getColumnIndex("productimage"));

        productImageView = (ImageView) findViewById(R.id.productImage);
        productImageView.setImageResource(getResources().getIdentifier(price, "drawable", getPackageName()));

        titleTextView = (TextView) findViewById(R.id.titleView);
        titleTextView.setText(productTitle);

        descriptionTextView = (TextView) findViewById(R.id.descriptionView);
        descriptionTextView.setText(productDescription);

        priceTextView = (TextView) findViewById(R.id.titleView);
        priceTextView.setText("High price: " + highPrice + "Low Price: " + lowPrice + "Recommended Price" + price);

        essentialActivity = MainActivity.essential ? true : false;

        if (!essentialActivity) {
            updateButton = (Button) findViewById(R.id.button);
            updateButton.setText("Update");
            updateButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent updateIntent = new Intent(ItemDescriptionActivity.this, updateInformation.class);
                    updateIntent.putExtra("product", product);
                    updateIntent.putExtra("city", city);
                }
            });
        }
    }
}
