package com.example.salessense.ui.store_managment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;

import java.util.ArrayList;

public class StoreManagementRecycleAdapter extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<String> texts;
    ArrayList<Integer> images;
    CustomAdapter customAdapter;
    Context context;

    StoreManagementRecycleAdapter(Context context){
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.store_management_recycler);

        recyclerView = findViewById(R.id.store_managment_recylcer);

        texts  = new ArrayList<>();
        images = new ArrayList<>();


        //will generate the xml elements to read/list the data
        customAdapter = new CustomAdapter(StoreManagementRecycleAdapter.this,texts,images);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));



    }

    void importData(ArrayList arr, ArrayList imageArr){
        // Get data from some where, might need ot change format
        // Add all data to this recycler view
        for(int i = 0; i<arr.size(); i++){
            texts.add((String)arr.get(i));
            images.add((int)imageArr.get(i));


        }
    }

//    void storeDataInArray(){
//        Cursor cursor = myDB.readAllData();
//
//        if(cursor.getCount()==0){
//            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            while(cursor.moveToNext()){
//                user_id.add(cursor.getInt(0));
//                attempt_num.add(cursor.getInt(1));
//                correct.add(cursor.getInt(2));
//                wrong.add(cursor.getInt(3));
//            }
//        }
//
//
//    }
}