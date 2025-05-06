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
     * Modified by: Brian Yang - 5/5/25
     *
     * This class serves as the controller for the recycler view.
     * Here one can add, remove, replace, or filter the contents of the recycler view.
     * */
    private Context context;
    private ArrayList<Product> products;
    private ArrayList<Product> allProducts;

    public CustomAdapter(Context context, ArrayList<Product> products) {
        /*
         * Author: Paul Allen
         * Last modified: 4/17/25
         */
        this.context = context;
        this.products = products;
        allProducts = new ArrayList<>(products); // Creates a full copy of all of the products may not need after implementing remote database
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
         * Author: Paul Allen
         * Last modified: 4/17/25
         */
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.store_display_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*
         * Author: Paul Allen
         * Last modified: 4/29/25
         * Modified by: Brian Yang - 5/6/25 - Removed buy button logic for store list view only
         *
         */
        Product product = products.get(position); // Get the correct product

        holder.textView.setText(product.getName());
        holder.imageView.setImageResource(product.getPicture());

        // Set click listener to show product details
        holder.itemView.setOnClickListener(view -> {
            ViewProductDialog viewProduct = new ViewProductDialog(context, this);
            viewProduct.viewProductBusiness(product);
        });
    }

    @Override
    public int getItemCount() {
        /*
         * Author: Paul Allen
         * Last modified: 4/05/25
         */
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /*
         * Author: Paul Allen
         * Last modified: 4/17/25
         * Modified by: Brian Yang - 5/5/25 – Removed buyButton for visual-only product listing
         */
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void addProduct(Product product) {
        /*
         * Author: Paul Allen
         * Last modified: 4/28/25
         */
        products.add(product);
    }

    public void removeProduct(Product product) {
        /*
         * Author: Paul Allen
         * Last modified: 4/28/25
         */
        int position = products.indexOf(product); // Get the position of the product
        if (position != -1) {
            products.remove(position);
            notifyItemRemoved(position); // Notify adapter of item removed
            notifyItemRangeChanged(position, products.size()); // Update affected items
        }
    }

    public void replaceProduct(Product oldProduct, Product newProduct) {
        /*
         * Author: Paul Allen
         * Last modified: 4/29/25
         */
        int position = products.indexOf(oldProduct); // Get the position of the product
        if (position != -1) {
            products.set(position, newProduct);
            notifyItemRemoved(position); // Notify adapter of item removed
            notifyItemRangeChanged(position, products.size()); // Update affected items
        }
    }

    @Override
    public Filter getFilter() {
        /*
         * Author: Paul Allen
         * Last modified: 4/29/25
         */
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        /*
         * Author: Paul Allen
         * Last modified: 4/29/25
         *
         * Call by getFilter().performFilter(key)
         * Should filter the results of the recycler view based off of the search bars contents
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // Show all items if no filter is applied
                filteredList.addAll(allProducts);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                // Search all the product names and descriptions to see if it contains the key
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
            /*
             * Author: Paul Allen
             * Last modified: 4/29/25
             */
            products.clear();
            products.addAll((ArrayList<Product>) results.values);
            notifyDataSetChanged(); // Notify the adapter of data changes
        }
    };
}
