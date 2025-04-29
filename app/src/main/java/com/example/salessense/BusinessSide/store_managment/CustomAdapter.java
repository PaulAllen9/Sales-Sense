package com.example.salessense.BusinessSide.store_managment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Dialogs.ViewProductDialog;
import com.example.salessense.Product;
import com.example.salessense.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Product> products;

    public CustomAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.store_display_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = products.get(position); // Get the correct product

        holder.textView.setText(product.getName());
        holder.imageView.setImageResource(product.getPicture());

        // Set click listener to show product details
        holder.itemView.setOnClickListener(view -> {
            ViewProductDialog.showProductDialog(context, this,product);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
    public void addProduct(Product product){
        products.add(product);
    }
    public void removeProduct(Product product){
        int position = products.indexOf(product); // Get the position of the product
        if (position != -1) { // Ensure product exists in the list
            products.remove(position);
            notifyItemRemoved(position); // Notify adapter of item removed
            notifyItemRangeChanged(position, products.size()); // Update affected items
        }
    }


}


