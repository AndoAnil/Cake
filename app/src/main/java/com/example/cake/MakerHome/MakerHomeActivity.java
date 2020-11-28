package com.example.cake.MakerHome;

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
import android.widget.Toast;

import com.example.cake.MainActivity;
import com.example.cake.R;
import com.example.cake.Utils.AddCakeInfo;
import com.example.cake.Utils.MakerMOdel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakerHomeActivity extends AppCompatActivity {

    private static final String TAG = "MakerHomeActivity";
    private Context mContext=MakerHomeActivity.this;
    private FloatingActionButton add_cake;
    private RecyclerView recyclerView;
    private List<AddCakeInfo> list;
    private ProgressBar progressBar;
    private MakerIemAdapter iemAdapter;


    //SharedPrefrence
    private SharedPreferences sharedPreferences;
    private String sh="usertype";
    private String userTye="userType";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    private String userId;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_home);
        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.maker_recycler_progress);
        add_cake=(FloatingActionButton) findViewById(R.id.add_cake);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mRef=firebaseDatabase.getReference();
        settingRestInf();
        sharedPreferences=getSharedPreferences(sh,Context.MODE_PRIVATE);
        setUserType();


        recyclerView=(RecyclerView) findViewById(R.id.maker_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Moving to CakeAddActivity
        add_cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,AddCakeActivity.class));
            }
        });

        setDataRcyclerView();
    }

    private void setUserType() {
        FirebaseUser firebaseUser1=mAuth.getCurrentUser();
        if(firebaseUser1!=null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(userTye, "maker");
            editor.commit();
        }
    }

    private void settingRestInf() {
        firebaseUser=mAuth.getCurrentUser();
        userId=firebaseUser.getUid();
        mRef=FirebaseDatabase.getInstance().getReference("information").child("maker").child(userId);
        FirebaseUser firebaseUser=mAuth.getInstance().getCurrentUser();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             MakerMOdel makerMOdel=snapshot.getValue(MakerMOdel.class);
             getSupportActionBar().setTitle(makerMOdel.getRestname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(mContext,MakerOrderActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    //Setting data to recyclerview

    private void setDataRcyclerView()
    {
        FirebaseUser current=mAuth.getCurrentUser();
        if(current!=null)
        {
            mRef=FirebaseDatabase.getInstance().getReference("store").child(userId);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list = new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        Log.d(TAG, "onDataChange: "+ds.getKey().toString());
                        list.add(ds.getValue(AddCakeInfo.class));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onDataChange: size"+list.size());
                    iemAdapter=new MakerIemAdapter(mContext,list);
                    recyclerView.setAdapter(iemAdapter);
                    iemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}