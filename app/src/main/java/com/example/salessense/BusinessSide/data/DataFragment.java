package com.example.salessense.BusinessSide.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.BusinessSide.register.CustomAdapterRegister;
import com.example.salessense.BusinessSide.register.RegisterViewModel;
import com.example.salessense.R;
import com.example.salessense.databinding.FragmentDataBinding;
import com.example.salessense.databinding.FragmentRegisterBinding;

import java.util.ArrayList;

public class DataFragment extends Fragment {

    private FragmentDataBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        RegisterViewModel dashboardViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;  // Make sure this matches your XML ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> dataTitles = new ArrayList<>();
        ArrayList<Integer> dataVisuals = new ArrayList<>();

        populateRecyclerView(dataTitles, dataVisuals); // Populate the data


        // Set up the adapter
        CustomAdapterData customAdapter = new CustomAdapterData(getContext(), dataTitles, dataVisuals);
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
    private void populateRecyclerView(ArrayList<String> dataTitles, ArrayList<Integer> dataVisuals) {
        // Add items
        for (int i = 0; i < 6; i++) {
            dataTitles.add("Graph Title");

            dataVisuals.add(R.drawable.baseline_trending_up_24);  // Reference the drawable resource for apple

        }
    }
}