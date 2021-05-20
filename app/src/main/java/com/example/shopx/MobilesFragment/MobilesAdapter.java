package com.example.shopx.MobilesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopx.R;

import java.util.List;

import com.example.shopx.Model.Mobile;

public class MobilesAdapter extends RecyclerView.Adapter<MobilesAdapter.viewHolder> {
    private List<Mobile>mobiles;
    private Context mContext;
    private onItemClickListener listener;

    public MobilesAdapter(Context mContext, onItemClickListener listener)
    {

        this.mContext=mContext;
        this.listener=listener;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_v1,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.product_name.setText(mobiles.get(position).getName());
        holder.product_price.setText(mobiles.get(position).getPrice());
        Glide.with(mContext).load("https://cf2.s3.souqcdn.com/item/2020/12/13/13/21/74/96/9/item_L_132174969_ffac6131feef0.jpg").into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        if(mobiles!=null)return mobiles.size();
        return 0;
    }

    public void setMobiles(List<Mobile> mobiles) {
        this.mobiles = mobiles;
        notifyDataSetChanged();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView product_name,product_price;
        ImageView product_image;
        AppCompatImageButton fav_btn;
        AppCompatImageButton add_cart_btn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_price=itemView.findViewById(R.id.product_price);
            product_image=itemView.findViewById(R.id.product_image);
            fav_btn=itemView.findViewById(R.id.fav_btn);
            add_cart_btn=itemView.findViewById(R.id.cart_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId=getAdapterPosition();
            Mobile mobile=mobiles.get(itemId);
            listener.onItemClick(mobile.getId());
        }
    }

    interface onItemClickListener
    {
         void onItemClick(String itemId);
    }
}
