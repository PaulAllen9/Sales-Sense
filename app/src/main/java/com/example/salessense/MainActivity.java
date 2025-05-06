package com.example.salessense;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.salessense.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "Firestore";
    private FirebaseFirestore db;  // Firestore database reference

    private TextView textViewData;  // UI element to show Firestore data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.store_managment, R.id.navigation_register, R.id.navigation_data)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore initialized successfully!");

        // Brian Notes: below 2 lines of code used for testing Firestore
        //textViewData = findViewById(R.id.textViewData); // Connect to UI TextView
        // Fetch Firestore data
        //readDataFromFirestore();

    }

    /* Brian Notes: used for testing Firestore data. Displayed Firestore data on the app.
    private void readDataFromFirestore() {
        db.collection("users")  // Collection name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder data = new StringBuilder();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            data.append("ID: ").append(document.getId()).append("\n")
                                    .append("Name: ").append(document.getString("first"))
                                    .append(" ").append(document.getString("last")).append("\n\n");
                        }
                        textViewData.setText(data.toString());  // Display data in UI
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        */


}