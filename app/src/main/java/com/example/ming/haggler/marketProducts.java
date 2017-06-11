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
import android.widget.Button;
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
    private Integer selected;
    private ArrayList<String> productList = new ArrayList<String>();
    private Button newProductButton;
    public static int i;
    private ArrayList<Integer> selectedProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_products);

        Intent intent = getIntent();
        value = intent.getStringExtra("city");

        newProductButton = (Button) findViewById(R.id.updateButton);
        productListView = (ListView) findViewById(R.id.marketListView);
        productTextView = (TextView) findViewById(R.id.marketTextView);
        productTextView.setText("Select Product");


        //connects tha applicaiton to the database
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        //adds information from database
        SQLiteDatabase db = myDB.openDatabase();
        Cursor cProductName = db.rawQuery("SELECT Title FROM Product", null);
        cProductName.moveToFirst();
        Cursor cProductPic = db.rawQuery("SELECT PicPath FROM Product", null);
        cProductPic.moveToFirst();
        Cursor cProductCityCheck = db.rawQuery("SELECT CityKey FROM CityProduct", null);
        cProductCityCheck.moveToFirst();

        // off sets the list so the index does not go out of bounds
        productCityCheck.add("0");
        //this is just to check the city the item is present in
        while (!cProductCityCheck.isAfterLast()) {
            productCityCheck.add(cProductCityCheck.getString(cProductCityCheck.getColumnIndex("CityKey")));
            cProductCityCheck.moveToNext();
        }

        i = 1;
        while(!cProductName.isAfterLast()){
            if (!productCityCheck.get(i).equals(value)) {
                cProductName.moveToNext();
                i = i + 1;
            } else {
                productList.add(cProductName.getString(cProductName.getColumnIndex("Title")));
                cProductName.moveToNext();
                selectedProducts.add(i - 1);
                i = i + 1;
                //adds eligible products to the list to eligible products can be selected later on
            }
        }
        marketProducts = productList.toArray(new String[0]);

        i = 1;
        //adds links to pictures to list but don't add if the data does not contain a link to a picture
        ArrayList<Integer> picList = new ArrayList<>();
        while(!cProductPic.isAfterLast()){;
            if ((cProductPic.toString() == "") && !productCityCheck.get(i).equals(value)) {
                String name = cProductPic.getString(cProductPic.getColumnIndex("PicPath"));
                picList.add(getResources().getIdentifier(name, "drawable", getPackageName()));
            } else {
                picList.add(0);
            }
            cProductPic.moveToNext();
            i = i + 1;
        }

        productImages = picList.toArray(new Integer[0]);

        productListView.setAdapter(new ItemsAdapter(this, marketProducts, productImages, price));
        db.close();
        //detects click, launches activity and adds data to activity
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int w, long l){
                selected = selectedProducts.get(w);
                selected += 1;
                Intent itemIntent = new Intent(marketProducts.this, ItemDescriptionActivity.class);
                String s = Integer.toString(selected);
                itemIntent.putExtra("product", s);
                itemIntent.putExtra("city", value.toString());
                startActivity(itemIntent);

            }
        });
    }

    public void newProduct(View view) {
        Intent newProductIntent = new Intent(marketProducts.this, NewMarketItemActivity.class);
        newProductIntent.putExtra("city", value.toString());
        startActivity(newProductIntent);
    }
}
