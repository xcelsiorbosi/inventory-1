package com.example.android.inventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory.data.InventoryContract.InventoryEntry;

import static com.example.android.inventory.R.id.price;
import static com.example.android.inventory.R.id.quantity;

public class InventoryCursorAdapter extends CursorAdapter {
    ContentResolver mContentResolver;

    public InventoryCursorAdapter(Context context, Cursor c, ContentResolver contentResolver) {
        super(context, c, 0);
        mContentResolver = contentResolver;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView quantityTextView = (TextView) view.findViewById(quantity);
        TextView priceTextView = (TextView) view.findViewById(price);

        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRODUCT);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);

        int rowId = cursor.getInt(idColumnIndex);
        String itemName = cursor.getString(nameColumnIndex);
        final int itemQuantity = cursor.getInt(quantityColumnIndex);
        double itemPrice = cursor.getDouble(priceColumnIndex);

        nameTextView.setText(itemName);
        nameTextView.setTag(rowId);
        quantityTextView.setText(Integer.toString(itemQuantity));
        priceTextView.setText("$" + Double.toString(itemPrice));

        Button makeSale = (Button) view.findViewById(R.id.list_item_sell_button);
        makeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();

                int sale = Integer.valueOf(quantityTextView.getText().toString());

                if (sale == 0) {
                    return;
                } else {
                    sale = --sale;
                }

                values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, sale);

                int rowid = (Integer) nameTextView.getTag();

                Uri currentItemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, rowid);

                int rowsAffected = mContentResolver.update(currentItemUri, values, null, null);
            }
        });

    }

}