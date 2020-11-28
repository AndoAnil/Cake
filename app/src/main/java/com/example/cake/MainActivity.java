package com.example.cake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cake.BuyerHome.BuyerHomeActivity;
import com.example.cake.MakerHome.MakerHomeActivity;
import com.example.cake.Utils.BottomSheetBuyer;
import com.example.cake.Utils.BottomSheetMaker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button buyer,maker;


    private SharedPreferences sharedPreferences;
    private String sh="usertype";
    private String userTye="userType";

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
        buyer=(Button) findViewById(R.id.btn_buyer);
        maker=(Button) findViewById(R.id.btn_maker);

        sharedPreferences=getSharedPreferences(sh, Context.MODE_PRIVATE);
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetBuyer buyer=new BottomSheetBuyer();
                buyer.show(getSupportFragmentManager(),"Buyer");
            }
        });

        maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetMaker buyer=new BottomSheetMaker();
                buyer.show(getSupportFragmentManager(),"Maker");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            if(sharedPreferences.contains(userTye))
            {
                if(sharedPreferences.getString(userTye,"").equals("maker"))
                {
                    startActivity(new Intent(getApplicationContext(), MakerHomeActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), BuyerHomeActivity.class));
                    finish();
                }
            }
        }
    }
}