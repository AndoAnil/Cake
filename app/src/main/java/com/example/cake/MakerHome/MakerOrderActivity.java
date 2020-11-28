package com.example.cake.MakerHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cake.BuyerHome.BuyerOrderActivity;
import com.example.cake.BuyerHome.Orderadapter;
import com.example.cake.R;
import com.example.cake.Utils.BuyerOrder;
import com.example.cake.Utils.StoreOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakerOrderActivity extends AppCompatActivity {

    private static final String TAG = "MakerOrderActivity";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<StoreOrder> list;
    private Context mcontext= MakerOrderActivity.this;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_order);
        getSupportActionBar().setTitle("Received Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView) findViewById(R.id.maker_order_recycler);
        progressBar=(ProgressBar) findViewById(R.id.maker_order_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        setData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData()
    {
        FirebaseUser firebaseUser=mAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            String userId=firebaseUser.getUid();
            mRef= FirebaseDatabase.getInstance().getReference("order");
            Log.d(TAG, "setData: started");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list=new ArrayList<>();
                    for (DataSnapshot ds:snapshot.getChildren())
                    {
                        for(DataSnapshot ds1:ds.getChildren()) {
                            list.add(ds1.getValue(StoreOrder.class));
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                    MakerOrderAdapter adapter=new MakerOrderAdapter(list,mcontext);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: "+error.getMessage());
                }
            });
        }
    }
}