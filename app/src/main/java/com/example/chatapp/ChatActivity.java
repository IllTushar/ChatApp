package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
String reciverImage,reciverName,reciverUid,SenderUid;
CircleImageView circleImageView;
TextView reciverNames;
FirebaseAuth mAuth;
FirebaseDatabase database;
public static String simage;
public static String rimage;
CardView sendbtn;
EditText sendMessage;
String senderRoom,reciverRoom;
RecyclerView messageAdapter;
ArrayList<Messages>messagesArrayList;
MessagesAdapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messagesArrayList = new ArrayList<>();
        reciverUid = getIntent().getStringExtra("uid");
        reciverName = getIntent().getStringExtra("name");
        reciverImage = getIntent().getStringExtra("ReciverImage");
        circleImageView = findViewById(R.id.image1);
        reciverNames = findViewById(R.id.recname);

        Picasso.get().load(reciverImage).into(circleImageView);
        reciverNames.setText(""+reciverName);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sendbtn = findViewById(R.id.sendBtn);
        sendMessage = findViewById(R.id.sendMessages);
        SenderUid = mAuth.getUid();
     try {

         Adapter = new MessagesAdapter(ChatActivity.this,messagesArrayList);
         messageAdapter = findViewById(R.id.messageAdapter);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         linearLayoutManager.setStackFromEnd(true);
        //   messageAdapter.setHasFixedSize(true);
         Log.d("##check","present");
         messageAdapter.setLayoutManager(linearLayoutManager);

         Log.d("##check","present1");
         messageAdapter.setAdapter(Adapter);

     }catch (Exception e){
         e.printStackTrace();
     }


        senderRoom = SenderUid+reciverUid;
        reciverRoom = reciverUid+SenderUid;
        DatabaseReference reference = database.getReference().child("user").child(mAuth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                     simage = snapshot.child("ImageUri").toString();
                     rimage =reciverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Sendmessage= sendMessage.getText().toString();
                if (Sendmessage.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please Enter Valid Message!!", Toast.LENGTH_SHORT).show();
                return;
                }




                    sendMessage.setText("");
                    Date date = new Date();

                    Messages messages = new Messages(Sendmessage, SenderUid, date.getTime());
                    database =FirebaseDatabase.getInstance();
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference().child("chats")
                                            .child(reciverRoom)
                                            .child("messages")
                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

            }
        });

    }
}