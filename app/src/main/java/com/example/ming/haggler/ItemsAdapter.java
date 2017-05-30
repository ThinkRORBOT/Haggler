package com.example.ming.haggler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ming on 10/05/2017.
 */

public class ItemsAdapter extends BaseAdapter {
    String [] result;
    Context context;
    Integer [] imageId;
    float [] price;
    private static LayoutInflater inflater=null;
    //Constructors to initialise data depending on the activity that called it
    public ItemsAdapter(essentialsProducts essentialProductsActivity, String[] products, Integer[] productImages, float[] price_arr) {

        result=products;
        context= essentialProductsActivity;
        price = price_arr;
        imageId=productImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public ItemsAdapter(marketProducts marketProductsActivity, String[] products, Integer[] productImages, float[] price_arr) {

        result=products;
        context= marketProductsActivity;
        imageId=productImages;
        price = price_arr;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView price;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //new holder object
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.products_list, null);
        //sets the attributes of holder to values in array
        holder.tv=(TextView) rowView.findViewById(R.id.productTitle);
        holder.img=(ImageView) rowView.findViewById(R.id.productImage);
        holder.price=(TextView) rowView.findViewById(R.id.productPrice);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        //for when the price is implemented
        //holder.price.setText(String.valueOf(price[position]));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}