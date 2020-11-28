package com.example.cake.MakerHome;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.cake.R;
import com.example.cake.Utils.BuyerModel;

import com.example.cake.Utils.StoreOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MakerOrderAdapter extends RecyclerView.Adapter<MakerOrderAdapter.Viewholder>
{

    private static final String TAG = "MakerOrderAdapter";
    private List<StoreOrder> list;
    private Context mContext;

    public MakerOrderAdapter(List<StoreOrder> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.makeritemdata, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.cakeName.append("Type-"+list.get(position).getCakename());
        holder.price.append("Total price-"+list.get(position).getPrice());
        holder.Quantity.append("Quantity-"+list.get(position).getQuantity());
        Glide.with(mContext).load(list.get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(mContext);
                ViewGroup viewGroup=(ViewGroup) view.findViewById(R.id.content);
                View v=LayoutInflater.from(mContext).inflate(R.layout.dialoge_store_order, viewGroup, false);
                ImageView imageView=(ImageView) v.findViewById(R.id.dialog_cake_image_store);
                TextView cake_name=(TextView) v.findViewById(R.id.cake_type_name_dialog);
                TextView cake_weight=(TextView) v.findViewById(R.id.cake_weight_order_dialog);

                TextView CustName=(TextView) v.findViewById(R.id.custumer_name);
                TextView custAdd=(TextView) v.findViewById(R.id.custumer_address);
                TextView custEmail=(TextView) v.findViewById(R.id.custumer_email);
                TextView cake=(TextView) v.findViewById(R.id.cake_type_name);
                TextView quant=(TextView) v.findViewById(R.id.cake_quantity_dialog);
                TextView weight=(TextView) v.findViewById(R.id.cake_weight_dialog_store);
                TextView totAmt=(TextView) v.findViewById(R.id.cake_price_dialog);

                Glide.with(mContext).load(list.get(position).getImage()).into(imageView);
                cake_name.setText(list.get(position).getCakename());
                cake.setText(list.get(position).getCakename());
                cake_weight.append(list.get(position).getWeight()+"Kg");
                quant.setText(list.get(position).getQuantity());
                totAmt.setText(list.get(position).getPrice());
                weight.append(list.get(position).getWeight()+"Kg");

                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("information").child("buyer").child(list.get(position).getCustumerId());
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BuyerModel buyerModel=snapshot.getValue(BuyerModel.class);
                        CustName.setText(buyerModel.getName());
                        custAdd.setText(buyerModel.getAddress());
                        custEmail.setText(buyerModel.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: "+error.getMessage());
                        Toast.makeText(mContext, "Something wrong wiht Custumer", Toast.LENGTH_SHORT).show();
                    }
                });


                alBuilder.setView(v);
                AlertDialog dialog=alBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView cakeName,Quantity,price;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.cake_image);
            cakeName=(TextView) itemView.findViewById(R.id.cake_name_item);
            Quantity=(TextView) itemView.findViewById(R.id.cake_quantity_item);
            price=(TextView) itemView.findViewById(R.id.cake_weight_item);
        }
    }
}