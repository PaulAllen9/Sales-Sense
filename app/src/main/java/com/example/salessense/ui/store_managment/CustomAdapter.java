package com.example.salessense.ui.store_managment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<String> texts;
    private ArrayList<Integer> images;

    public CustomAdapter(Context context, ArrayList<String> texts, ArrayList<Integer> images) {
        this.context = context;
        this.texts = texts;
        this.images = images;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.display_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("CustomAdapter", "Binding position: " + position);
        Log.d("CustomAdapter", "Text: " + texts.get(position));
        Log.d("CustomAdapter", "Image: " + images.get(position));
        holder.textView.setText(String.valueOf(texts.get(position)));
        holder.imageView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.imageView);

        }
    }


}


