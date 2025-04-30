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

import com.example.salessense.BusinessSide.store_managment.CustomAdapter;
import com.example.salessense.Product;
import com.example.salessense.R;

public class AddProductDialog {
    private Dialog mDialog;
    private ImageButton exit;
    private Button addProduct, addPictureBTN;
    private EditText name, description, price;
    private ImageView picture;

    private CustomAdapter adapter;
    public AddProductDialog(Context context, CustomAdapter adapter){
        mDialog = initializeDialog(context);
        this.adapter=adapter;

        exit = mDialog.findViewById(R.id.closePopupButton);
        addProduct = mDialog.findViewById(R.id.finalizeBTN);
        name = mDialog.findViewById(R.id.pNameET);
        description = mDialog.findViewById(R.id.pDescriptionET);
        price = mDialog.findViewById(R.id.pPriceET);
        addPictureBTN= mDialog.findViewById(R.id.addImageBTN);
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
        // This dialog settings is for when a user is trying to add a product to their storefront.
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addProduct(new Product(name.getText().toString().trim(),3,R.drawable.apple,description.getText().toString().trim(), Double.parseDouble(price.getText().toString())  ));
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
    public void editProductDialog(Product product) {
        // This dialog setting is when the user is attempting to edit an existing product
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(String.valueOf(product.getPrice()));

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.replaceProduct(product, new Product(name.getText().toString().trim(),3,R.drawable.apple,description.getText().toString().trim(), Double.parseDouble(price.getText().toString())  ));
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
