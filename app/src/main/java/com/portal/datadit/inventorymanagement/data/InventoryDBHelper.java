package com.portal.datadit.inventorymanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.portal.datadit.inventorymanagement.data.InventoryContract.InventoryEntry;

public class InventoryDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "items.db";

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT , "
                + InventoryEntry.COLUMN_PRODUCT_PRICE + " TEXT , "
                + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER , "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT , "
                + InventoryEntry.COLUMN_SUPPLIER_EMAIL + " TEXT, "
                + InventoryEntry.COLUMN_ITEM_IMAGE + " BLOB, "
                + InventoryEntry.COLUMN_SUPPLIER_NUMBER + " LONG );";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME);
        onCreate(db);
    }
}
