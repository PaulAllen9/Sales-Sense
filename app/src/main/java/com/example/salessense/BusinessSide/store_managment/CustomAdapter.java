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
        Dialog mDialog = new Dialog(context);
        DisplayMetrics metrics = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Clcik Button","Clicked Button");
                mDialog.setContentView(R.layout.product_popup_display_card);
                mDialog.getWindow().setLayout(width,height);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.setCancelable(false);

                ImageButton exit = mDialog.findViewById(R.id.closePopupButton);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });



                mDialog.show();

            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(products.get(position).getName()));
        holder.imageView.setImageResource(products.get(position).getPicture());

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


