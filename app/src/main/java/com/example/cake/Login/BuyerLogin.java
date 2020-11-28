package com.example.cake.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;

import com.example.cake.BuyerHome.BuyerHomeActivity;
import com.example.cake.R;
import com.example.cake.Register.BuyerRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BuyerLogin extends Fragment {
    private static final String TAG = "BuyerLogin";

    private EditText emailET,passwordET;
    private Button loginBt;
    private TextView create;
    private Context mcontext;
    private ProgressBar progressBar;
    private TextView wait;
    //Firebase
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.buyer_login_fragment,container,false);
        emailET=(EditText) view.findViewById(R.id.buyer_email);
        passwordET=(EditText) view.findViewById(R.id.buyer_password);
        loginBt=(Button) view.findViewById(R.id.buyer_login_btn);
        progressBar=(ProgressBar) view.findViewById(R.id.buyer_login_progress);
        wait=(TextView) view.findViewById(R.id.buyer_login_wait);
        mcontext=getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        wait.setVisibility(View.INVISIBLE);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailET.getText().toString();
                String pass=passwordET.getText().toString();
                if(email.isEmpty())
                {
                    emailET.setError("Invalid input");
                }
                else if(pass.isEmpty())
                {
                    passwordET.setError("Invalid input");
                }
                else if (email.isEmpty()  && pass.isEmpty())
                {
                    emailET.setError("Invalid input");
                    passwordET.setError("Invalid input");
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user=mAuth.getCurrentUser();
                                        try {
                                            if(user.isEmailVerified())
                                            {
                                                wait.setVisibility(View.VISIBLE);
                                                Log.d(TAG, "onComplete: Email verification success");
                                                Intent intent=new Intent(mcontext, BuyerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(mcontext, "Email is not Verified check email", Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        Log.d(TAG, "onComplete: "+task.getException().getMessage());
                                        Toast.makeText(mcontext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+e.getMessage());
                                }
                            });
                }
            }
        });
    }
}
