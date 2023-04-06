package com.sahil.garage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        if(prefs.getString("current_username",null)!=null){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginOperation();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignupOperation();
            }
        });
    }

    private void handleLoginOperation() {
        if(emailEditText.getText().toString().isEmpty()){
            return;
        }
        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        String userPassword = prefs.getString(emailEditText.getText().toString(), null);
        SharedPreferences.Editor editor = prefs.edit();


        if(userPassword == null ) {
            // User not registered, show an error message
            Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.equals(passwordEditText.getText().toString())) {
            // Login successful
            // Navigate to DashboardActivity
            editor.putString("current_username", emailEditText.getText().toString());
            editor.apply();
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Incorrect username and password", Toast.LENGTH_SHORT).show();
            // Incorrect email or password, show an error message
        }
    }

    private void handleSignupOperation() {
        String userEmail = emailEditText.getText().toString();
        String userPassword = passwordEditText.getText().toString();



        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(prefs.getString(emailEditText.getText().toString(), null)!=null){
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putString(userEmail, userPassword);
        editor.putString("current_username", emailEditText.getText().toString());
        editor.apply();
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        // User registered, either show a success message or navigate to DashboardActivity
    }
}