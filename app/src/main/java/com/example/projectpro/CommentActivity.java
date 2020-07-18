package com.example.projectpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectpro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    EditText com;
    TextView post;
    ImageView profile;
    String postid,publisherid;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        com=findViewById(R.id.addcomment);
        post=findViewById(R.id.post);
        profile=findViewById(R.id.profile);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (com.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this,"you cant send empty",Toast.LENGTH_LONG).show();
                }
                else{
                    addcomment();
                }
            }

        });
        getImage();
    }
    private void addcomment() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",com.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());

        reference.push().setValue(hashMap);
        com.setText("");
    }
    private void getImage(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}