package com.example.italdiszkont;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY =99;
    private SharedPreferences preferences;
    private FirebaseAuth myauth;
    EditText usernameET;
    EditText useremailET;
    EditText userpasswordET;
    EditText userpassword_confirmET;
    Spinner user_spinner;
    EditText addressET;
    EditText phoneET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bundle bundle = getIntent().getExtras();
        int secretkey = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secretkey != 99){
            finish();
        }

        usernameET = findViewById(R.id.username_reg_edittext);
        useremailET = findViewById(R.id.email_edittext);
        userpasswordET = findViewById(R.id.password_edittext);
        userpassword_confirmET = findViewById(R.id.confirm_password_edittext);
        user_spinner = findViewById(R.id.type_spinner);
        addressET = findViewById(R.id.address_edittext);
        phoneET = findViewById(R.id.phone_edittext);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username_pref = preferences.getString("username","");
        String password_pref = preferences.getString("password","");

        usernameET.setText(username_pref);
        userpasswordET.setText(password_pref);
        userpassword_confirmET.setText(password_pref);

        user_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.setAdapter(adapter);

        myauth = FirebaseAuth.getInstance();


    }

    public void regist(View view) {


        String username_str = usernameET.getText().toString();
        String email_str = useremailET.getText().toString();
        String password_str = userpasswordET.getText().toString();
        String password_confirm_str = userpassword_confirmET.getText().toString();
        String address_str= addressET.getText().toString();
        String phone_str = phoneET.getText().toString();

        if(password_str.equals(password_confirm_str)){
            //heppy
        }


        myauth.createUserWithEmailAndPassword(email_str, password_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startShopping();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "user registration failed:"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void exit(View view) {
        finish();
    }

    private void startShopping(){
        Intent intent = new Intent(this, ShopActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String selected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}