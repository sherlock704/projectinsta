package com.example.projectpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {

    EditText name,password;
    Button loginbtn;
    TextView signuptext;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser!=null){
                    // Toast.makeText(signUp.this,"you are logged in",Toast.LENGTH_LONG).show();

                }else{
                    // Toast.makeText(signUp.this,"please login",Toast.LENGTH_LONG).show();
                }

            }
        };

        name=findViewById(R.id.editEmail);
        password=findViewById(R.id.editPassword);
        loginbtn=findViewById(R.id.btnLogin);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });

    }


    private void userlogin() {
        String uname = name.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (TextUtils.isEmpty(uname)) {

            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(uname).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();

            return;
        }

        firebaseAuth.signInWithEmailAndPassword(uname, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(signUp.this, "error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
