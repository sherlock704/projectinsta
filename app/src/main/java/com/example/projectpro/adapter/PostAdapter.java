package com.example.projectpro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectpro.CommentActivity;
import com.example.projectpro.MessageActivity;
import com.example.projectpro.R;
import com.example.projectpro.model.Chat;
import com.example.projectpro.model.Post;
import com.example.projectpro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context mcontext;
    private List<Post> mPost;
    FirebaseUser firebaseUser;

    public PostAdapter(Context mcontext, List<Post> mPost) {
        this.mcontext = mcontext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.post_item, parent ,false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final Post post=mPost.get(position);
        Glide.with(mcontext).load(post.getPostimage()).into(holder.postImage);
        if(post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }
        publisherinfo(holder.proImage,holder.name,holder.publisher,post.getPublisher());
        isLikes(post.getPostid(),holder.likeim);
        mrlikes(holder.like,post.getPostid());
        holder.likeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.likeim.getTag().equals("like")){

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                    .child((firebaseUser.getUid())).setValue(true);

                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                            .child((firebaseUser.getUid())).removeValue();

                }
            }
        });
        holder.comim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext, CommentActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView proImage,postImage,likeim,comim,save;
        TextView name,like,description,publisher,totalcom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proImage=itemView.findViewById(R.id.profile);
            postImage=itemView.findViewById(R.id.post);
            likeim=itemView.findViewById(R.id.like);
            comim=itemView.findViewById(R.id.comment);
           save=itemView.findViewById(R.id.bookmark);
            name=itemView.findViewById(R.id.username);
            like=itemView.findViewById(R.id.likes);
            description=itemView.findViewById(R.id.description);
           publisher=itemView.findViewById(R.id.publisher);
            totalcom=itemView.findViewById(R.id.comments);
        }
    }
    private void isLikes(String postid,final ImageView imageView){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void mrlikes(final TextView likes,String postid){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            likes.setText(dataSnapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void publisherinfo(final ImageView proImage,final TextView name,final TextView publisher,final String userid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(mcontext).load(user.getImageUrl()).into(proImage);
                name.setText(user.getUsername());
               // publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
