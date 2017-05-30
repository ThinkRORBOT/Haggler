package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class EssentialCitiesActivity extends AppCompatActivity {

    //initialises the data used
    //public static String[] cities = new String []{
    //        "Hong Kong", "Shanghai", "Mumbai", "Delhi", "London", "Paris", "Rome", "Bangkok", "Sydney", "New York"
    //};
    public static  ArrayList<String> citiesTest = new ArrayList<String>();

    public static int [] cityImages = {
            R.drawable.hongkong, R.drawable.shanghai, R.drawable.mumbai, R.drawable.delhi, R.drawable.london, R.drawable.paris, R.drawable.rome, R.drawable.bangkok, R.drawable.sydney,
            R.drawable.newyork};

    private String[] cities;

    private ListView cityListView;
    private ArrayAdapter<String> listAdapter;
    private TextView cityTextView;
    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connects to the database
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
        Log.d("MyApp", "cnt:"+c.getCount());
        db.close();
        //Creates The interface by initialzing it
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

        //creates an adapter to allow a list item to be clicked

        cityListView.setAdapter(new CustomAdapter(this, cities, cityImages));

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                selected = i;

                Intent productIntent = new Intent(EssentialCitiesActivity.this, essentialsProducts.class);
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
