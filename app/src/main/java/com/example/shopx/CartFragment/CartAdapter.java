package com.example.shopx.CartFragment;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    private List<ProductInfo> items;
    private CartAdapterInterface listener;
    private double totalPrice = 0;
    private Context mContext;

    public CartAdapter(CartAdapterInterface listener, Context mContext) {
        this.listener = listener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<ProductInfo> items) {
        this.items = items;
        notifyDataSetChanged();
        for (ProductInfo item : items) {
            totalPrice += Double.parseDouble(item.getPrice());
        }
        listener.getTotalPrice(totalPrice);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView pricePerUnit;
        private TextView quantity;
        private AppCompatImageButton increase_btn;
        private AppCompatImageButton decrease_btn;
        private AppCompatImageButton remove_btn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            increase_btn = itemView.findViewById(R.id.increase_btn);
            decrease_btn = itemView.findViewById(R.id.decrease_btn);
            remove_btn = itemView.findViewById(R.id.remove_btn);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            pricePerUnit = itemView.findViewById(R.id.price_per_unit);
            quantity = itemView.findViewById(R.id.quantity);

            increase_btn.setOnClickListener(this);
            decrease_btn.setOnClickListener(this);
            remove_btn.setOnClickListener(this);
        }

        public void bind(ProductInfo product) {
            productName.setText(product.getName());
            String price = Utils.formatPrice(product.getPrice());
            productPrice.setText(price);
            pricePerUnit.setText(price);
            Glide.with(mContext).load(product.getImageUrl()).into(productImage);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            int position = getAdapterPosition();
            long curr_quantity = Long.parseLong(quantity.getText().toString());
            double price = Double.parseDouble(items.get(position).getPrice());
            if (viewId == R.id.increase_btn) {
                curr_quantity++;
                quantity.setText(String.valueOf(curr_quantity));
                totalPrice += price;
                price *= curr_quantity;
                productPrice.setText(String.format("%,.1f",price));
                listener.getTotalPrice(totalPrice);
            } else if (viewId == R.id.decrease_btn) {
                if (curr_quantity > 1) {
                    curr_quantity--;
                    totalPrice -= price;
                }
                quantity.setText(String.valueOf(curr_quantity));
                price *= curr_quantity;
                productPrice.setText(String.format("%,.1f",price));
                listener.getTotalPrice(totalPrice);
            } else if (viewId == R.id.remove_btn) {
                totalPrice -= (curr_quantity * price);
                listener.onRemoveIconClick(items.get(position));
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                listener.getTotalPrice(totalPrice);
            }
        }
    }

    interface CartAdapterInterface {
        void onRemoveIconClick(ProductInfo product);

        void getTotalPrice(double total);
    }
}
