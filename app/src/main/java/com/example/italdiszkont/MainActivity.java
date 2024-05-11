package com.example.italdiszkont;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 99;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    private FirebaseAuth mauth;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mauth = FirebaseAuth.getInstance();

    }

    public void login(View view) {


        String username_str = username.getText().toString();
        String password_str = password.getText().toString();

         mauth.signInWithEmailAndPassword(username_str, password_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                    startShopping();
                 }
                 else{
                     Toast.makeText(MainActivity.this, "user login failed:"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                 }
             }
         });

    }

    private void startShopping(){
        Intent intent = new Intent(this, ShopActivity.class);

        startActivity(intent);
    }

    public void regist(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();
    }

    public void GuestLogin(View view) {
        mauth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startShopping();
                }
                else{
                    Toast.makeText(MainActivity.this, "user anonym login failed:"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}