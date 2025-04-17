package com.example.salessense.BusinessSide.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;

import java.util.ArrayList;

public class CustomAdapterData extends RecyclerView.Adapter<CustomAdapterData.MyViewHolder>{

    private Context context;
    private ArrayList<String> tableTitles;
    private ArrayList<Integer> tableImages;


    public CustomAdapterData(Context context, ArrayList<String> tableTitles, ArrayList<Integer> tableImages) {
        this.context = context;
        this.tableTitles = tableTitles;
        this.tableImages = tableImages;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.data_display_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tableTitleTV.setText(String.valueOf(tableTitles.get(position)));
        holder.dataVisual.setImageResource(tableImages.get(position));
    }

    @Override
    public int getItemCount() {
        return tableTitles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tableTitleTV;
        ImageView dataVisual;


        public MyViewHolder(View itemView) {
            super(itemView);
            tableTitleTV=itemView.findViewById(R.id.tableTitleTV);
            dataVisual=itemView.findViewById(R.id.dataVisual);
        }
    }


}


