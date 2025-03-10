package com.example.salessense.ui.store_managment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.R;
import com.example.salessense.databinding.FragmentManagmentBinding;

import java.util.ArrayList;

public class StoreFragment extends Fragment {

    private FragmentManagmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StoreViewModel storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        // Inflate the layout using View Binding
        binding = FragmentManagmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;  // Make sure this matches your XML ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> texts = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();
        populateRecyclerView(texts, images); // Populate the data


        // Set up the adapter
        CustomAdapter customAdapter = new CustomAdapter(getContext(), texts, images);
        recyclerView.setAdapter(customAdapter); // Attach the adapter here


        // Set up LiveData observation
        final TextView textView = binding.textHome;
        storeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void populateRecyclerView(ArrayList<String> texts, ArrayList<Integer> images) {
        // Add items
        texts.add("Apple");
        texts.add("Banana");
        images.add(R.drawable.apple);  // Reference the drawable resource for apple
        images.add(R.drawable.banana); // Reference the drawable resource for banana


    }
}
