package com.portal.datadit.inventorymanagement;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.portal.datadit.inventorymanagement.data.InventoryContract;
import com.portal.datadit.inventorymanagement.data.InventoryContract.InventoryEntry;
import com.portal.datadit.inventorymanagement.data.InventoryDBHelper;

public class InventoryCursorAdapter extends CursorAdapter {
    public static final String LOG_TAG = InventoryCursorAdapter.class.getSimpleName();
    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }
    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_NAME));
        viewHolder.pName.setText("Product Name:  "+ name);
        final String quantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_QUANTITY));
        viewHolder.pQuantity.setText("Quantity:  "+quantity);
        final String price = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_PRICE));
        viewHolder.pPrice.setText("Price:  "+price);
        final String pos = String.valueOf(cursor.getPosition()+1);
        viewHolder.sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity);
                if (qty == 0) {
                    Toast.makeText(context, "Item is Sold Out", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Error");
                } else {
                    qty--;
                    Uri uri = ContentUris.withAppendedId(InventoryContract.CONTENT_URI, Long.parseLong(pos));
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(qty));
                    int rowsAffected = context.getContentResolver().update(uri, values, InventoryEntry._ID + "=?", new String[]{pos});
                    if (rowsAffected == 0) {
                        Log.e(LOG_TAG, "Updation Unsuccessful");
                    } else
                        Log.e(LOG_TAG, "Updation Successful");
                }
            }
        });
    }
}
