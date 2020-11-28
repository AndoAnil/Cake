package com.example.cake.Register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cake.MainActivity;
import com.example.cake.R;
import com.example.cake.Utils.BuyerModel;
import com.example.cake.Utils.MakerMOdel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakerRegister extends Fragment {
    private static final String TAG = "MakerRegister";

    private EditText nameEt,emailEt,passwordEt,confirmpassEt,addressEt;
    private Button register;
    private TextView have_account;
    private LinearLayout progress;
    private Context mcontext;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mRef;
    private String userId="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.maker_register_fragment,container,false);
        nameEt=(EditText) view.findViewById(R.id.maker_res_name);
        emailEt=(EditText) view.findViewById(R.id.maker_email_register);
        passwordEt=(EditText) view.findViewById(R.id.maker_password_register);
        confirmpassEt=(EditText) view.findViewById(R.id.maker_confirm_pass);
        register=(Button) view.findViewById(R.id.maker_register_btn);
        have_account=(TextView) view.findViewById(R.id.maker_have_account);
        progress=(LinearLayout) view.findViewById(R.id.maker_register_progress);
        addressEt=(EditText) view.findViewById(R.id.maker_register_address);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext=getActivity();
        mAuth=FirebaseAuth.getInstance();
        mdatabase=FirebaseDatabase.getInstance();
        mRef=mdatabase.getReference();
        progress.setVisibility(View.INVISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEt.getText().toString();
                String password=passwordEt.getText().toString();
                String confirmPassword=confirmpassEt.getText().toString();
                String restName=nameEt.getText().toString();
                String addres=addressEt.getText().toString();
                if(email.isEmpty() || password.isEmpty() ||confirmPassword.isEmpty() || restName.isEmpty() || addres.isEmpty())
                {
                    Toast.makeText(mcontext, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmPassword))
                {
                    confirmpassEt.setError("Invalid password");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progress.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user=mAuth.getCurrentUser();
                                        userId=user.getUid();
                                        sendingVerificationMail();
                                        MakerMOdel model=new MakerMOdel(restName,addres,email,"123344","");
                                        mRef.child("information")
                                                .child("maker")
                                                .child(userId)
                                                .setValue(model)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(mcontext, "Successfully registered", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                    else
                                    {
                                        Toast.makeText(mcontext, "Something wrong please try again", Toast.LENGTH_SHORT).show();
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

    private void sendingVerificationMail() {
        FirebaseUser current=FirebaseAuth.getInstance().getCurrentUser();
        if(userId!=null)
        {
            current.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(mcontext, "Verify email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mcontext, MainActivity.class));
                            }
                        }
                    });
        }
    }
}
