package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
private TextInputEditText email;
private Button forgetPassword;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        // Find By Id...
        email = findViewById(R.id.femailId);
        forgetPassword = findViewById(R.id.forgetPassword1);
        mAuth = FirebaseAuth.getInstance();

        // Forget Password Button...
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                if (Email.isEmpty()){
                    Toast.makeText(ForgetPassword.this,"Enter EmailID!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    mAuth.sendPasswordResetEmail(Email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent i = new Intent(ForgetPassword.this,Login.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}