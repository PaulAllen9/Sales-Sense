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
import com.github.mikephil.charting.data.BarEntry;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: ????????????
 * Last modified: ????????
 * Modified by: Brian Yang - 5/6/25
 * - Added MPAndroidChart support
 * - Listens for BarEntry chart data from ViewModel and updates adapter
 * - Listens for BarEntry data and X-axis labels from ViewModel
 * - Passes both into CustomAdapterData for accurate MPAndroidChart rendering
 */
public class DataFragment extends Fragment {

    private FragmentDataBinding binding;
    private DataViewModel dataViewModel; // Brian added

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // RegisterViewModel dashboardViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;  // Make sure this matches your XML ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Chart titles
        ArrayList<String> dataTitles = new ArrayList<>();
        // Brian removed placeholders
        // ArrayList<Integer> dataVisuals = new ArrayList<>();
        // populateRecyclerView(dataTitles, dataVisuals); // Populate the data
        dataTitles.add("Sales by Day");
        dataTitles.add("Quantity Sold per Product");
        dataTitles.add("Stock Level Overview");

        // Initialize ViewModel and observe chart data
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        /*dataViewModel.getChartData().observe(getViewLifecycleOwner(), chartDataList -> {
            if (chartDataList != null && chartDataList.size() == dataTitles.size()) {
                CustomAdapterData adapter = new CustomAdapterData(getContext(), dataTitles, chartDataList);
                recyclerView.setAdapter(adapter);
            }
        });*/
        dataViewModel.getChartDataWithLabels().observe(getViewLifecycleOwner(),
                chartPair -> {
            if (chartPair != null) {
                List<List<BarEntry>> chartDataList = chartPair.first;
                List<List<String>> chartLabelsList = chartPair.second;

                if (chartDataList.size() == dataTitles.size() && chartLabelsList.size() == dataTitles.size()) {
                    CustomAdapterData adapter = new CustomAdapterData(getContext(), dataTitles,
                            chartDataList, chartLabelsList);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        // Brian added, then removed: was for testing
        // List<List<BarEntry>> chartDataList = new ArrayList<>();

        /* Brian added then removed for testing
        // : Chart 1: Sales by Day (x = day, y = total sales)
        List<BarEntry> salesByDay = new ArrayList<>();
        salesByDay.add(new BarEntry(1, 10));
        salesByDay.add(new BarEntry(2, 14));
        salesByDay.add(new BarEntry(3, 9));
        chartDataList.add(salesByDay);

        // Brian added: Chart 2: Quantity Sold per Product (x = product index, y = quantity)
        List<BarEntry> quantityPerProduct = new ArrayList<>();
        quantityPerProduct.add(new BarEntry(0, 30)); // Apple
        quantityPerProduct.add(new BarEntry(1, 18)); // Banana
        quantityPerProduct.add(new BarEntry(2, 22)); // Orange
        chartDataList.add(quantityPerProduct);

        // Brian added: Chart 3: Stock Levels from Inventory (x = product index, y = current stock)
        List<BarEntry> stockLevels = new ArrayList<>();
        stockLevels.add(new BarEntry(0, 44)); // Apple
        stockLevels.add(new BarEntry(1, 60)); // Banana
        stockLevels.add(new BarEntry(2, 35)); // Kiwi
        chartDataList.add(stockLevels);

        // Set up the adapter
        CustomAdapterData customAdapter = new CustomAdapterData(getContext(), dataTitles, chartDataList);
        recyclerView.setAdapter(customAdapter); // Attach the adapter here
        */

        // Set up LiveData observation
        final TextView textView = binding.textDashboard;
        // dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        textView.setText("Explore Your Data!");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /*
    private void populateRecyclerView(ArrayList<String> dataTitles, ArrayList<Integer> dataVisuals) {
        // Add items
        for (int i = 0; i < 6; i++) {
            dataTitles.add("Graph Title");

            dataVisuals.add(R.drawable.baseline_trending_up_24);  // Reference the drawable resource for apple

        }
    }

     */
}