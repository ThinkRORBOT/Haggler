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

public class essentialsProducts extends AppCompatActivity {
    String value;
    public static String essentialProduct[] = new String[]{};
    public static int productImages[];
    public static float price[];
    private ListView productListView;
    private ArrayAdapter<String> listAdapter;
    private TextView productTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essentials_products);

        Intent intent = getIntent();
        value = intent.getStringExtra("key");
        productListView = (ListView) findViewById(R.id.essentialsListView);
        productTextView = (TextView) findViewById(R.id.essentialsTextView);

        ArrayList<String> productList = new ArrayList<String>();
        productList.addAll(Arrays.asList(essentialProduct));

        productListView.setAdapter(new ItemsAdapter(this, essentialProduct, productImages, price));

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

            }
        });
    }


}
