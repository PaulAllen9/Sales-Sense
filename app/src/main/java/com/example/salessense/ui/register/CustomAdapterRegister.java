package com.example.salessense.ui.register;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;

import java.util.ArrayList;

public class CustomAdapterRegister extends RecyclerView.Adapter<CustomAdapterRegister.MyViewHolder>{

    private Context context;
    private ArrayList<String> productNames;
    private ArrayList<Integer> productQuantities;
    private ArrayList<Float> productPrices;


    public CustomAdapterRegister(Context context, ArrayList<String> productNames, ArrayList<Integer> productQuantities, ArrayList<Float> productPrices) {
        this.context = context;
        this.productNames = productNames;
        this.productQuantities = productQuantities;
        this.productPrices = productPrices;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.register_display_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameIV.setText(String.valueOf(productNames.get(position)));
        holder.quantityTV.setText(String.valueOf(productQuantities.get(position)));
        holder.priceIV.setText(String.valueOf(productPrices.get(position)));
    }

    @Override
    public int getItemCount() {
        return productNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameIV;
        TextView quantityTV;
        TextView priceIV;


        public MyViewHolder(View itemView) {
            super(itemView);
            nameIV=itemView.findViewById(R.id.itemNameTV);
            quantityTV=itemView.findViewById(R.id.quantityTV);
            priceIV=itemView.findViewById(R.id.priceTV);
        }
    }


}


