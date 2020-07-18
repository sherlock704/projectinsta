package com.example.projectpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectpro.fragment.HomeFragment;
import com.example.projectpro.fragment.LikeFragment;
import com.example.projectpro.fragment.ProfileFragment;
import com.example.projectpro.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView circle;
    TextView textView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
   BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circle=findViewById(R.id.circleimage);
        textView=findViewById(R.id.username);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                textView.setText(user.getUsername());
                if(user.getImageUrl().equals("default")){
                    circle.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MainActivity.this).load(user.getImageUrl()).into(circle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        bottomNavigationView=findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()){
                        case R.id.home:
                            selectedFragment=new HomeFragment();
                            break;

                        case R.id.add:
                            Intent intent = new Intent(MainActivity.this, PostActivity.class);
                            finish();
                            startActivity(intent);

                            break;

                        case R.id.like:
                            selectedFragment=new LikeFragment();

                            break;

                        case R.id.profile:

                            SharedPreferences.Editor editor
                                    = getSharedPreferences("PREPS",MODE_PRIVATE).edit();
                           editor.putString("profeild",FirebaseAuth.getInstance().getCurrentUser().getUid());
                           editor.apply();
                           selectedFragment=new ProfileFragment();
                            break;
                    }
                    if(selectedFragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedFragment).commit();
                    }
                    return true;
                }
            };


}
