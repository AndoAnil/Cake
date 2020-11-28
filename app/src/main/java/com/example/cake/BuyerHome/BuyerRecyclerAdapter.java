package com.example.cake.BuyerHome;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cake.R;
import com.example.cake.Utils.AddCakeInfo;
import com.example.cake.Utils.BuyerOrder;
import com.example.cake.Utils.MakerMOdel;
import com.example.cake.Utils.StoreOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyerRecyclerAdapter extends RecyclerView.Adapter<BuyerRecyclerAdapter.Viewholder> {


    private List<AddCakeInfo> list;
    private Context mContext;
    private static final String TAG = "BuyerRecyclerAdapter";

    public BuyerRecyclerAdapter(List<AddCakeInfo> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_item_data, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
     holder.cakename.setText("Type-"+list.get(position).getCakename());
     holder.weight.setText("weight-"+list.get(position).getWeight());
     Glide.with(mContext).load(list.get(position).getImageUrl()).into(holder.cakeImage);
     holder.order.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             final AlertDialog.Builder[] alBuilder = {new AlertDialog.Builder(mContext)};
             ViewGroup viewGroup=(ViewGroup) view.findViewById(R.id.content);
             View v=LayoutInflater.from(mContext).inflate(R.layout.order_bottomsheet, viewGroup, false);
             ImageView cake=(ImageView) v.findViewById(R.id.cakeImage_order);
             TextView cake_name=(TextView) v.findViewById(R.id.cake_type_name);
             TextView cake_weight=(TextView) v.findViewById(R.id.cake_weight_order);
             TextView restName=(TextView) v.findViewById(R.id.restorent_name);
             TextView restAddres=(TextView) v.findViewById(R.id.restorent_address);
             TextView cake_price=(TextView) v.findViewById(R.id.cake_price_order);
             TextView totalPrice=(TextView) v.findViewById(R.id.total_ammount);
             EditText qValue=(EditText) v.findViewById(R.id.how_much_cake);

             Button confirm=(Button) v.findViewById(R.id.confirm_order_bt);

             Glide.with(mContext).load(list.get(position).getImageUrl()).into(cake);
             cake_name.setText(list.get(position).getCakename());
             cake_weight.append(list.get(position).getWeight()+"Kg");
             cake_price.setText(list.get(position).getPrice());

             FirebaseAuth mAuth=FirebaseAuth.getInstance();
             FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
             DatabaseReference mRef;
             FirebaseUser user=mAuth.getCurrentUser();
             if(user!=null)
             {
                 String restId=list.get(position).getStoreId();
                 mRef=FirebaseDatabase.getInstance().getReference("information").child("maker").child(restId);
                 mRef.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         MakerMOdel makerMOdel=snapshot.getValue(MakerMOdel.class);
                         restName.setText(makerMOdel.getRestname());
                         restAddres.setText(makerMOdel.getAddress());
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {
                         Toast.makeText(mContext, "Store does not exists", Toast.LENGTH_SHORT).show();
                     }
                 });
             }

             qValue.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                 }

                 @Override
                 public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                     try {
                         if(qValue.getText().toString().isEmpty())
                         {
                             totalPrice.setText("");
                         }
                         else if(Integer.parseInt(qValue.getText().toString()) < Integer.parseInt(list.get(position).getQuantity())) {
                             int v = Integer.parseInt(qValue.getText().toString()) * Integer.parseInt(cake_price.getText().toString());
                             totalPrice.setText(Integer.toString(v));
                         }
                         else {
                             totalPrice.setText("Quantity is Temporary Unavailable");
                         }
                     }catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void afterTextChanged(Editable editable) {

                 }
             });

             final boolean[] o = {false};
             confirm.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     FirebaseAuth mAuth=FirebaseAuth.getInstance();
                     FirebaseUser user1=mAuth.getCurrentUser();
                     String currentId=user1.getUid();
                     if(user1!=null && totalPrice.getText().toString().length()!=0 && o[0] ==false)
                     {
                         BuyerOrder order=new BuyerOrder(list.get(position).getStoreId(),
                                 list.get(position).getCakename() , totalPrice.getText().toString() , qValue.getText().toString() , list.get(position).getImageUrl(),list.get(position).getWeight());
                         DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("information")
                                 .child("order");
                         dbRef.child(currentId)
                                 .child(list.get(position).getStoreId())
                                 .child(list.get(position).getCakename())
                                 .setValue(order)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful())
                                      {

                                      }
                                     }
                                 });

                         StoreOrder storeOrder=new StoreOrder(currentId,list.get(position).getCakename(),
                                 totalPrice.getText().toString(),qValue.getText().toString(),list.get(position).getImageUrl(),list.get(position).getWeight());
                         DatabaseReference mRef1=FirebaseDatabase.getInstance().
                                   getReference("order");
                         mRef1.child(list.get(position).getStoreId())
                                 .child(currentId)
                                 .child(list.get(position).getCakename())
                                 .setValue(storeOrder)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         Log.d(TAG, "onComplete: Successfully added to Maker DB");
                                         Toast.makeText(mContext, "Successfully order placed", Toast.LENGTH_SHORT).show();
                                         o[0] =true;
                                         sendNotification(list.get(position).getStoreId());

                                     }
                                 });

                     }
                     else {
                         Toast.makeText(mContext, "Please Go to Home and Try again", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
             alBuilder[0].setView(v);
             AlertDialog dialog= alBuilder[0].create();
             dialog.show();
         }
     });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        ImageView cakeImage;
        TextView weight,cakename;
        Button order;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cakeImage=(ImageView) itemView.findViewById(R.id.cake_image_buyer);
            weight=(TextView) itemView.findViewById(R.id.cake_weight_item_buyer);
            cakename=(TextView) itemView.findViewById(R.id.cake_name_item_buyer);
            order=(Button) itemView.findViewById(R.id.order_cake);
        }
    }

    private void sendNotification(String store)
    {
        String url="https://fcm.googleapis.com/fcm/send";
        FirebaseMessaging.getInstance().subscribeToTopic(store);
        RequestQueue requestQueue=Volley.newRequestQueue(mContext);
        JSONObject mainObject=new JSONObject();
        try{
            mainObject.put("to","/topics/"+"Order");
            JSONObject notification=new JSONObject();
            notification.put("title","Cake order");
            notification.put("body","You have received order from buyer");
            mainObject.put("notification",notification);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,
                    mainObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponseNotification: "+error.getMessage());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAj9h-zsM:APA91bFn2wozTWx5F5-zXgU7pw4uxDPeIxmYfig5juvWXCOO6_aXoey4v_wDnZbA_SH2759V5uYK_S636W3ZG8fBFcvQELwEX51hMB-ePmGPiCt8eArYAvNwfvYYhGKly1DS161pMG8S");
                    return header;
                }
            };
            requestQueue.add(request);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
