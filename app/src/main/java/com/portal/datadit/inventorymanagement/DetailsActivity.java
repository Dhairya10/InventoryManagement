package com.portal.datadit.inventorymanagement;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.portal.datadit.inventorymanagement.data.InventoryContract.InventoryEntry;
import com.portal.datadit.inventorymanagement.data.InventoryDBHelper;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private Uri mCurrentItemUri;
    public static final int ITEM_LOADER = 1;
    private TextView productName;
    private TextView productPrice;
    private TextView productQuantity;
    private TextView supplierName;
    private TextView supplierMail;
    private TextView supplierNumber;
    private EditText itemQuantity;
    private ImageButton itemAdd;
    private ImageButton itemDelete;
    private Button itemOrder;
    private Button itemRemove;
    private ImageView itemImage;
    public static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private InventoryDBHelper inventoryDBHelper;
    private SQLiteDatabase database;
    private TextView imageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        supplierName = findViewById(R.id.textViewName);
        supplierMail = findViewById(R.id.textViewMail);
        supplierNumber = findViewById(R.id.textViewNumber);
        itemAdd = findViewById(R.id.imageButtonAdd);
        itemDelete = findViewById(R.id.imageButtonDelete);
        itemOrder = findViewById(R.id.buttonOrder);
        itemRemove = findViewById(R.id.buttonDelete);
        itemImage = findViewById(R.id.imageView);
        productName = findViewById(R.id.textviewProductName);
        productPrice = findViewById(R.id.textViewProductPrice);
        productQuantity = findViewById(R.id.textViewProductQuantity);
        imageDisplay = findViewById(R.id.textViewClick);
        imageDisplay.setOnClickListener(this);
        itemAdd.setOnClickListener(this);
        itemDelete.setOnClickListener(this);
        itemRemove.setOnClickListener(this);
        itemOrder.setOnClickListener(this);
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
        inventoryDBHelper = new InventoryDBHelper(this);
        database = inventoryDBHelper.getReadableDatabase();
    }
    public void decrease() {
           Cursor cursor = getContentResolver().query(mCurrentItemUri, null, null, null);
        if (cursor.moveToFirst()) {
            final String pos = String.valueOf(cursor.getPosition()+1);
            int index = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int databaseQuantity = Integer.parseInt(cursor.getString(index));
            if (databaseQuantity == 0) {
                Toast.makeText(this, "Item is Sold Out", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error");
            } else {
                databaseQuantity--;
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(databaseQuantity));
                int rowsAffected = getContentResolver().update(mCurrentItemUri, values, InventoryEntry._ID + "=?", new String[]{pos});
                if (rowsAffected == 0) {
                    Log.e(LOG_TAG, "Updation Unsuccessful");
                } else {
                    Log.e(LOG_TAG, "Updation Successful");
                    Toast.makeText(this, "Quantity decreased successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
        cursor.close();
    }


    public void increase()
    {
        Cursor cursor = getContentResolver().query(mCurrentItemUri, null, null, null);
        if (cursor.moveToFirst()) {
            final String pos = String.valueOf(cursor.getPosition()+1);
            int index = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int databaseQuantity = Integer.parseInt(cursor.getString(index));
            if (databaseQuantity == 0) {
                Toast.makeText(this, "Item is Sold Out", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error");
            } else {
                databaseQuantity++;
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(databaseQuantity));
                int rowsAffected = getContentResolver().update(mCurrentItemUri, values, InventoryEntry._ID + "=?", new String[]{pos});
                if (rowsAffected == 0) {
                    Log.e(LOG_TAG, "Updation Unsuccessful");
                } else {
                    Log.e(LOG_TAG, "Updation Successful");
                    Toast.makeText(this, "Quantity increased successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
        cursor.close();
    }

    public void display()
    {
        Cursor cursor = getContentResolver().query(mCurrentItemUri, null, null, null);
        if (cursor.moveToFirst())
        {
            final String pos = String.valueOf(cursor.getPosition()+1);
            int index = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_ITEM_IMAGE);
            byte[] imageArray = cursor.getBlob(index);
            if (imageArray == null)
            {
                Log.e(LOG_TAG,"Blob is null");
            }
            else {
                Bitmap imageBitmap = ImageUtils.getImage(imageArray);
                itemImage.setImageBitmap(imageBitmap);
            }
        }
        cursor.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_EMAIL,
                InventoryEntry.COLUMN_SUPPLIER_NUMBER,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_ITEM_IMAGE
        };
        return new CursorLoader(this, mCurrentItemUri, projection,
                null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            Log.e(LOG_TAG,"data is null");
            return;
        }
        if (data.moveToFirst()) {
           do {
                String name = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_SUPPLIER_NAME));
                String email = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_SUPPLIER_EMAIL));
                String number = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_SUPPLIER_NUMBER));
                String prodName = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_NAME));
                String prodPrice = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_PRICE));
                String prodQuantity = data.getString(data.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_QUANTITY));
                productName.setText("Product Name: "+prodName);
                productPrice.setText("Product Price: "+prodPrice);
                productQuantity.setText("Product Quantity: "+prodQuantity);
                supplierName.setText("Supplier Name: "+name);
                supplierNumber.setText("Supplier Number: "+number);
                supplierMail.setText("Supplier Mail: "+email);
            } while (data.moveToNext());
        }
        data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        supplierName.setText("");
        supplierNumber.setText("");
        supplierMail.setText("");
        productName.setText("");
        productPrice.setText("");
        productQuantity.setText("");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Item");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
        if (rowsDeleted == 0) {
            Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion Successful", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void Contact() {
        String[] projection = {InventoryEntry.COLUMN_SUPPLIER_NUMBER};
        Cursor cursor = getContentResolver().query(mCurrentItemUri, projection, null,
                null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_SUPPLIER_NUMBER);
            String number = cursor.getString(index);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else {
                Log.e(LOG_TAG,"No app available to handle the intent");
            }
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v == itemAdd) {
            increase();
        } else if (v == itemDelete) {
            decrease();
        } else if (v == itemOrder) {
            Contact();
        } else if (v == itemRemove) {
            showDeleteConfirmationDialog();
        }
        else if (v == imageDisplay)
        {
            display();
        }
    }
}
