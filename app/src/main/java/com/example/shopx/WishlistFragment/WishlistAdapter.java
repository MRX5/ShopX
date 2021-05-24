package com.example.shopx.WishlistFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopx.Model.Wishlist;
import com.example.shopx.R;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.viewHolder> {
    private List<Wishlist> items;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.productName.setText(items.get(position).getProductName());
        holder.productPrice.setText(items.get(position).getProductPrice());
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageButton favBtn;
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            favBtn = itemView.findViewById(R.id.fav_btn);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            favBtn.setOnClickListener(v->
            {

            });
        }
    }
}
