package com.example.salessense.BusinessSide.store_managment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Dialogs.AddProductDialog;
import com.example.salessense.Product;
import com.example.salessense.R;
import com.example.salessense.databinding.FragmentManagmentBinding;

import java.util.ArrayList;

public class StoreFragment extends Fragment {
    /*
     * Author: Paul Allen
     * Last modified: 4/29/25
     *
     * This fragment is the general controls for the store management aspect of this project.
     * */

    private FragmentManagmentBinding binding;
    private Button addProductButon;

    private TextView emptyMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StoreViewModel storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        // Inflate the layout using View Binding
        binding = FragmentManagmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up LiveData observation
        // I don't think we need this, but may be useful for when we integrate with the remote database
        emptyMessage = binding.emptyMessage;
        storeViewModel.getText().observe(getViewLifecycleOwner(), emptyMessage::setText);

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;  // Make sure this matches your XML ID
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        ArrayList<Product> products = new ArrayList<>();

        populateRecyclerView(products); // Populate the data

        // Set up the adapter
        CustomAdapter customAdapter = new CustomAdapter(getContext(), products);
        recyclerView.setAdapter(customAdapter); // Attaching the adapter

        // Search bar functionality
        EditText searchText = binding.searchBar;
        Button searchBTN = binding.searchButton;
        searchBTN.setOnClickListener(view -> {
            customAdapter.getFilter().filter(searchText.getText().toString().trim());
        });

        // Adding a product
        addProductButon = binding.addProductButton;
        addProductButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductDialog dialog =   new AddProductDialog(getContext(), customAdapter);
                dialog.showProductDialog();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void populateRecyclerView(ArrayList<Product> products) {
        /*
        * Author: Paul Allen
        * Last modified: 4/17/25
        *
        * This function is supposed to add items to the arrays, which will be used to craft the
        * cardViews inside of the recycler view.
        * */

        Product appleProduct= new Product("Apple",1,R.drawable.apple,"This is the most delicous apple",.59);
        Product bananaProduct= new Product("Banana",2,R.drawable.banana,"This banana is crazy good.",.69);
        products.add(appleProduct);
        products.add(bananaProduct);


        if(products.isEmpty()){
            emptyMessage.setVisibility(VISIBLE);
        }
        else{
            emptyMessage.setVisibility(GONE);
        }


    }
}
