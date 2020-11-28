package com.example.cake.BuyerHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cake.MainActivity;
import com.example.cake.MakerHome.MakerIemAdapter;
import com.example.cake.R;
import com.example.cake.Utils.AddCakeInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerHomeActivity extends AppCompatActivity {

    private static final String TAG = "BuyerHomeActivity";
    private RecyclerView buyerRecyclerView;
    private List<AddCakeInfo> list;
    private BuyerRecyclerAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String sh="usertype";
    private String userTye="userType";
    private ProgressBar progressBar;
    private Context mcontext=BuyerHomeActivity.this;

    //Firebase
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);
        buyerRecyclerView=(RecyclerView) findViewById(R.id.buyer_recycler_view);
        progressBar=(ProgressBar) findViewById(R.id.buyer_recycler_data);
        sharedPreferences=getSharedPreferences(sh, Context.MODE_PRIVATE);
        setUserType();
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mRef=firebaseDatabase.getReference();
        buyerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();
    }

    private void setData() {
        FirebaseUser user=mAuth.getCurrentUser();
        String userId=user.getUid();
        if(user!=null)
        {
            mRef=FirebaseDatabase.getInstance().getReference("store");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {

                        for(DataSnapshot ds1:ds.getChildren()) {
                            Log.d(TAG, "onDataChange: " + ds1.getKey().toString());
                            list.add(ds1.getValue(AddCakeInfo.class));
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onDataChange: size"+list.size());
                    adapter=new BuyerRecyclerAdapter(list,mcontext);
                    buyerRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setUserType() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(userTye, "buyer");
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sign_out)
        {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else
        {
          startActivity(new Intent(getApplicationContext(),BuyerOrderActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}