package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ming on 7/05/2017.
 * Allows the user to choose the city
 */

public class MarketCitiesActivity extends AppCompatActivity {
    //public static String[] cities = new String []{
    //        "Hong Kong", "Shanghai", "Delhi", "Bangkok"
   // };

    // 1 = kowloon market hong kong
    // 1 = MBK bangkok
    //2 = amari plaza discount department store bangkok


    public static int [] cityImages = {
            R.drawable.hongkong, R.drawable.shanghai, R.drawable.delhi, R.drawable.bangkok};

    private ListView cityListView;
    private ArrayAdapter<String> listAdapter;
    private TextView cityTextView;
    private int selected = 0;
    public static  ArrayList<String> citiesTest = new ArrayList<String>();
    private String[] cities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creates The interface
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        SQLiteDatabase db = myDB.openDatabase();

        //creates a cursor object to contain the data and move to an ArrayList
        Cursor c = db.rawQuery("SELECT CityName FROM City", null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            citiesTest.add(c.getString(c.getColumnIndex("CityName")));
            c.moveToNext();
        }
        c.close();
        cities = citiesTest.toArray(new String[0]);
        setContentView(R.layout.content_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cityTextView = (TextView) findViewById(R.id.city_textView);
        cityTextView.setText("Select City");
        //Populates the list view item

        cityListView = (ListView) findViewById(R.id.cityListView);

        //adapts array to list view can be used

        ArrayList<String> citiesList = new ArrayList<String>();
        citiesList.addAll(Arrays.asList(cities));

        //listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, citiesList);

        cityListView.setAdapter(new CustomAdapter(this, cities, cityImages));

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                selected = i;
                Intent productIntent = new Intent(MarketCitiesActivity.this, marketProducts.class);
                String s = Integer.toString(i);
                productIntent.putExtra("city", s);
                startActivity(productIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
