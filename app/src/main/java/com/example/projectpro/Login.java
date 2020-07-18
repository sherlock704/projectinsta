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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText editUserName,editMobile,editEmail,editPassword;
    private Button btnRegister;

    DatabaseReference reff;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editUserName = findViewById(R.id.editUserName);
        editMobile = findViewById(R.id.editMobileNumber);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){

        }
    }

    private void register() {
        final String username=editUserName.getText().toString();
        final String emailt=editEmail.getText().toString();
        String upass=editPassword.getText().toString();
        final String phoneno=editMobile.getText().toString();


        if(TextUtils.isEmpty(emailt)){

            Toast.makeText(this,"please eter email",Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(upass)){
            Toast.makeText(this,"please eter password",Toast.LENGTH_LONG).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailt).matches()) {
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_LONG).show();

            return;
        }

        mAuth.createUserWithEmailAndPassword(emailt,upass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("bio", "");
                            hashMap.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/projectpro-83cf2.appspot.com/o/1120rdz%20(2).JPG?alt=media&token=63cd72c4-bba7-4d4a-b5af-cecf89790c93");

                            reff.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "reguster sucess", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                }
                            });
                          /*  User user=new User(
                                    username,emailt,phoneno
                            );*/
                        }
                        else {
                            Toast.makeText(Login.this,"not success",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
