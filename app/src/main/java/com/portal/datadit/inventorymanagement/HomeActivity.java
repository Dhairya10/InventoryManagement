package com.portal.datadit.inventorymanagement;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.portal.datadit.inventorymanagement.data.InventoryContract;
import com.portal.datadit.inventorymanagement.data.InventoryContract.InventoryEntry;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    InventoryCursorAdapter adapter;
    public static final int LOADER_ID = 1;
    long id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FloatingActionButton addItem = findViewById(R.id.floatingActionButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView ItemlistView = findViewById(R.id.listViewHome);
        View emptyView = findViewById(R.id.empty_view);
        ItemlistView.setEmptyView(emptyView);
        adapter = new InventoryCursorAdapter(this, null);
        ItemlistView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        ItemlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryContract.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
        };
        return new CursorLoader(this,
                InventoryContract.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
