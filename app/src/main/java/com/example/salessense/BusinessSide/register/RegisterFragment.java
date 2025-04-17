package com.example.salessense.BusinessSide.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.databinding.FragmentRegisterBinding;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        RegisterViewModel dashboardViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;  // Make sure this matches your XML ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<Integer> productQuantities = new ArrayList<>();
        ArrayList<Float> productPrices = new ArrayList<>();

        populateRecyclerView(productNames, productQuantities, productPrices); // Populate the data


        // Set up the adapter
        CustomAdapterRegister customAdapter = new CustomAdapterRegister(getContext(), productNames, productQuantities, productPrices);
        recyclerView.setAdapter(customAdapter); // Attach the adapter here


        // Set up LiveData observation
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void populateRecyclerView(ArrayList<String> productNames, ArrayList<Integer> productQuantities, ArrayList<Float> productPrices) {
        // Add items
        for (int i = 0; i < 25; i++) {
            productNames.add("Apple");

            productQuantities.add(i);  // Reference the drawable resource for apple

            productPrices.add((float) (i*2+.333));
        }

    }
}