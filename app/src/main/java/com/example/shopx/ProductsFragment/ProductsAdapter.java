package com.example.shopx.ProductsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.R;

import java.util.List;

import com.example.shopx.Repository;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.viewHolder> {
    private List<ProductInfo> products;
    private Context mContext;
    private onItemClickListener listener;
    private Repository repository;
    public ProductsAdapter(Context mContext, onItemClickListener listener) {

        this.mContext = mContext;
        this.listener = listener;
        repository = new Repository();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_v1, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.product_name.setText(products.get(position).getName());
        holder.product_price.setText(products.get(position).getPrice());
        if (products.get(position).isInWish()) {
            holder.fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite_green,null));
        } else {
            holder.fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite,null));
        }

        if (products.get(position).isInCart()) {
            holder.add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart_green,null));
        } else {
            holder.add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart,null));
        }
        Glide.with(mContext).load("https://cf2.s3.souqcdn.com/item/2020/12/13/13/21/74/96/9/item_L_132174969_ffac6131feef0.jpg").into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        if (products != null) return products.size();
        return 0;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView product_name, product_price;
        ImageView product_image;
        AppCompatImageButton fav_btn;
        AppCompatImageButton add_cart_btn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);

            fav_btn = itemView.findViewById(R.id.fav_btn);

            add_cart_btn = itemView.findViewById(R.id.cart_btn);

            fav_btn.setOnClickListener(v ->
            {
                boolean isWish = products.get(getAdapterPosition()).isInWish();
                boolean isCart = products.get(getAdapterPosition()).isInCart();
                String category= products.get(getAdapterPosition()).getCategory();
                if (isWish) {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite,null));
                    products.get(getAdapterPosition()).setInWish(false);
                } else {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite_green,null));
                    products.get(getAdapterPosition()).setInWish(true);
                }

                repository.addToUserProducts(products.get(getAdapterPosition()).getId(),
                        !isWish,isCart,category);

            });

            add_cart_btn.setOnClickListener(v ->
            {
                boolean isWish = products.get(getAdapterPosition()).isInWish();
                boolean isCart = products.get(getAdapterPosition()).isInCart();
                String category= products.get(getAdapterPosition()).getCategory();
                if (isCart) {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart,null));
                    products.get(getAdapterPosition()).setInCart(false);
                } else {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart_green,null));
                    products.get(getAdapterPosition()).setInCart(true);
                }

                repository.addToUserProducts(products.get(getAdapterPosition()).getId(),
                        isWish,!isCart,category);
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId = getAdapterPosition();
            ProductInfo product = products.get(itemId);
            listener.onItemClick(product);
        }
    }

    public interface onItemClickListener {
        void onItemClick(ProductInfo product);
    }
}
