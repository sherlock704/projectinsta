package com.example.projectpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(welcome.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void sign(View view) {
        Intent intent = new Intent(welcome.this, signUp.class);
        finish();
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(welcome.this, Login.class);
        finish();
        startActivity(intent);
    }
}
