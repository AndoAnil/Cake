package com.example.cake.MakerHome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cake.R;
import com.example.cake.Utils.AddCakeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddCakeActivity extends AppCompatActivity {
    private static final String TAG = "AddCakeActivity";
    private EditText cakeNameEt, quantityEt, weightEt,priceEt;
    private LinearLayout addImageLayout;
    private ImageView cake_image;
    private Button add_cake_store;
    private int PICK_IMAGE = 2001;
    private Context mcontext = AddCakeActivity.this;
    private Uri imageUri;
    private ProgressBar progressBar;
    private StorageTask uploadTask;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);
        getSupportActionBar().setTitle("Add cake");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_cake_store = (Button) findViewById(R.id.add_cake_btn);
        addImageLayout = (LinearLayout) findViewById(R.id.LinearImage);
        cakeNameEt = (EditText) findViewById(R.id.cake_name);
        quantityEt = (EditText) findViewById(R.id.cake_quantity);
        weightEt = (EditText) findViewById(R.id.cake_weight);
        cake_image = (ImageView) findViewById(R.id.cakeImage);
        priceEt=(EditText) findViewById(R.id.cake_price);
        progressBar = (ProgressBar) findViewById(R.id.image_uploading);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        //Storage
        mStorageRef = FirebaseStorage.getInstance().getReference("cake_images");
        addImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        add_cake_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cakeName=cakeNameEt.getText().toString();
                String quant=quantityEt.getText().toString();
                String weight=weightEt.getText().toString();
                String price=priceEt.getText().toString();
                if(cakeName.isEmpty() || quant.isEmpty() || weight.isEmpty())
                {
                    Toast.makeText(mcontext, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(quant)<1)
                {
                    quantityEt.setError("PLease add quantity more then 1");
                }
                else if(Integer.parseInt(weight)<=0)
                {
                    weightEt.setError("Please add cake weight more then 1kg");
                }
                else
                {
                    if(uploadTask!=null && uploadTask.isInProgress())
                    {
                        Toast.makeText(mcontext, "Uploading is in Progress", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        uploadImage(cakeName, quant, weight,price);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(mcontext).load(imageUri).into(cake_image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void uploadImage(String cakeName,String quantity,String weight,String price) {
        if (imageUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask=fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image=uri.toString();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            FirebaseUser current=mAuth.getCurrentUser();
                            userId=current.getUid();
                            Toast.makeText(mcontext, "Added to Store", Toast.LENGTH_SHORT).show();
                            AddCakeInfo addCakeInfo=new AddCakeInfo(cakeName,quantity,weight,image,price,userId);
                            mRef.child("store")
                                    .child(userId)
                                    .child(cakeName)
                                    .setValue(addCakeInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            startActivity(new Intent(getApplicationContext(),MakerHomeActivity.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    });
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progrss = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progrss);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mcontext, "Image not selected", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(mcontext, "No Image selected", Toast.LENGTH_SHORT).show();

        }
    }
}