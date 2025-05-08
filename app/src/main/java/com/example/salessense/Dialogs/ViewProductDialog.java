package com.example.salessense.Dialogs;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

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

import com.example.salessense.BusinessSide.TemporaryCartData;
import com.example.salessense.BusinessSide.register.CustomAdapterRegister;
import com.example.salessense.BusinessSide.store_managment.CustomAdapter;
import com.example.salessense.Product;
import com.example.salessense.R;

public class ViewProductDialog {

    Dialog mDialog;
    private ImageButton exit, plus, minus;
    private TextView productNameTV, productDescription;
    private Button editProductBTN, deleteProductBTN, addToTransactionBTN;
    private EditText quantity;
    private ImageView productImage;

    private CustomAdapter adapter;
    private CustomAdapterRegister registerAdapter;
    private Context context;

    private TemporaryCartData cart;

    public ViewProductDialog(Context context, CustomAdapter adapter){
        this.context=context;
        this.adapter=adapter;
        mDialog = initializeDialog(context);

        exit = mDialog.findViewById(R.id.closePopupButton);

        ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
        productNameTV = mDialog.findViewById(R.id.productNameTV);
        productImage = mDialog.findViewById(R.id.productImage);
        productDescription = mDialog.findViewById(R.id.productDescription);

        quantity = mDialog.findViewById(R.id.quantityET);

        addToTransactionBTN= mDialog.findViewById(R.id.addToTransaction);
        editProductBTN = mDialog.findViewById(R.id.editProductBTN);
        deleteProductBTN = mDialog.findViewById(R.id.deleteProductBTN);
        plus = mDialog.findViewById(R.id.plusButton);
        minus = mDialog.findViewById(R.id.minusButton);

        exit.setOnClickListener(v -> mDialog.dismiss());


    }
    public ViewProductDialog(Context context, CustomAdapterRegister adapter){
        this.context=context;
        this.registerAdapter=adapter;
        mDialog = initializeDialog(context);

        exit = mDialog.findViewById(R.id.closePopupButton);

        ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
        productNameTV = mDialog.findViewById(R.id.productNameTV);
        productImage = mDialog.findViewById(R.id.productImage);
        productDescription = mDialog.findViewById(R.id.productDescription);

        quantity = mDialog.findViewById(R.id.quantityET);

        addToTransactionBTN= mDialog.findViewById(R.id.addToTransaction);
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
        /*
        * Author: Paul Allen
        * last modified 5-6-2025
        *
        * When a business user attempts to view product details from the store management page
        * */
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
        cart = new TemporaryCartData(context);

        addToTransactionBTN.setOnClickListener(view -> {
            product.setQuantityInCart(Integer.parseInt(quantity.getText().toString().trim()));
            cart.addProduct(product);

            mDialog.dismiss();
        });

        editProductBTN.setOnClickListener(view -> {
            AddProductDialog dialog = new AddProductDialog(context,adapter);
            //product.setQuantityInCart(Integer.parseInt(quantity.getText().toString().trim()));
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

    public void viewProductCustomerStore(Product product) {
        /*
         * Author: Paul Allen
         * last modified 5-6-2025
         *
         * When a Customer user attempts to view product details from the store page
         * */
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
        addToTransactionBTN.setOnClickListener(view -> {
            TemporaryCartData transaction = new TemporaryCartData(context);
            product.setQuantityInCart(Integer.parseInt(quantity.getText().toString().trim()));
            transaction.addProduct(product);
        });
        editProductBTN.setVisibility(GONE);
        deleteProductBTN.setVisibility(GONE);
        // Set product details in the dialog

        mDialog.show();
    }
    public void viewProductRegister(Product product) {

        /*
        * Author: Paul Allen
        * Last modified: 5/7/25
        *
        * When a user inspects an item in their cart or register page
        * */
        productNameTV.setText(product.getName());
        productImage.setImageResource(product.getPicture());
        productDescription.setText(product.getDescription()); // Assuming Product class has a description field
        quantity.setText(""+product.getQuantityInCart());
        cart = new TemporaryCartData(context);

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
        //Update Button
        addToTransactionBTN.setText("Update Quantity");
        addToTransactionBTN.setOnClickListener(view -> {
            product.setQuantityInCart(Integer.parseInt(quantity.getText().toString().trim()));
            cart.updateProductQuantity(product);
            registerAdapter.updateProduct(product);
            mDialog.dismiss();
        });

        editProductBTN.setVisibility(INVISIBLE);
        deleteProductBTN.setOnClickListener(view -> {
            cart.removeProduct(product);
            registerAdapter.removeProduct(product);
            mDialog.dismiss();
        });
        // Set product details in the dialog

        mDialog.show();
    }
}