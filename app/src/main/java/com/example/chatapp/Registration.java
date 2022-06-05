package com.example.chatapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
CircleImageView images;
TextInputEditText name,email,password,confirmPassword;
Button signIn,signUp;
private FirebaseAuth mAuth;
private FirebaseStorage storage;
private FirebaseDatabase database;
Uri imageUri;

ProgressDialog dialog;
ActivityResultLauncher<Intent> resultLauncher ;
//private static final int CAMERA_ACTION_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Find By Id...
        name = findViewById(R.id.rname);
        email = findViewById(R.id.remailId);
        password = findViewById(R.id.rpassword);
        confirmPassword = findViewById(R.id.confirmpassword);
        signIn= findViewById(R.id.rsignIn);
        signUp = findViewById(R.id.rsignUp);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        images = findViewById(R.id.profile_image);
         dialog  = new ProgressDialog(this);
         dialog.setMessage("Please wait..");
         dialog.setCancelable(false);
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";


//        // Circular Image....
//        images.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                resultLauncher.launch(intent);
//            }
//        });
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               if (result.getResultCode()==RESULT_OK && result.getData()!=null){
                   imageUri = result.getData().getData();
                   images.setImageURI(imageUri);
               }
            }
        });
        // SignIn....
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this,Login.class);
                startActivity(i);
                finish();
            }
        });


        // SigUp...
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();
                String status ="Hey there I'm using ChatApp";
                dialog.show();
                if (Name.isEmpty()){
                    Toast.makeText(Registration.this,"Enter Name!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(Email.isEmpty()){
                    Toast.makeText(Registration.this,"Enter Email!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(Password.isEmpty()){
                    Toast.makeText(Registration.this,"Enter Password!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(Password.length()<6){
                    Toast.makeText(Registration.this,"Password is too short!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(!(Password.matches(pattern))){
                    Toast.makeText(Registration.this,"Enter valid Password!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(!(Password.equals(ConfirmPassword))){
                    Toast.makeText(Registration.this,"Re-Enter Confirm Password!! ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                else if(Password.equals(ConfirmPassword)){
                    mAuth.createUserWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()){
                              DatabaseReference reference = database.getReference().child("user").child(mAuth.getUid());
                              StorageReference storageReference = storage.getReference().child("upload").child(mAuth.getUid());
                             if (imageUri!=null){
                                 storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                         if (task.isSuccessful()){
                                             storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri) {
                                                   String ImageUri = uri.toString();
                                                   User user = new User(mAuth.getUid(),Name,Email,ImageUri,status);
                                                   reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           dialog.dismiss();
                                                         if (task.isSuccessful()){
                                                             Intent i = new Intent(Registration.this,MainActivity.class);
                                                             startActivity(i);
                                                             finish();
                                                         }
                                                       }
                                                   });
                                                 }
                                             });
                                         }
                                     }
                                 });
                             }
                             else {
                                 String ImageUri = "https://firebasestorage.googleapis.com/v0/b/chatapp-7768e.appspot.com/o/profile.png?alt=media&token=995d4f92-54da-44c6-bb03-047fc8cc6b1d";
                                 User user = new User(mAuth.getUid(),Name,Email,ImageUri,status);
                                 reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful()){
                                             Intent i = new Intent(Registration.this,MainActivity.class);
                                             startActivity(i);
                                             finish();
                                         }
                                     }
                                 });
                             }
                          }
                        }
                    });
                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Registration.this,"SignUp Failed !!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}