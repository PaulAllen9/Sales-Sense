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

import com.example.salessense.BusinessSide.store_managment.CustomAdapter;
import com.example.salessense.Product;
import com.example.salessense.R;

public class AddProductDialog {
    public static void showProductDialog(Context context, CustomAdapter adapter) {
        // Set click listener to show product details
        Dialog mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.adding_product_popup);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);

        ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
        Button addProduct = mDialog.findViewById(R.id.finalizeBTN);
        EditText name = mDialog.findViewById(R.id.pNameET);
        EditText description = mDialog.findViewById(R.id.pDescriptionET);
        EditText price = mDialog.findViewById(R.id.pPriceET);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //customAdapter.removeProduct(products.get(0));
                adapter.addProduct(new Product(name.getText().toString().trim(),3,R.drawable.apple,description.getText().toString().trim(), Double.parseDouble(price.getText().toString())  ));
                mDialog.dismiss();
            }
        });


        // Set product details in the dialog

        exit.setOnClickListener(v -> mDialog.dismiss());

        mDialog.show();
    }
}
