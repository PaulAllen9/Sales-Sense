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

import com.example.salessense.BusinessSide.TemporaryCartData;
import com.example.salessense.Product;
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
 * Author: Paul Allen
 * First created: 4-20-25
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

        ArrayList<Product> products = new ArrayList<>();

        //populateRecyclerView(products); // Populate the data
        TemporaryCartData transaction = new TemporaryCartData(getContext());
        products=transaction.getCartContents();


        TextView amount= binding.totalAmount;
        // Set up the adapter
        CustomAdapterRegister customAdapter = new CustomAdapterRegister(getContext(), products, amount);
        recyclerView.setAdapter(customAdapter); // Attach the adapter here
        customAdapter.updateTotal();

        // Set up LiveData observation
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
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
    private void populateRecyclerView(ArrayList<Product> products) {
        TemporaryCartData transaction = new TemporaryCartData(getContext());
        products=transaction.getCartContents();

    }
}
