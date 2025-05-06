package com.example.salessense.BusinessSide.register;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.databinding.FragmentRegisterBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
 * Author: ?????????
 * First created: ?????
 * Last Modified: 5/6/25 - Brian Yang - added live Firestore query to pull name, stock, price,
 * and description. Data is stored in same array lists but not reflects actual inventory. This is
 * now fully dynamic.
 *
 * Original: This served as a customer facing product list viewer with transactional behavior.
 * Modified: An interactive, Firestore connected customer register with live cart functionality and
 * transactional checkout.
 * */

// Brian Notes: This fragment originally displayed hardcoded mock data.
// Brian edited: It now loads live inventory from Firestore, supports cart creation, and performs
// real time transactional checkouts.
public class RegisterFragment extends Fragment implements CustomAdapterRegister.OnCartActionListener {

    private FragmentRegisterBinding binding;
    // Brian added: lists to hold Firestore inventory
    private ArrayList<String> productNames = new ArrayList<>();
    private ArrayList<Integer> productQuantities = new ArrayList<>();
    private ArrayList<Float> productPrices = new ArrayList<>();
    private CustomAdapterRegister customAdapter;
    // Brian added: Local cart state mapping product name to quantity
    private final Map<String, Integer> cart = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel dashboardViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Initialize view binding for layout
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize recycler view and layout manager
        RecyclerView recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Brian added: listener for cart actions
        customAdapter = new CustomAdapterRegister(getContext(), productNames, productQuantities,
                productPrices, this);
        recyclerView.setAdapter(customAdapter);

        loadInventoryFromFirestore();

        // WORK IN PROGRESS - search box
        binding.searchButton.setOnClickListener(v -> showCheckoutDialog());

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    // Brian added: Load inventory data from Firestore
    private void loadInventoryFromFirestore() {
        // initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("inventory").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Clear old data to avoid duplication
                productNames.clear();
                productQuantities.clear();
                productPrices.clear();

                // Populate lists with Firestore data
                for (DocumentSnapshot doc : task.getResult()) {
                    productNames.add(doc.getString("name"));
                    productQuantities.add(doc.getLong("stock").intValue());
                    productPrices.add(doc.getDouble("price").floatValue());
                }

                customAdapter.notifyDataSetChanged(); // Refresh UI
            } else {
                Toast.makeText(getContext(), "Failed to load inventory", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Brian added: Callback when user adds an item to the cart
    @Override
    public void onAddToCart(String productName, int quantity) {
        int currentQty = cart.getOrDefault(productName, 0);
        cart.put(productName, currentQty + quantity);
        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
    }

    // Brian added: Show confirmation dialog summarizing cart contents and total
    private void showCheckoutDialog() {
        if (cart.isEmpty()) {
            Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder summary = new StringBuilder();
        double total = 0.0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productName = entry.getKey();
            int qty = entry.getValue();
            int index = productNames.indexOf(productName);
            if (index == -1) continue;

            float price = productPrices.get(index);
            double subtotal = qty * price;
            summary.append(productName).append(" x").append(qty).append(" = $").append(String.format("%.2f", subtotal)).append("\n");
            total += subtotal;
        }

        summary.append("\nTotal: $").append(String.format("%.2f", total));

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Checkout")
                .setMessage(summary.toString())
                .setPositiveButton("Confirm", (dialog, which) -> handlePurchase(cart))
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Brian added: Handles actual Firestore transaction logic to update stock and record purchase
    private void handlePurchase(Map<String, Integer> cart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.runTransaction(transaction -> {
            double total = 0.0;
            ArrayList<Map<String, Object>> items = new ArrayList<>();

            Map<String, DocumentSnapshot> snapshots = new HashMap<>();

            /* Brian Notes: A Firestore requirement is that all executables are read first and then
             * written after.
             */
            // FIRST: READ all product docs
            for (String productName : cart.keySet()) {
                DocumentReference productRef = db.collection("inventory").document(productName.toLowerCase());
                DocumentSnapshot snapshot = transaction.get(productRef);
                snapshots.put(productName, snapshot);
            }

            // SECOND: Process and WRITE
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                DocumentSnapshot snapshot = snapshots.get(productName);

                if (snapshot == null || !snapshot.exists()) {
                    throw new FirebaseFirestoreException("Product not found: " + productName, FirebaseFirestoreException.Code.NOT_FOUND);
                }

                Long stock = snapshot.getLong("stock");
                Double price = snapshot.getDouble("price");

                if (stock == null || price == null || stock < quantity) {
                    throw new FirebaseFirestoreException("Invalid or insufficient stock for " + productName, FirebaseFirestoreException.Code.ABORTED);
                }

                DocumentReference productRef = db.collection("inventory").document(productName.toLowerCase());
                transaction.update(productRef, "stock", stock - quantity);

                total += price * quantity;

                Map<String, Object> item = new HashMap<>();
                item.put("productId", productName);
                item.put("quantity", quantity);
                items.add(item);
            }

            // record transaction summary in Firestore
            Map<String, Object> txn = new HashMap<>();
            txn.put("userId", "demo-user");
            txn.put("timestamp", FieldValue.serverTimestamp());
            txn.put("total", total);
            txn.put("items", items);

            DocumentReference txnRef = db.collection("transactions").document();
            transaction.set(txnRef, txn);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Checkout complete", Toast.LENGTH_SHORT).show();
            cart.clear(); // clear cart after successful transaction
            loadInventoryFromFirestore(); // REFRESH INVENTORY
        }).addOnFailureListener(e -> {
            Log.e("CheckoutError", e.getMessage(), e);
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}