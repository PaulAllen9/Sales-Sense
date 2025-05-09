package com.example.salessense.Security;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Scene;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import com.example.salessense.MainActivity;
import com.example.salessense.R;
import com.example.salessense.databinding.ActivityMainBinding;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class signInStart extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Scene Main;

    private Scene Account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_display);
        Main = Scene.getSceneForLayout((ViewGroup) findViewById(R.id.rootContainer),
                R.layout.activity_main, this);
        Account = Scene.getSceneForLayout((ViewGroup) findViewById(R.id.rootContainer),
                R.layout.account_create, this);

    }

    private String bytesToHex(byte[] hash){
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void SignInPep(View view) {
        //temp for signing in
        EditText user = findViewById(R.id.user);
        EditText pass = findViewById(R.id.pass);
        String shad = "";

        Intent intent = new Intent(this, MainActivity.class);
        //attempted Sha-256 Hex one-way encryption.
        //Will need to grab associated Sha password from fireStore to verify correct login.
        try{
            MessageDigest di = MessageDigest.getInstance("SHA-256");
            byte[] encoded = di.digest(pass.getText().toString().getBytes(StandardCharsets.UTF_8));
            shad = bytesToHex(encoded);
        } catch (Exception e){
            Toast.makeText(this, "Encryption failed", Toast.LENGTH_SHORT).show();
        }
        //compare username and password to known server values (username and sha password) in this case the letter 't' is the pass.
        if (user.getText().toString().equals("user")  && shad.equals("e3b98a4da31a127d4bde6e43033f66ba274cab0eb7eb1c70ec41402bf6273dd8")) {
            try {
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e("CRASH", "Error in SignInPep: " + e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "ERROR: wrong credentials!", Toast.LENGTH_SHORT).show();
            pass.setText("");
        }
    }

    public void accountCreate(View view) {
        Intent intent = new Intent(this, accountCreation.class);
        startActivity(intent);
        finish();
    }

}
