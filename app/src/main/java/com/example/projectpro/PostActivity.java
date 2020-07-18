package com.example.projectpro;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    Uri imageuri;
    String myUrl="";
    StorageTask upload;
    StorageReference storageReference;
    ImageView close,add;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        close=findViewById(R.id.cancel);
        add=findViewById(R.id.imageadd);
        textView=findViewById(R.id.post);
        editText=findViewById(R.id.descrip);
        storageReference= FirebaseStorage.getInstance().getReference("posts");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this,MainActivity.class));
                finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });
        CropImage.activity().setAspectRatio(1,1)
                .start(PostActivity.this);
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void upLoadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if(imageuri!=null){
            final StorageReference filerefrence=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtention(imageuri));
            upload=filerefrence.putFile(imageuri);
            upload.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isComplete()){
                        throw task.getException();
                    }
                    return filerefrence.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isComplete()){
                        Uri downloadUri= (Uri) task.getResult();
                        myUrl=downloadUri.toString();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
                        String postid=reference.push().getKey();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("postimage", myUrl);
                        hashMap.put("description", editText.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                        finish();

                    }else{
                        Toast.makeText(PostActivity.this,"failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(PostActivity.this,"NO image selected",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageuri=result.getUri();
            add.setImageURI(imageuri);
        }else{
            Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PostActivity.this,MainActivity.class));
        finish();
        super.onBackPressed();
    }

}