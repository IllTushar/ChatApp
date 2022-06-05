package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context mainActivity;
    ArrayList<User> userArrayList;
    public UserAdapter(MainActivity mainActivity, ArrayList<User> userArrayList) {
        this.mainActivity = mainActivity;
        this.userArrayList  = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         User user = userArrayList.get(position);
//         if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid())){
//             holder.itemView.setVisibility(View.GONE);
//         }
         holder.name.setText(user.name);
         holder.status.setText(user.status);
        Picasso.get().load(user.ImageUri).into(holder.profile_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(mainActivity,ChatActivity.class);
               i.putExtra("name",user.getName());
               i.putExtra("ReciverImage",user.getImageUri());
               i.putExtra("uid",user.getUid());
               mainActivity.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {

        return userArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_image;
        TextView name;
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.mname);
            status = itemView.findViewById(R.id.mstatus);
        }
    }
}
