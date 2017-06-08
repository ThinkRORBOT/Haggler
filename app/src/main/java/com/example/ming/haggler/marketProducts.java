package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class marketProducts extends AppCompatActivity {
    private ListView productListView;
    private ArrayAdapter<String> listAdapter;
    private TextView productTextView;
    private String value;
    public static String marketProducts[];
    public static Integer productImages[];
    public static float price[];
    private ArrayList<String> productCityCheck = new ArrayList<>();
    // so that the list is off set
    private int selected;
    private ArrayList<String> productList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_products);

        Intent intent = getIntent();
        value = intent.getStringExtra("city");

        Log.d("marketProducts", "opened");

        productListView = (ListView) findViewById(R.id.marketListView);
        productTextView = (TextView) findViewById(R.id.marketTextView);
        productTextView.setText("Select Product");


        //connects tha applicaiton to the database
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        //adds information from database
        SQLiteDatabase db = myDB.openDatabase();
        Cursor cProductName = db.rawQuery("SELECT title FROM products", null);
        cProductName.moveToFirst();
        Cursor cProductPic = db.rawQuery("SELECT productImage FROM products", null);
        cProductPic.moveToFirst();
        Cursor cProductCityCheck = db.rawQuery("SELECT cityid FROM cityproduct", null);
        cProductCityCheck.moveToFirst();

        // off sets the list so the index does not go out of bounds
        productCityCheck.add("0");
        //this is just to check the city the item is present in
        while (!cProductCityCheck.isAfterLast()) {
            productCityCheck.add(cProductCityCheck.getString(cProductCityCheck.getColumnIndex("cityid")));
            cProductCityCheck.moveToNext();
        }

        int i = 1;
        while(!cProductName.isAfterLast()){
            Log.d("app", (productCityCheck.get(i) == value)+"");
            Log.d(productCityCheck.get(i), value);
            if (!productCityCheck.get(i).equals(value)) {
                cProductName.moveToNext();
                i = i + 1;
            } else {
                productList.add(cProductName.getString(cProductName.getColumnIndex("title")));
                cProductName.moveToNext();
                i = i + 1;
            }
        }
        marketProducts = productList.toArray(new String[0]);

        i = 1;
        ArrayList<Integer> picList = new ArrayList<>();
        while(!cProductPic.isAfterLast()){;
            if (cProductPic != null || !productCityCheck.get(i).equals(value)) {
                String name = cProductPic.getString(cProductPic.getColumnIndex("productImage"));
                picList.add(getResources().getIdentifier(name, "drawable", getPackageName()));
            } else {
                picList.add(0);
            }
            cProductPic.moveToNext();
            i = i + 1;
        }

        productImages = picList.toArray(new Integer[0]);

        productListView.setAdapter(new ItemsAdapter(this, marketProducts, productImages, price));

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                selected = i;
                Intent itemIntent = new Intent(marketProducts.this, ItemDescriptionActivity.class);
                String s = Integer.toString(i);
                itemIntent.putExtra("product", s);
                itemIntent.putExtra("city", value.toString());
                startActivity(itemIntent);

            }
        });
    }
}
