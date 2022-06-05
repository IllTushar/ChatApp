package com.example.chatapp;

import static com.example.chatapp.ChatActivity.rimage;
import static com.example.chatapp.ChatActivity.simage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages>messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new receiverViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       Messages messages = messagesArrayList.get(position);

       if (holder.getClass()==senderViewHolder.class){
           senderViewHolder viewHolder = (senderViewHolder) holder;
           viewHolder.textView.setText(messages.getMessage());
           Picasso.get().load(simage).into(viewHolder.circleImageView);
       }else {
           receiverViewHolder viewHolder = (receiverViewHolder) holder;
           viewHolder.textView.setText(messages.getMessage());
           Picasso.get().load(rimage).into(viewHolder.circleImageView);
       }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
         Messages messages = messagesArrayList.get(position);
         if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
             return ITEM_SEND;
         }else {
             return ITEM_RECIVE;
         }
    }

    class senderViewHolder extends RecyclerView.ViewHolder{
            CircleImageView circleImageView;
            TextView textView;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image2);
            textView = itemView.findViewById(R.id.txtmessage1);
        }
    }
    class receiverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image3);
            textView = itemView.findViewById(R.id.txtmessage2);
        }
    }
}
