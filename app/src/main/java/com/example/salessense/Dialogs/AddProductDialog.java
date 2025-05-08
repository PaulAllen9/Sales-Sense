package com.example.salessense.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.salessense.BusinessSide.store_managment.CustomAdapter;
import com.example.salessense.Product;
import com.example.salessense.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProductDialog {
    /*
     * Author: Paul Allen
     * Last modified: 5/6/25 by Brian Yang
     * - Supports decimal prices
     * - Writes to Firestore
     * - Default image: minus_sign
     */

    private Dialog mDialog;
    private ImageButton exit;
    private Button addProduct, addPictureBTN;
    private EditText name, description, price, quantity;
    private ImageView picture;

    private CustomAdapter adapter;
    private Context context;
    public AddProductDialog(Context context, CustomAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        mDialog = initializeDialog(context);

        exit = mDialog.findViewById(R.id.closePopupButton);
        addProduct = mDialog.findViewById(R.id.finalizeBTN);
        name = mDialog.findViewById(R.id.pNameET);
        description = mDialog.findViewById(R.id.pDescriptionET);
        price = mDialog.findViewById(R.id.pPriceET);
        quantity = mDialog.findViewById(R.id.pQuantityET);  // ✅ required for stock
        addPictureBTN = mDialog.findViewById(R.id.addImageBTN);
        picture = mDialog.findViewById(R.id.pictureIV);

        exit.setOnClickListener(v -> mDialog.dismiss());
    }

    private Dialog initializeDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.adding_product_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        return dialog;
    }

    public void showProductDialog() {
        // Finalize Product button: push to Firestore + update adapter
        addProduct.setOnClickListener(view -> {
            String nameText = name.getText().toString().trim();
            String descText = description.getText().toString().trim();
            String priceText = price.getText().toString().trim();
            String qtyText = quantity.getText().toString().trim();

            if (nameText.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
                Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double parsedPrice = Double.parseDouble(priceText);   // ✅ decimal-safe
                int stock = Integer.parseInt(qtyText);                // ✅ for Firestore field

                // Choose image: apple, banana, or default to minus_sign
                int image;
                if (nameText.equalsIgnoreCase("apple")) {
                    image = R.drawable.apple;
                } else if (nameText.equalsIgnoreCase("banana")) {
                    image = R.drawable.banana;
                } else {
                    image = R.drawable.minus_sign;  // ✅ default
                }

                Map<String, Object> productData = new HashMap<>();
                productData.put("name", nameText);
                productData.put("description", descText);
                productData.put("price", parsedPrice);
                productData.put("stock", stock);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("inventory").document(nameText.toLowerCase())
                        .set(productData)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show();
                            Product product = new Product(nameText, (int) (Math.random() * 10000), image, descText, parsedPrice);
                            adapter.addProduct(product);
                            adapter.notifyDataSetChanged();
                            mDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });

            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });

        mDialog.show();
    }

    public void editProductDialog(Product product) {
        // Populate fields
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(String.valueOf(product.getPrice()));
        quantity.setText("1");  // Optional: could preload existing stock if available

        addProduct.setOnClickListener(view -> {
            try {
                double parsedPrice = Double.parseDouble(price.getText().toString().trim());
                int image = product.getPicture();
                String newName = name.getText().toString().trim();
                String newDesc = description.getText().toString().trim();

                Product updated = new Product(newName, product.getId(), image, newDesc, parsedPrice);
                adapter.replaceProduct(product, updated);
                mDialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Enter valid price", Toast.LENGTH_SHORT).show();
            }
        });

        mDialog.show();
    }
}
