package com.example.projectpro.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectpro.MessageActivity;
import com.example.projectpro.R;
import com.example.projectpro.model.User;
import com.example.projectpro.fragment.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {
    private Context mcontext;
    private List<User> muser;
    FirebaseUser firebaseUser;

    public UserAdapter(Context mcontext, List<User> muser) {
        this.mcontext = mcontext;
        this.muser = muser;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item, parent ,false);
        return new UserAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final User user=muser.get(position);
        holder.name.setText(user.getUsername());

            if(user.getImageUrl().equals("default")){
                holder.image.setImageResource(R.mipmap.ic_launcher);
            }else{
                Glide.with(mcontext).load(user.getImageUrl()).into(holder.image);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mcontext, MessageActivity.class);
                    intent.putExtra("userid",user.getId());
                    mcontext.startActivity(intent);
                }
            });



        isFollowing(user.getId(),holder.follow);
        if(user.getId().equals(firebaseUser.getUid())){
            holder.follow.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=mcontext.getSharedPreferences("PREPS",Context.MODE_PRIVATE).edit();
                editor.putString("profeild",user.getId());
                editor.apply();
                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                        new ProfileFragment()).commit();
            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);


                }else{
                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
                }
        });
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView image;
        Button follow;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.username);
            image=itemView.findViewById(R.id.circleimage);
            follow=itemView.findViewById(R.id.btn1);
        }
    }
    private void isFollowing(final String userid,final Button button){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists()){
                    button.setText("following");
                }else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
