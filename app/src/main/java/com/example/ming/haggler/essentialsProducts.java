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
import java.util.List;

public class essentialsProducts extends AppCompatActivity {
    String value;
    //display price of product later on
    public static float price[];
    private ListView productListView;
    private ArrayAdapter<String> listAdapter;
    private TextView productTextView;
    private int selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essentials_products);

        //gets information from the activity that initiated this activity
        Intent intent = getIntent();
        value = intent.getExtras().getString("city");


        //initialises ui elements
        productListView = (ListView) findViewById(R.id.essentialsListView);
        productTextView = (TextView) findViewById(R.id.essentialsTextView);


        //connects tha applicaiton to the database
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        //adds information from database
        SQLiteDatabase db = myDB.openDatabase();
        Cursor cProductName = db.rawQuery("SELECT Title FROM Product", null);
        cProductName.moveToFirst();
        Cursor cProductPic = db.rawQuery("SELECT PicPath FROM Product", null);
        cProductPic.moveToFirst();
        //Log.d("cnt", "cnt: " + cProductName.getCount());
        ArrayList<String> productList = new ArrayList<String>();
        //converts information from database to an array
        while(!cProductName.isAfterLast()){
            productList.add(cProductName.getString(cProductName.getColumnIndex("Title")));
            cProductName.moveToNext();
        }
        cProductName.close();
        String[] essentialProduct = productList.toArray(new String[0]);
        //initialises new array to that the strings can be sorted
        //Arrays.sort(essentialProduct);
        ArrayList<Integer> picList = new ArrayList<>();

        while(!cProductPic.isAfterLast()){
            Log.d("MyApp", "Opened");
            String name = cProductPic.getString(cProductPic.getColumnIndex("PicPath"));
            picList.add(getResources().getIdentifier(name, "drawable", getPackageName()));
            cProductPic.moveToNext();

        }
        cProductPic.close();

        Integer[] productImages = picList.toArray(new Integer[0]);

        //productList.addAll(Arrays.asList(essentialProduct));

        productListView.setAdapter(new ItemsAdapter(this, essentialProduct, productImages, price));
        db.close();
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                selected = i;
                Intent itemIntent = new Intent(essentialsProducts.this, ItemDescriptionActivity.class);
                //Add one to i so the correct item from database is selected
                i += 1;
                String s = Integer.toString(i);
                itemIntent.putExtra("product", s);
                itemIntent.putExtra("city", value.toString());
                startActivity(itemIntent);


            }
        });
    }


}
