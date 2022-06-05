package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
private TextInputEditText email,password;
private TextView forgetPassword;
Button signIn,signUp;
private FirebaseAuth mAuth;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // FindView By Id....
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        forgetPassword = findViewById(R.id.forgetPassword);
        signIn = findViewById(R.id.lsignIn);
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        signUp = findViewById(R.id.lsignUp);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        // Forget Password....
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent p = new Intent(Login.this,ForgetPassword.class);
              startActivity(p);
              finish();
            }
        });


        // SignUp..
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Registration.class);
                startActivity(i);
              finish();
            }
        });


        // SignIn...
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString();
                String Password = password.getText().toString();
                dialog.show();
                if (Email.isEmpty()){
                    Toast.makeText(Login.this,"Enter Vaild EmailID !",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                else if (Password.isEmpty()){
                    Toast.makeText(Login.this,"Enter Password !",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                else if(Password.length()<6){
                    Toast.makeText(Login.this,"Password is too short!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(Password.matches(pattern)){
                    mAuth.signInWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(Login.this,MainActivity.class);
                                        dialog.dismiss();
                                        startActivity(i);

                                        finish();

                                    } else {
                                        dialog.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this,"SignIn Failed !!",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                }
            }
        });
    }
}