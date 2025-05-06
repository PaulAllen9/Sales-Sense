package com.example.salessense.Dialogs;

import static android.view.View.GONE;

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

    Dialog mDialog;
    private ImageButton exit, plus, minus;
    private TextView productNameTV, productDescription;
    private Button editProductBTN, deleteProductBTN;
    private EditText quantity;
    private ImageView productImage;

    private CustomAdapter adapter;
    private Context context;
    public ViewProductDialog(Context context, CustomAdapter adapter){
        this.context=context;
        this.adapter=adapter;
        mDialog = initializeDialog(context);
        this.adapter=adapter;

        exit = mDialog.findViewById(R.id.closePopupButton);

        ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
        productNameTV = mDialog.findViewById(R.id.productNameTV);
        productImage = mDialog.findViewById(R.id.productImage);
        productDescription = mDialog.findViewById(R.id.productDescription);

        quantity = mDialog.findViewById(R.id.quantityET);
        editProductBTN = mDialog.findViewById(R.id.editProductBTN);
        deleteProductBTN = mDialog.findViewById(R.id.deleteProductBTN);
        plus = mDialog.findViewById(R.id.plusButton);
        minus = mDialog.findViewById(R.id.minusButton);

        exit.setOnClickListener(v -> mDialog.dismiss());


    }
    private Dialog initializeDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.product_popup_display_card);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        return dialog;
    }
    public void viewProductBusiness(Product product) {

        productNameTV.setText(product.getName());
        productImage.setImageResource(product.getPicture());
        productDescription.setText(product.getDescription()); // Assuming Product class has a description field
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
        editProductBTN.setOnClickListener(view -> {
            AddProductDialog dialog = new AddProductDialog(context,adapter);
            dialog.editProductDialog(product);
            mDialog.dismiss();
        });
        deleteProductBTN.setOnClickListener(view -> {
            adapter.removeProduct(product);
            mDialog.dismiss();
        });
        // Set product details in the dialog

        mDialog.show();
    }
    public void viewProductCustomer(Product product) {

        productNameTV.setText(product.getName());
        productImage.setImageResource(product.getPicture());
        productDescription.setText(product.getDescription()); // Assuming Product class has a description field
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
        editProductBTN.setVisibility(GONE);
        deleteProductBTN.setVisibility(GONE);
        // Set product details in the dialog

        mDialog.show();
    }
}