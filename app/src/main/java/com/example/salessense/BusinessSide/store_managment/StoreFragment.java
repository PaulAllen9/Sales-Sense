package com.example.salessense.BusinessSide.store_managment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salessense.Dialogs.AddProductDialog;
import com.example.salessense.Product;
import com.example.salessense.R;
import com.example.salessense.databinding.FragmentManagmentBinding;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Brian Notes: edited 5/5/25 - adjusted to use Firestore and dynamic data. Removed mock logic
// and added purchase handler.
public class StoreFragment extends Fragment {
    /*
     * Author: Paul Allen
     * Last modified: 4/29/25
     * Modified by: Brian Yang - 5/6/25 - Reconnected Add Product button to Firestore, removed Buy logic
     *
     * This fragment is the general controls for the store management aspect of this project.
     * */

    private FragmentManagmentBinding binding;
    private Button addProductButton;

    private TextView emptyMessage;

    // Brian Notes: Promote these to class-level so Firestore can access them
    private CustomAdapter customAdapter;
    private ArrayList<Product> products = new ArrayList<>();
    // End Notes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StoreViewModel storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        // Inflate the layout using View Binding
        binding = FragmentManagmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up LiveData observation
        emptyMessage = binding.emptyMessage;
        storeViewModel.getText().observe(getViewLifecycleOwner(), emptyMessage::setText);

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Set up the adapter
        customAdapter = new CustomAdapter(getContext(), products);
        recyclerView.setAdapter(customAdapter);

        // Brian added: Load real time data from Firestore inventory
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("inventory").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                products.clear(); // FIX: Prevent duplicate items

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String name = document.getString("name");
                    long id = document.getLong("id") != null ? document.getLong("id") : 0;
                    double price = document.getDouble("price") != null ? document.getDouble("price") : 0.0;
                    String desc = document.getString("description");

                    // Brian added: New logic - Assign default image based on name, else use
                    // minus_sign
                    int image;
                    if (name.equalsIgnoreCase("apple")) {
                        image = R.drawable.apple;
                    } else if (name.equalsIgnoreCase("banana")) {
                        image = R.drawable.banana;
                    } else {
                        image = R.drawable.minus_sign; // Default icon
                    }

                    Product product = new Product(name, (int) id, image, desc, price);
                    products.add(product);
                }

                if (products.isEmpty()) {
                    emptyMessage.setVisibility(VISIBLE);
                } else {
                    emptyMessage.setVisibility(GONE);
                }

                customAdapter.notifyDataSetChanged(); // refresh recyclerView
            } else {
                emptyMessage.setVisibility(VISIBLE);
            }
        });



        // Search bar functionality
        EditText searchText = binding.searchBar;
        Button searchBTN = binding.searchButton;
        searchBTN.setOnClickListener(view -> {
            customAdapter.getFilter().filter(searchText.getText().toString().trim());
        });

        // Adding a product to Firestore (popup dialogue)
        addProductButton = binding.addProductButton;
        addProductButton.setOnClickListener(view -> {
            AddProductDialog dialog = new AddProductDialog(getContext(), customAdapter);
            dialog.showProductDialog();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
    // Brian added: Firestore purchase transaction handler; kept for reference if needed in future.
    // Most likely will not need as this was implemented in 'register' package.
    public void handlePurchase(String productId, int quantity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = "demo-user"; // Replace with authenticated user ID if needed

        DocumentReference productRef = db.collection("inventory").document(productId);

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(productRef);
            Long stock = snapshot.getLong("stock");
            Double price = snapshot.getDouble("price");

            if (stock == null || price == null) {
                throw new FirebaseFirestoreException("Missing stock or price",
                        FirebaseFirestoreException.Code.DATA_LOSS);
            }

            if (stock < quantity) {
                throw new FirebaseFirestoreException("Not enough stock",
                        FirebaseFirestoreException.Code.ABORTED);
            }

            transaction.update(productRef, "stock", stock - quantity);

            Map<String, Object> txn = new HashMap<>();
            txn.put("userId", userId);
            txn.put("timestamp", FieldValue.serverTimestamp());
            txn.put("total", quantity * price);
            txn.put("items", Arrays.asList(
                    new HashMap<String, Object>() {{
                        put("productId", productId);
                        put("quantity", quantity);
                    }}
            ));

            DocumentReference txnRef = db.collection("transactions").document();
            transaction.set(txnRef, txn);

            return null;
        }).addOnSuccessListener(unused -> {
            Log.d("Purchase", "Purchase completed");
            Toast.makeText(getContext(), "Purchase successful!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("Purchase", "Purchase failed", e);
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });


    }

     */
}
