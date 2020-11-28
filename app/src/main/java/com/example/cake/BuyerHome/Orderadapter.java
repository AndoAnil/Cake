package com.example.cake.BuyerHome;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cake.R;
import com.example.cake.Utils.BuyerModel;
import com.example.cake.Utils.BuyerOrder;
import com.example.cake.Utils.MakerMOdel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Orderadapter extends RecyclerView.Adapter<Orderadapter.Viewholder> {
    private List<BuyerOrder> list;
    private Context mContext;
    private static final String TAG = "Orderadapter";

    public Orderadapter(List<BuyerOrder> list, Context mContext) {
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
        holder.price.append("Total price-"+list.get(position).getCakeprice());
        holder.Quantity.append("Quantity-"+list.get(position).getQuantity());
        Glide.with(mContext).load(list.get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(mContext);
                ViewGroup viewGroup=(ViewGroup) view.findViewById(R.id.content);
                View v=LayoutInflater.from(mContext).inflate(R.layout.dialog_buyer_order, viewGroup, false);
                ImageView imageView=(ImageView) v.findViewById(R.id.dialog_cake_image_buyer);
                TextView cake_name=(TextView) v.findViewById(R.id.cake_type_name_dialog_buyer);
                TextView cake_weight=(TextView) v.findViewById(R.id.cake_weight_order_dialog_buyer);

                TextView restName=(TextView) v.findViewById(R.id.restaurant_name);
                TextView restAdd=(TextView) v.findViewById(R.id.restaurant_address);
                TextView restEmail=(TextView) v.findViewById(R.id.restaurant_email);
                TextView cake=(TextView) v.findViewById(R.id.cake_type_name_buyer);
                TextView quant=(TextView) v.findViewById(R.id.cake_quantity_dialog_buyer);
                TextView weight=(TextView) v.findViewById(R.id.cake_weight_dialog_buyer);
                TextView totAmt=(TextView) v.findViewById(R.id.cake_price_dialog_buyer);

                Glide.with(mContext).load(list.get(position).getImage()).into(imageView);
                cake.setText(list.get(position).getCakename());
                cake_name.setText(list.get(position).getCakename());
                quant.setText(list.get(position).getQuantity());
                totAmt.setText(list.get(position).getCakeprice());
                weight.append(list.get(position).getWeight()+"Kg");
                cake_weight.append(list.get(position).getWeight()+"Kg");

                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                DatabaseReference mRef= FirebaseDatabase.getInstance().getReference("information").child("maker").child(list.get(position).getStoreId());
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MakerMOdel makerMOdel=snapshot.getValue(MakerMOdel.class);
                        restName.setText(makerMOdel.getRestname());
                        restAdd.setText(makerMOdel.getAddress());
                        restEmail.setText(makerMOdel.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: "+error.getMessage());
                        Toast.makeText(mContext, "Something went wrong ", Toast.LENGTH_SHORT).show();
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
