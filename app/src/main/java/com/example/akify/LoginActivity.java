package com.example.akify;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Button btn;
    TextView signInTxt;
    EditText idTxt;
    EditText pswdTxt;
    Button loginBtn;
    ProgressBar progressBar;
    public static final String EXTRA_MESSAGE = "com.example.akify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.loginBtn);
        signInTxt = findViewById(R.id.signInBtnText);

        idTxt = findViewById(R.id.editTextid);
        pswdTxt = findViewById(R.id.editTextPassword);

        loginBtn = findViewById(R.id.loginBtn);



        signInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(v -> loginAcc());

    }

    private void loginAcc() {
        String id = idTxt.getText().toString();
        String pswd = pswdTxt.getText().toString();

        boolean isValidated = validateData(id, pswd);
        if(!isValidated)
            return;

        readData(id,pswd);
    }

    private void readData(String id, String pswd) {
        chnageInProgress(true);
        //to get path till "Users"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                chnageInProgress(false);

                task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        //if user exists
                        if(dataSnapshot.exists()){

                            //if password is correct
                            if(dataSnapshot.child("pswd").getValue().toString().equals(pswd)){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                String name = dataSnapshot.child("name").getValue().toString();
                                intent.putExtra(EXTRA_MESSAGE,name);
                                startActivity(intent);
                            }

                            //if password is incorrect
                            else{
                                Toast.makeText(LoginActivity.this, "Password is incorrect, Please enter correct password", Toast.LENGTH_SHORT).show();
                            }

                        }
                        //if user doesn't exist
                        else {
                            Toast.makeText(LoginActivity.this, "User doesn't exist, Please sign in.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    //error of server side
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "It's not u, it's us", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    private void chnageInProgress(boolean inProgress) {
        if(inProgress){
            loginBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String id, String pswd) {
        if(id.isEmpty()){
            idTxt.setError("Enter correct Username");
            return false;
        }

        if(pswd.length()<6){
            pswdTxt.setError("Password length must be greater than 6");
            return false;
        }

        return true;
    }
}