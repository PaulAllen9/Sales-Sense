package com.example.salessense.BusinessSide.store_managment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Product;
import com.example.salessense.R;

import org.w3c.dom.Text;

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
            Dialog mDialog = new Dialog(context);
            mDialog.setContentView(R.layout.product_popup_display_card);
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.setCancelable(true);

            ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
            TextView productNameTV = mDialog.findViewById(R.id.productNameTV);
            ImageView productImage = mDialog.findViewById(R.id.productImage);
            TextView productDescription = mDialog.findViewById(R.id.productDescription);

            // Set product details in the dialog
            productNameTV.setText(product.getName());
            productImage.setImageResource(product.getPicture());
            productDescription.setText(product.getDescription()); // Assuming Product class has a description field

            exit.setOnClickListener(v -> mDialog.dismiss());

            mDialog.show();
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


}


