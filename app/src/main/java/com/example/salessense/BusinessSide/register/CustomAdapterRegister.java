package com.example.salessense.BusinessSide.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Dialogs.ViewProductDialog;
import com.example.salessense.Product;
import com.example.salessense.R;

import org.w3c.dom.Text;

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
    private ArrayList<Product> products;
    private TextView amount;

//    public interface OnCartActionListener {
//        void onAddToCart(String productId, int quantity);
//    }
//
//    private OnCartActionListener cartActionListener;
//
    public CustomAdapterRegister(Context context, ArrayList<Product> products, TextView amount) {
        this.context = context;
        this.products=products;
        this.amount=amount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.register_display_card, parent, false);
        return new MyViewHolder(view);
    }

    // Brian Notes: Most of these changes were made to help the flow of data to Firestore.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTV.setText(product.getName());
        holder.priceTV.setText(String.format("$%.2f ea", product.getPrice()));   // better price formatting
        holder.stockTV.setText("Stock: " + product.getBackStock());                  // stock label
        holder.quantityTV.setText(""+product.getQuantityInCart());
        holder.itemSum.setText(String.format("$%.2f",(product.getQuantityInCart()*product.getPrice())));
        holder.itemView.setOnClickListener(view -> {
            ViewProductDialog edit = new ViewProductDialog(context, this);
            edit.viewProductRegister(product);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, priceTV, stockTV, quantityTV, itemSum;


        public MyViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.itemNameTV);
            priceTV = itemView.findViewById(R.id.itemPriceTV);
            stockTV = itemView.findViewById(R.id.backStock);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            itemSum = itemView.findViewById(R.id.itemSumTV);

        }
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
        updateTotal();
    }

    public void updateProduct(Product oldProduct) {
        /*
         * Author: Paul Allen
         * Last modified: 4/29/25
         */
        notifyItemChanged(products.indexOf(oldProduct));
        updateTotal();

    }
    public void updateTotal(){
        double total=0;
        for(Product i:products){
            total+=(i.getQuantityInCart()*i.getPrice());
        }
        amount.setText(String.format("$%.2f",total));
    }

}
