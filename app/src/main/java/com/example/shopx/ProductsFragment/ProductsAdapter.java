package com.example.shopx.ProductsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopx.Model.ProductInfo;
import com.example.shopx.R;

import java.util.List;

import com.example.shopx.Repository;
import com.example.shopx.Utils.Utils;

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
        holder.bind(products.get(position));
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
                int position = getAdapterPosition();
                boolean isWish = products.get(position).isInWish();
                boolean isCart = products.get(position).isInCart();
                String category = products.get(position).getCategory();
                if (isWish) {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_favourite, null));
                    products.get(position).setInWish(false);
                    Toast.makeText(mContext, "Product removed from wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_favourite_red, null));
                    products.get(position).setInWish(true);
                    Toast.makeText(mContext, "Product added to wishlist", Toast.LENGTH_SHORT).show();
                }

                listener.onButtonClick(products.get(position).getId(), !isWish, isCart);
                repository.addToUserProducts(products.get(position).getId(),
                        !isWish, isCart, category);

            });

            add_cart_btn.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                boolean isWish = products.get(position).isInWish();
                boolean isCart = products.get(position).isInCart();
                String category = products.get(position).getCategory();
                if (isCart) {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_add_cart, null));
                    products.get(position).setInCart(false);
                    Toast.makeText(mContext, "Product removed from cart", Toast.LENGTH_SHORT).show();

                } else {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_add_cart_green, null));
                    products.get(position).setInCart(true);
                    Toast.makeText(mContext, "Product added to cart", Toast.LENGTH_SHORT).show();

                }
                listener.onButtonClick(products.get(position).getId(), isWish, !isCart);
                repository.addToUserProducts(products.get(position).getId(),
                        isWish, !isCart, category);
            });

            itemView.setOnClickListener(this);
        }

        public void bind(ProductInfo currProduct) {
            product_name.setText(currProduct.getName());
            String price = Utils.formatPrice(currProduct.getPrice());
            product_price.setText(price);

            if (currProduct.isInWish()) {
                fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_favourite_red, null));
            } else {
                fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_favourite, null));
            }

            if (currProduct.isInCart()) {
                add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_add_cart_green, null));
            } else {
                add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.icon_add_cart, null));
            }
            Glide.with(mContext).load(currProduct.getImageUrl()).into(product_image);
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

        void onButtonClick(String productId, boolean inWish, boolean inCart);
    }
}
