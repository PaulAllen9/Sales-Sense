package com.example.salessense.BusinessSide.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;

import java.util.ArrayList;

/*
 * Author: ?????????
 * First created: ?????
 * Last Modified: 5/6/25 - Brian Yang
 *
 * This class serves as a recycler view adapter that bridges product inventory data with the
 * UI elements in the customer-facing register interface. Its primary responsibility is to
 * dynamically bind product data to each item card in a list.
 * */
public class CustomAdapterRegister extends RecyclerView.Adapter<CustomAdapterRegister.MyViewHolder> {

    private Context context;
    private ArrayList<String> productNames;
    private ArrayList<Integer> productQuantities;
    private ArrayList<Float> productPrices;

    public interface OnCartActionListener {
        void onAddToCart(String productId, int quantity);
    }

    private OnCartActionListener cartActionListener;

    public CustomAdapterRegister(Context context, ArrayList<String> productNames,
                                 ArrayList<Integer> productQuantities,
                                 ArrayList<Float> productPrices, OnCartActionListener listener) {
        this.context = context;
        this.productNames = productNames;
        this.productQuantities = productQuantities;
        this.productPrices = productPrices;
        this.cartActionListener = listener; // Brian add: Saves the callback
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.register_display_card, parent, false);
        return new MyViewHolder(view);
    }

    // Brian Notes: Most of these changes were made to help the flow of data to Firestore.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = productNames.get(position);
        int stock = productQuantities.get(position);
        float price = productPrices.get(position);

        holder.nameTV.setText(name);
        holder.priceTV.setText(String.format("$%.2f ea", price));   // better price formatting
        holder.stockTV.setText("Stock: " + stock);                  // stock label

        holder.addToCartButton.setOnClickListener(v -> {
            int quantity = 1;
            try {
                String input = holder.quantityInput.getText().toString().trim();
                if (!input.isEmpty()) {
                    quantity = Integer.parseInt(input);             // parse quantity
                }
            } catch (NumberFormatException e) {
                quantity = 1;                                       // 1 if input is invalid
            }

            if (cartActionListener != null && quantity > 0) {
                cartActionListener.onAddToCart(name.toLowerCase(), quantity); /* Custom listener
                for Firestore integration */
            }
        });
    }

    @Override
    public int getItemCount() {
        return productNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, priceTV, stockTV;
        EditText quantityInput;
        Button addToCartButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.itemNameTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            stockTV = itemView.findViewById(R.id.quantityTV);
            quantityInput = itemView.findViewById(R.id.quantityInput);
            addToCartButton = itemView.findViewById(R.id.buyButton);
            addToCartButton.setText("Add to Cart");
        }
    }
}
