package com.example.ming.haggler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jibble.simpleftp.*;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.R.id.content;

public class NewMarketItemActivity extends AppCompatActivity {
    private TextView titleText;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView marketTextView;
    private EditText marketEditText;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private TextInputLayout tilTitle;
    private TextInputLayout tilDescription;
    private TextInputLayout tilPrice;
    private Button loadImage;
    private Button updateButton;
    private ImageView targetImage;
    private TextInputLayout tilMarket;
    private String value;
    private SQLiteDatabase db;
    private int privRep;
    public Uri targetUri;
    public String imagePath;
    private String filePath;
    public Bitmap bitmap;
    private Boolean imageSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_market_item);

        //sets the user interface up
        titleText = (TextView) findViewById(R.id.newItemTitle);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        updateButton = (Button) findViewById(R.id.updateButton);
        marketTextView = (TextView) findViewById(R.id.marketTextView);
        marketEditText = (EditText) findViewById(R.id.marketEditText);
        tilDescription = (TextInputLayout) findViewById(R.id.tileDescription);
        tilTitle = (TextInputLayout) findViewById(R.id.tilTitle);
        tilPrice = (TextInputLayout) findViewById(R.id.tilePrice);
        tilMarket = (TextInputLayout) findViewById(R.id.tilMarket);
        targetImage = (ImageView) findViewById(R.id.targetImage);
        //loadImage = (Button) findViewById(R.id.loadimage);

        privRep = LoginActivity.reputation;
        //get city from previous activity
        Intent intent = getIntent();
        value = intent.getStringExtra("city");

        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        db = myDB.openDatabase();
    }

    private boolean checkValidity() {
        //checks if all the user input is valid, if not valid, show error

        if (privRep < 3) {
            Toast.makeText(getApplicationContext(), "Sorry, you can only make a new product if you make more price contributions", Toast.LENGTH_LONG).show();
            return false;
        }

        if (nameEditText.getText().toString().matches("")) {
            tilTitle.setError("Need something in this field");
            return false;
        } else if (descriptionEditText.getText().toString().matches("")) {
            tilDescription.setError("Need something in this field");
            return false;
        } else if (priceEditText.getText().toString().matches("")) {
            tilPrice.setError("Need something in this field");
            return false;
        } else if (marketEditText.getText().toString().matches("")) {
            tilMarket.setError("Need something in this field");
            return false;
        }

        if (nameEditText.getText().toString().length() > 255) {
            tilTitle.setError("Character limit is 255");
            return false;
        } else if (descriptionEditText.getText().toString().length() > 2000) {
            tilDescription.setError("Character limit is 2000");
            return false;
        } else if (marketEditText.getText().toString().length() > 100) {
            tilMarket.setError("Character limit is 100");
            return false;
        }

        if(priceEditText.getText().toString().matches("0")) {
            tilPrice.setError("Price cannot be zero");
            return false;
        }

        return true;
    }

    public void createItem(View view) throws IOException {
        if (checkValidity()) {

            //if image was selected check if image can be uploaded
            if (imageSelected) {
                if (!uploadImage()){
                    AlertDialog alertDialog = new AlertDialog.Builder(NewMarketItemActivity.this).create();
                    alertDialog.setTitle("Cannot connect to ftp");
                    alertDialog.setMessage("Do you want to upload information without images?");
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }

            }

            //if it is valid assign variables to values from textbox
            Log.d("item", "in");
            String title = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            Float price = Float.valueOf(priceEditText.getText().toString());
            String market = marketEditText.getText().toString();
            int productid = marketProducts.i;
            ContentValues content = new ContentValues();
            content.put("Title", title);
            content.put("Description", description);
            content.put("ProductKey", productid);
            content.put("PicPath", "");
            //place values into database
            long newRowId = db.insert("Product", null, content);

            ContentValues cityProduct = new ContentValues();
            cityProduct.put("id", productid);
            cityProduct.put("CityKey", value);
            cityProduct.put("ProductKey", productid);
            cityProduct.put("inputnumber", 1);
            cityProduct.put("lowPrice", price);
            cityProduct.put("highPrice", price);
            cityProduct.put("Price", price);
            cityProduct.put("market", market);

            long newRowId1 = db.insert("CityProduct", null, cityProduct);
            db.close();
            setResult(RESULT_OK);


            privRep += 1;
            if (!LoginActivity.username.matches("")) {
                ChangeUserSetting uSet = new ChangeUserSetting();
                uSet.changeReputation(privRep);
            }

            this.finish();
        }
    }

    //uploads image to http server
    private boolean uploadImage() throws IOException {
        //ToDo: Store file location in database
        SimpleFTP ftp = new SimpleFTP();
        // Connect to an FTP server on port 21.
        try {
            ftp.connect("server address", 21, "username", "pwd");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Set binary mode.
        ftp.bin();

        // Change to a new working directory on the FTP server.


        ftp.cwd("path");

        // Upload some files.
        ftp.stor(new File(imagePath));

        // Quit from the FTP server.
        ftp.disconnect();

        return true;
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, 0);
    }

    //gets the image the user has chosen and displays it in a image view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            targetUri = data.getData();

            filePath = getPath(targetUri);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
                imageSelected = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //get the path of the image
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

}
