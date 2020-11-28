package com.example.cake.BuyerHome;

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

import com.example.cake.R;
import com.example.cake.Utils.BuyerModel;
import com.example.cake.Utils.BuyerOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerOrderActivity extends AppCompatActivity {

    private static final String TAG = "BuyerOrderActivity";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<BuyerOrder> list;
    private Context mcontext=BuyerOrderActivity.this;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order);
        getSupportActionBar().setTitle("Order placed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView=(RecyclerView) findViewById(R.id.buyer_order_recycler);
        progressBar=(ProgressBar) findViewById(R.id.buyer_order_progress);
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
            mRef= FirebaseDatabase.getInstance().getReference("information").child("order").child(userId);
            Log.d(TAG, "setData: started");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list=new ArrayList<>();
                    for (DataSnapshot ds:snapshot.getChildren())
                    {
                        for(DataSnapshot ds1:ds.getChildren()) {
                            list.add(ds1.getValue(BuyerOrder.class));
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                    Orderadapter orderadapter=new Orderadapter(list,mcontext);
                    recyclerView.setAdapter(orderadapter);
                    orderadapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: "+error.getMessage());
                }
            });
        }
    }
}