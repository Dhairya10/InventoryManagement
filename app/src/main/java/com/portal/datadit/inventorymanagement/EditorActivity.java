package com.portal.datadit.inventorymanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.portal.datadit.inventorymanagement.data.InventoryContract;
import com.portal.datadit.inventorymanagement.data.InventoryContract.InventoryEntry;
import com.portal.datadit.inventorymanagement.data.InventoryDBHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText pName;
    private EditText pQuantity;
    private EditText pPrice;
    private EditText sName;
    private EditText sNumber;
    private EditText sMail;
    private Button save;
    private Button addImage;
    private static final int SELECT_PICTURE = 100;
    InventoryDBHelper dbHelper;
    private SQLiteDatabase database;
    byte[] inputData;
    private boolean imageSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        pName = findViewById(R.id.editTextProductName);
        pQuantity = findViewById(R.id.editTextProductQuantity);
        pPrice = findViewById(R.id.editTextProductPrice);
        sName = findViewById(R.id.editTextSupplierName);
        sNumber = findViewById(R.id.editTextSupplierNumber);
        sMail = findViewById(R.id.editTextSupplierMail);
        save = findViewById(R.id.buttonSave);
        addImage = findViewById(R.id.buttonAddImage);
        addImage.setOnClickListener(this);
        save.setOnClickListener(this);
        dbHelper = new InventoryDBHelper(this);
        database = dbHelper.getWritableDatabase();
    }
    private void saveItem() {
        String productName = pName.getText().toString().trim();
        String productPrice = pPrice.getText().toString().trim();
        String productQuantity = pQuantity.getText().toString().trim();
        String supplierName = sName.getText().toString().trim();
        String supplierMail = sMail.getText().toString().trim();
        String supplierNumber = sNumber.getText().toString().trim();
        if (TextUtils.isEmpty(productName) && TextUtils.isEmpty(productQuantity) && TextUtils.isEmpty(productPrice) &&
                TextUtils.isEmpty(supplierName) && TextUtils.isEmpty(supplierNumber) && TextUtils.isEmpty(supplierMail)) {
            Toast.makeText(this, "Enter the required information", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productName))
        {Toast.makeText(this, "Please Enter a Product Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productQuantity))
        {Toast.makeText(this, "Please Enter the quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(productPrice))
        {Toast.makeText(this, "Please Enter the price", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(supplierName))
        {Toast.makeText(this, "Please enter the name of the supplier", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierNumber))
        {Toast.makeText(this, "Please enter the supplier number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(supplierMail))
        {Toast.makeText(this, "Please enter the supplier mail", Toast.LENGTH_SHORT).show();
        return;
        }
        if (supplierNumber.length()!=10)
        {Toast.makeText(this, "Please enter a valid contact number(10 digits)", Toast.LENGTH_SHORT).show();
        return;
        }
        if (!imageSaved)
        {Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL, supplierMail);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NUMBER, supplierNumber);
        values.put(InventoryEntry.COLUMN_ITEM_IMAGE,inputData);
        Uri uri = getContentResolver().insert(InventoryContract.CONTENT_URI, values);
        if (uri == null) {
            Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Values Added Successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    private void saveImage()
    {
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {

           Uri imageUri = data.getData();
          if (imageUri!=null)
          {

               try {
                   InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    inputData = ImageUtils.getBytes(inputStream);
                   Toast.makeText(this, "Image Added Successfully", Toast.LENGTH_SHORT).show();
                   imageSaved = true;
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
                   Log.e("Editor Activity","File not found");
               } catch (IOException e) {
                   e.printStackTrace();
                   Log.e("Editor Activity","IO Exception");
               }
           }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            saveItem();
        }
        if (v == addImage)
        {
            saveImage();
        }
    }
}




