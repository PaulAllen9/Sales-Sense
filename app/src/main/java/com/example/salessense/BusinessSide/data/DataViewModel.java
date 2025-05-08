package com.example.salessense.BusinessSide.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Brian added
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

import android.util.Pair;

/*
 * Author: ???????
 * Created: ????????
 * Modified by: Brian Yang - 5/6/25
 * - Integrated Firestore to return chart  data
 * - Returns 3 datasets: Sales by Day, Quantity Sold, Stock Levels
 */
public class DataViewModel extends ViewModel {

    // private final MutableLiveData<List<List<BarEntry>>> chartData = new MutableLiveData<>();
    private final MutableLiveData<Pair<List<List<BarEntry>>,
            List<List<String>>>> chartDataWithLabels = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DataViewModel() {
        loadChartDataFromFirestore();
    }

    /*public LiveData<List<List<BarEntry>>> getChartData() {
        return chartData;
    }*/
    public LiveData<Pair<List<List<BarEntry>>, List<List<String>>>> getChartDataWithLabels() {
        return chartDataWithLabels;
    }

    private void loadChartDataFromFirestore() {
        /*
        List<BarEntry> salesByDay = new ArrayList<>();
        List<BarEntry> quantityPerProduct = new ArrayList<>();
        List<BarEntry> stockLevels = new ArrayList<>();

         */

        Map<String, Float> productQuantityMap = new HashMap<>();
        Map<String, Float> dailySalesMap = new TreeMap<>();
        Map<String, Float> inventoryMap = new HashMap<>();

        // Step 1: Pull transactions
        db.collection("transactions")
                .get()
                .addOnSuccessListener(task -> {
                    for (QueryDocumentSnapshot doc : task) {
                        // Extract date
                        Timestamp ts = doc.getTimestamp("timestamp");
                        if (ts != null) {
                            String dateKey = new SimpleDateFormat("MM/dd", Locale.US).format(ts.toDate());
                            float total = doc.getDouble("total") != null ? doc.getDouble("total").floatValue() : 0f;
                            dailySalesMap.put(dateKey, dailySalesMap.getOrDefault(dateKey, 0f) + total);
                        }

                        // Extract item quantities
                        List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");
                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                String product = (String) item.get("productId");
                                Long qtyLong = item.get("quantity") instanceof Long ? (Long) item.get("quantity") : 0L;
                                float qty = qtyLong.floatValue();
                                productQuantityMap.put(product, productQuantityMap.getOrDefault(product, 0f) + qty);
                            }
                        }
                    }

                    // Step 2: Pull inventory stock levels
                    db.collection("inventory")
                            .get()
                            .addOnSuccessListener(inv -> {
                                for (DocumentSnapshot doc : inv) {
                                    String product = doc.getString("name");
                                    if (product != null && doc.contains("stock")) {
                                        Number stock = doc.getDouble("stock");
                                        if (stock != null) {
                                            inventoryMap.put(product, stock.floatValue());
                                        }
                                    }
                                }

                                // Create chart entries

                                // 1. Sales by Day
                                List<BarEntry> salesByDay = new ArrayList<>();
                                List<String> salesByDayLabels = new ArrayList<>();
                                int i = 0;
                                for (Map.Entry<String, Float> entry : dailySalesMap.entrySet()) {
                                    salesByDay.add(new BarEntry(i++, entry.getValue()));
                                    salesByDayLabels.add(entry.getKey());
                                }

                                // 2. Quantity Sold per Product
                                List<BarEntry> quantityPerProduct = new ArrayList<>();
                                List<String> quantityLabels = new ArrayList<>();
                                int j = 0;
                                for (Map.Entry<String, Float> entry : productQuantityMap.entrySet()) {
                                    quantityPerProduct.add(new BarEntry(j++, entry.getValue()));
                                    quantityLabels.add(entry.getKey());
                                }

                                // 3. Stock Levels
                                List<BarEntry> stockLevels = new ArrayList<>();
                                List<String> stockLabels = new ArrayList<>();
                                int k = 0;
                                for (Map.Entry<String, Float> entry : inventoryMap.entrySet()) {
                                    stockLevels.add(new BarEntry(k++, entry.getValue()));
                                    stockLabels.add(entry.getKey());
                                }

                                // Combine
                                List<List<BarEntry>> allEntries = Arrays.asList(salesByDay, quantityPerProduct, stockLevels);
                                List<List<String>> allLabels = Arrays.asList(salesByDayLabels, quantityLabels, stockLabels);

                                // Emit as pair
                                chartDataWithLabels.setValue(new Pair<>(allEntries, allLabels));
                            });
                });
    }
}