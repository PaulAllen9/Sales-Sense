package com.example.salessense.BusinessSide.store_managment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Dialogs.ViewProductDialog;
import com.example.salessense.Product;
import com.example.salessense.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {
    /*
     * Author: Paul Allen
     * Last modified: 4/17/25
     *
     * This class serves as the controller for the recycler view.
     * Here one can add, remove, replace, or filter the contents of the recycler view.
     * */
    private Context context;
    private ArrayList<Product> products;
    private ArrayList<Product> allProducts;


    public CustomAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        allProducts = new ArrayList<>(products); // Creates a full copy of all of the products may not need after implementing remote database
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
    public void replaceProduct(Product oldProduct, Product newProduct){
        int position = products.indexOf(oldProduct); // Get the position of the product
        if (position != -1) { // Ensure product exists in the list
            products.set(position,newProduct);
            notifyItemRemoved(position); // Notify adapter of item removed
            notifyItemRangeChanged(position, products.size()); // Update affected items
        }
    }
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allProducts); // Show all items if no filter is applied
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product product : allProducts) {
                    if (product.getName().toLowerCase().contains(filterPattern) ||
                            product.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((ArrayList<Product>) results.values);
            notifyDataSetChanged(); // Notify the adapter of data changes
        }
    };
}

