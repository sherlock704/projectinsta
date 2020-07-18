package com.example.projectpro.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectpro.R;
import com.example.projectpro.adapter.PostAdapter;
import com.example.projectpro.adapter.UserAdapter;
import com.example.projectpro.model.Post;
import com.example.projectpro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> mPost;
    private List<String> followlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recyle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mPost=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(),mPost);
        recyclerView.setAdapter(postAdapter);
        checkFollowing();
        return view;
    }

    private void checkFollowing(){
        followlist=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followlist.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    followlist.add(snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    for(String id:followlist){
                        if(post.getPublisher().equals(id)){
                            mPost.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
