package com.example.salessense.Security;
//
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salessense.R;

public class accountCreation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create);
    }
    public void goBack(View view){
       Intent intent = new Intent(this, signInStart.class);
       startActivity(intent);
       finish();
    }
    public void accountCreate(View view) {
        EditText email = findViewById(R.id.email);
        EditText user = findViewById(R.id.userName);
        EditText pass = findViewById(R.id.password);

        String emailText = email.getText().toString();
        String userText = user.getText().toString();
        String passText = pass.getText().toString();


        if (emailText.isEmpty() || userText.isEmpty() || passText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            pass.setText("");
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            pass.setText("");
            user.setText("");
            email.setText("");
        }
        else {
            Toast.makeText(this, "Account created for: " + userText, Toast.LENGTH_LONG).show();
            goBack(view);
        }
    }
}
