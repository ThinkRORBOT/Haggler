package com.example.ming.haggler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    public static int productImages[];
    public static float price[];
    private int selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_products);

        Intent intent = getIntent();
        value = intent.getStringExtra("key");

        productListView = (ListView) findViewById(R.id.marketListView);
        productTextView = (TextView) findViewById(R.id.marketTextView);

        ArrayList<String> productList = new ArrayList<String>();
        productList.addAll(Arrays.asList(marketProducts));

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
