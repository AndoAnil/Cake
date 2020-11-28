package com.example.cake.MakerHome;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cake.R;
import com.example.cake.Utils.AddCakeInfo;

import java.util.List;


public class MakerIemAdapter extends RecyclerView.Adapter<MakerIemAdapter.Viewholder> {


    private Context mcontext;
    private List<AddCakeInfo> list;

    public MakerIemAdapter(Context mcontext, List<AddCakeInfo> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.makeritemdata, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.cake_name.append("Type-"+list.get(position).getCakename());
        holder.cake_quan.append("Quantity-"+list.get(position).getQuantity());
        holder.cake_weight.append("Weight-"+list.get(position).getWeight());
        Glide.with(mcontext).load(list.get(position).getImageUrl()).into(holder.cake_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        ImageView cake_image;
        TextView cake_name,cake_quan,cake_weight;
       public Viewholder(@NonNull View itemView) {
           super(itemView);
           cake_image=(ImageView) itemView.findViewById(R.id.cake_image);
           cake_name=(TextView) itemView.findViewById(R.id.cake_name_item);
           cake_quan=(TextView) itemView.findViewById(R.id.cake_quantity_item);
           cake_weight=(TextView) itemView.findViewById(R.id.cake_weight_item);
       }
   }
}
