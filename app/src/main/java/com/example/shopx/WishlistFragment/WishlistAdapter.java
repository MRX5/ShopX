package com.example.shopx.WishlistFragment;

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
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.R;
import com.example.shopx.Utils.Utils;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.viewHolder> {
    private List<ProductInfo> items;
    private Context mContext;
     private onFavouriteIconClickListener listener;
    public WishlistAdapter(onFavouriteIconClickListener listener,Context mContext)
    {
        this.listener=listener;
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<ProductInfo> items) {
        this.items = items;
        notifyDataSetChanged();
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
                int position=getAdapterPosition();
                listener.onIconClick(items.get(position));
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,items.size());
            });
        }

        public void bind(ProductInfo currProduct) {
            productName.setText(currProduct.getName());
            String price= Utils.formatPrice(currProduct.getPrice());
            productPrice.setText(price);
            Glide.with(mContext).load(currProduct.getImageUrl()).into(productImage);
        }
    }
    interface onFavouriteIconClickListener{
        void onIconClick(ProductInfo product);
    }
}
