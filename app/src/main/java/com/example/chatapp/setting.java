package com.example.chatapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class setting extends AppCompatActivity {
EditText name,status;
CircleImageView images;
ImageView saved;
FirebaseAuth mAuth;
FirebaseDatabase database;
FirebaseStorage storage;
Uri imageUri;
    ProgressDialog dialog;
    String email;
ActivityResultLauncher<Intent> resultLauncher ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //Find ID.....
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        saved = findViewById(R.id.save);
        images = findViewById(R.id.setting_image);




        // FireBase Insatance......
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        DatabaseReference reference = database.getReference().child("user").child(mAuth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(mAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                String ImageUri = snapshot.child("imageUri").getValue().toString();
                String Name = snapshot.child("name").getValue().toString();
                String Status = snapshot.child("status").getValue().toString();
                String Uid = snapshot.child("uid").getValue().toString();


                name.setText(Name);
                status.setText(Status);
                Picasso.get().load(ImageUri).into(images);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher.launch(intent);
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK && result.getData()!=null){
                    imageUri = result.getData().getData();
                    images.setImageURI(imageUri);
                }
            }
        });

        dialog  = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);


        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
              String setting_name = name.getText().toString();
                String setting_status=status.getText().toString();
                if (imageUri!=null){
                 storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           String finalUri = uri.toString();
                            User user = new User(mAuth.getUid(),setting_name,email,finalUri,setting_status);
                           reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       dialog.dismiss();
                                       Toast.makeText(setting.this, "Update Successful!!", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(setting.this,MainActivity.class));
                                       finish();
                                   }
                                   else {
                                       dialog.dismiss();
                                       Toast.makeText(setting.this, "Update Failed!!", Toast.LENGTH_SHORT).show();

                                   }
                               }
                           });
                        }
                    });
                     }
                 });
                }
                else{
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalUri = uri.toString();
                            User user = new User(mAuth.getUid(),setting_name,email,finalUri,setting_status);
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(setting.this, "Update Successful!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(setting.this,MainActivity.class));
                                        finish();
                                    }
                                    else {
                                        dialog.dismiss();
                                        Toast.makeText(setting.this, "Update Failed!!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
    }
}