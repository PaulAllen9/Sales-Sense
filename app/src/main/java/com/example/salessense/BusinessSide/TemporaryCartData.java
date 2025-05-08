package com.example.salessense.BusinessSide;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.salessense.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryCartData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PICTURE = "picture";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_QUANTITY_IN_CART = "quantity_in_cart";

    private Context context;
    public TemporaryCartData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CART + " ("
                + COLUMN_ID + " INTEGER, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PICTURE + " INTEGER, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_STOCK + " INTEGER, "
                + COLUMN_QUANTITY_IN_CART + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addProduct(Product product) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // Check if product is already in the cart
            Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_NAME},
                    COLUMN_NAME + " = ?", new String[]{String.valueOf(product.getName())},
                    null, null, null);

            if (cursor.moveToFirst()) {

                Toast.makeText(context, "Item is already in the cart. Please navigate to the cart to manage.", Toast.LENGTH_SHORT).show();
            } else {
                // Product does not exist, insert it
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, product.getId());
                values.put(COLUMN_NAME, product.getName());
                values.put(COLUMN_PICTURE, product.getPicture());
                values.put(COLUMN_DESCRIPTION, product.getDescription());
                values.put(COLUMN_PRICE, product.getPrice());
                values.put(COLUMN_STOCK, product.getBackStock());
                values.put(COLUMN_QUANTITY_IN_CART, product.getQuantityInCart());

                db.insert(TABLE_CART, null, values);
            }

            cursor.close();
            db.close();
        }
        catch (Exception e){
            Toast.makeText(context, "There was an error.", Toast.LENGTH_SHORT).show();

        }


    }
    public void updateProductQuantity(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_QUANTITY_IN_CART},
                COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())},
                null, null, null);

        if (cursor.moveToFirst()) {
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_IN_CART));

            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY_IN_CART, existingQuantity + product.getQuantityInCart());

            db.update(TABLE_CART, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        }

        cursor.close();
        db.close();
    }

    // Remove a Product from Cart
    public void removeProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_NAME + " = ?", new String[]{String.valueOf(product.getName())});
        db.close();
    }

    // Get All Products in Cart
    public ArrayList<Product> getCartContents() {
        ArrayList<Product> cart = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PICTURE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK))

                        );

                product.setQuantityInCart(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_IN_CART)));
                cart.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cart;
    }

    // Clear Cart (Reset Transaction)
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
        db.close();
    }
}