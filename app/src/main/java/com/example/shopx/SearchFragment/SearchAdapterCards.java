package com.example.shopx.SearchFragment;

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

import com.example.shopx.Model.ProductInfo;
import com.example.shopx.R;
import com.example.shopx.Repository;

import java.util.List;

public class SearchAdapterCards extends RecyclerView.Adapter<SearchAdapterCards.viewHolder> {
    private List<ProductInfo> items;
     private onItemClickListener listener;
     private Context mContext;
     private Repository repository;
    public SearchAdapterCards(onItemClickListener listener,Context mContext)
    {
        this.listener=listener;
        this.mContext=mContext;
        repository=new Repository();
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_v1, parent, false);
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

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName;
        private TextView productPrice;
        private ImageView productImage;
        private AppCompatImageButton fav_btn;
        private AppCompatImageButton add_cart_btn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage=itemView.findViewById(R.id.product_image);
            fav_btn=itemView.findViewById(R.id.fav_btn);
            add_cart_btn=itemView.findViewById(R.id.cart_btn);
            fav_btn.setOnClickListener(this);
            add_cart_btn.setOnClickListener(this);
            itemView.setOnClickListener(v-> {
                ProductInfo currItem=items.get(getAdapterPosition());
                listener.onItemClick(currItem);
            });
        }
        private void bind(ProductInfo currItem) {
            productName.setText(currItem.getName());
            productPrice.setText(currItem.getPrice());
            if(currItem.isInCart())
            {
                add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart_green,null));
            }
            else
            {
                add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart,null));
            }
            if(currItem.isInWish())
            {
                fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite_red,null));
            }
            else
            {
                fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite,null));
            }
        }

        @Override
        public void onClick(View v) {
            int itemId= v.getId();
            if(itemId==R.id.fav_btn)
            {
                boolean isWish = items.get(getAdapterPosition()).isInWish();
                boolean isCart =items.get(getAdapterPosition()).isInCart();
                String category=items.get(getAdapterPosition()).getCategory();
                if (isWish) {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite,null));
                    items.get(getAdapterPosition()).setInWish(false);
                } else {
                    fav_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_favourite_red,null));
                    items.get(getAdapterPosition()).setInWish(true);
                }

                repository.addToUserProducts(items.get(getAdapterPosition()).getId(),
                        !isWish,isCart,category);
            }
            else if(itemId==R.id.cart_btn)
            {
                boolean isWish = items.get(getAdapterPosition()).isInWish();
                boolean isCart = items.get(getAdapterPosition()).isInCart();
                String category=items.get(getAdapterPosition()).getCategory();
                if (isCart) {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart,null));
                    items.get(getAdapterPosition()).setInCart(false);
                } else {
                    add_cart_btn.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.icon_add_cart_green,null));
                    items.get(getAdapterPosition()).setInCart(true);
                }

                repository.addToUserProducts(items.get(getAdapterPosition()).getId(),
                        isWish,!isCart,category);
            }
        }
    }
    interface onItemClickListener{
        void onItemClick(ProductInfo product);
    }
}
