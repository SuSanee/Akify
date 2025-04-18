package com.example.akify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn;
    ProgressBar prgBar;
    EditText nameTxt;
    EditText idTxt;
    EditText emailTxt;
    EditText pswdTxt;
    TextView loginTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginTxt = findViewById(R.id.loginBtnText);
        nameTxt = findViewById(R.id.editTextName);
        idTxt = findViewById(R.id.editTextid);
        emailTxt = findViewById(R.id.editTextEmailAddress);
        pswdTxt = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.signInBtn);
        prgBar = findViewById(R.id.progressBar);

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        DatabaseReference database;
//
//        database = FirebaseDatabase.getInstance().getReference("Users");
//
//        User user = new User("user", "u1", "u1@gmail.com", "123456");
//
//        database.child(user.getId()).setValue(user);
//
        btnSignIn.setOnClickListener(v -> createAccount());
    }

    private void createAccount(){
        String name = nameTxt.getText().toString();
        String id = idTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String pswd = pswdTxt.getText().toString();

        boolean isValidated = validateData(name,id,email,pswd);
        if(!isValidated)
            return;

        registrationInFirebase(name,id,email,pswd);

    }

    public void registrationInFirebase(String name, String id, String email, String pswd){
        changeInProgress(true);

        DatabaseReference database;

        database = FirebaseDatabase.getInstance().getReference("Users");

        User user = new User(name,id,email,pswd);

        database.child(id).setValue(user).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(SignInActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void changeInProgress(boolean inProgress){
        if(inProgress){
            btnSignIn.setVisibility(View.GONE);
            prgBar.setVisibility(View.VISIBLE);
        }
        else{
            btnSignIn.setVisibility(View.VISIBLE);
            prgBar.setVisibility(View.GONE);
        }
    }

    private boolean validateData(String name, String id, String email, String pswd){

        if(name.equals("")) {
            nameTxt.setError("Enter name");
            return false;
        }

        if(id.equals("")) {
            idTxt.setError("Enter id");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Email is invalid");
            return false;
        }

        if(pswd.length()<6){
            pswdTxt.setError("Password must contain minimum 6 letters");
            return false;
        }
        return true;
    }
}