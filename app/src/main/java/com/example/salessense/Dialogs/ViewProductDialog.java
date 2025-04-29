package com.example.salessense.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salessense.BusinessSide.store_managment.CustomAdapter;
import com.example.salessense.Product;
import com.example.salessense.R;

public class ViewProductDialog {
    private Product product;
    public static void showProductDialog(Context context, CustomAdapter adapter, Product product) {
        Dialog mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.product_popup_display_card);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);

        ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
        TextView productNameTV = mDialog.findViewById(R.id.productNameTV);
        ImageView productImage = mDialog.findViewById(R.id.productImage);
        TextView productDescription = mDialog.findViewById(R.id.productDescription);

        EditText quantity = mDialog.findViewById(R.id.quantityET);
        Button deleteProductBTN = mDialog.findViewById(R.id.deleteProductBTN);
        ImageButton plus = mDialog.findViewById(R.id.plusButton);
        ImageButton minus = mDialog.findViewById(R.id.minusButton);

        plus.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(quantity.getText().toString().trim());
            quantity.setText(String.valueOf(currentQuantity + 1));
        });
        minus.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(quantity.getText().toString().trim());
            if(currentQuantity>1){
                quantity.setText(String.valueOf(currentQuantity - 1));
            }
            else{
                quantity.setText(String.valueOf(1));
            }
        });


        deleteProductBTN.setOnClickListener(view -> {
            adapter.removeProduct(product);
            mDialog.dismiss();
        });
        // Set product details in the dialog
        productNameTV.setText(product.getName());
        productImage.setImageResource(product.getPicture());
        productDescription.setText(product.getDescription()); // Assuming Product class has a description field

        exit.setOnClickListener(v -> mDialog.dismiss());

        mDialog.show();
    }
}