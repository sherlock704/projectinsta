package com.example.projectpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpro.model.Chat;
import com.example.projectpro.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Viewholder> {
    private Context mcontext;
    private List<Chat> mchat;
    FirebaseUser firebaseUser;
    String imageurl;

    public MessageAdapter(Context mcontext, List<Chat> mchat,String imageurl) {
        this.mcontext = mcontext;
        this.mchat = mchat;
        this.imageurl=imageurl;
    }


    @NonNull
    @Override
    public MessageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item, parent ,false);
        return new MessageAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }




    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView image;
        Button follow;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.showmsg);
            image=itemView.findViewById(R.id.circle);
            follow=itemView.findViewById(R.id.btn);
        }
    }

}

