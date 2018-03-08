package com.portal.datadit.inventorymanagement.data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
public class InventoryContract {
    private InventoryContract() {}
    public static final String CONTENT_AUTHORITY = "com.portal.datadit.inventorymanagement";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

    public static class InventoryEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "Inventory";
        public static final String COLUMN_PRODUCT_NAME = "Name";
        public static final String COLUMN_PRODUCT_QUANTITY = "Quantity";
        public static final String COLUMN_PRODUCT_PRICE = "Price";
        public static final String COLUMN_SUPPLIER_NAME = "SupplierName";
        public static final String COLUMN_SUPPLIER_NUMBER = "SupplierNumber";
        public static final String COLUMN_SUPPLIER_EMAIL = "SupplierEmail";
        public static  final String COLUMN_ITEM_IMAGE = "ItemImage";
    }
}
