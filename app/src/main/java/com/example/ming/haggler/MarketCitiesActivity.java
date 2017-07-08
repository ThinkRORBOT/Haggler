package com.example.ming.haggler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    // the market in question as a reference
    // 1 = kowloon market hong kong
    // 1 = MBK bangkok
    //2 = amari plaza discount department store bangkok

    //TODO Place these paths into database
    private int [] cityImages = {
            R.drawable.hongkong, R.drawable.shanghai, R.drawable.delhi, R.drawable.bangkok};

    private ListView cityListView;
    private ArrayAdapter<String> listAdapter;
    private TextView cityTextView;
    private int selected = 0;
    private ArrayList<String> citiesStore = new ArrayList<String>();
    private String[] cities;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creates a new instance to access the database
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);

        SQLiteDatabase db = myDB.openDatabase();

        //creates a cursor object to contain the data and move to an ArrayList
        Cursor c = db.rawQuery("SELECT cityname FROM cities", null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            //Log.d("Test", "done");
            citiesStore.add(c.getString(c.getColumnIndex("cityname")));
            c.moveToNext();
        }
        c.close();
        cities = citiesStore.toArray(new String[0]);
        db.close();
        //creates the interface
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

        //Todo: Get file from data base
        boolean imageExists = false;
        String filename = null;
        String localFile = null;
        if (imageExists) {
            if (downloadAndSaveFile(filename, localFile));

        }

        //gets the data into a list view like object
        cityListView.setAdapter(new CustomAdapter(this, cities, cityImages));
        //detect the item the user has clicked and start the next activity
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int w, long l){
                selected = w;
                Intent productIntent = new Intent(MarketCitiesActivity.this, marketProducts.class);
                w += 1;
                String s = Integer.toString(w);
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

    private Boolean downloadAndSaveFile(String server, int portNumber,
                                        String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            ftp.connect(server, portNumber);
            Log.d("Connected. Reply: ", ftp.getReplyString());

            ftp.login(user, password);
            Log.d("Logged in: ", "Loading");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            Log.d("Downloading: ", "test");
            ftp.enterLocalPassiveMode();

            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
                Bitmap bitmap = BitmapFactory.decodeFile(filename);
                imageView.setImageBitmap(bitmap);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }
}
